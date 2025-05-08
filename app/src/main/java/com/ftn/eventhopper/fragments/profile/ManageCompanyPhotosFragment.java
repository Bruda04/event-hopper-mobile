package com.ftn.eventhopper.fragments.profile;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ftn.eventhopper.R;
import com.ftn.eventhopper.adapters.ImagePreviewAdapter;
import com.ftn.eventhopper.fragments.registration.viewmodels.PupImageUploadViewModel;
import com.google.android.material.button.MaterialButton;

import java.io.IOException;
import java.util.ArrayList;

import lombok.Getter;
import lombok.Setter;

public class ManageCompanyPhotosFragment extends Fragment{

    private NavController navController;

    private PupImageUploadViewModel viewModel;

    @Getter
    @Setter
    private ArrayList<ImagePreviewAdapter.ImagePreviewItem> uploadedImages = new ArrayList<>();
    private ImagePreviewAdapter imagePreviewAdapter;
    private RecyclerView imagePreviewRecyclerView;
    private MaterialButton uploadImageButton;

    private ArrayList<String> existingImages;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requireActivity().getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                navController.popBackStack(R.id.company, false);
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_company_photos, container, false);
        viewModel = new ViewModelProvider(this).get(PupImageUploadViewModel.class);

        navController = NavHostFragment.findNavController(this);

        // Get the existing images from the arguments (Bundle)
        this.existingImages = getArguments().getStringArrayList("existingImages");

        // If there are existing images, add them as preview items
        if (existingImages != null) {
            for (String imageUrl : existingImages) {
                // Convert each image URL to a Bitmap or add as an item
                this.uploadedImages.add(new ImagePreviewAdapter.ImagePreviewItem(imageUrl)); // Or use a URL to load images
            }
        }

        uploadImageButton = view.findViewById(R.id.upload_images_btn);
        uploadImageButton.setOnClickListener(v -> selectImages());

        imagePreviewAdapter = new ImagePreviewAdapter(this.uploadedImages, this::removeImage);
        imagePreviewRecyclerView = view.findViewById(R.id.image_preview);
        imagePreviewRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        imagePreviewRecyclerView.setAdapter(imagePreviewAdapter);


        ProgressBar progressBar = view.findViewById(R.id.progress_bar);

        view.findViewById(R.id.next_btn).setOnClickListener(v -> {
            progressBar.setVisibility(View.VISIBLE);

            // Iterate through all uploaded images
            for (ImagePreviewAdapter.ImagePreviewItem image : uploadedImages) {
                if (image.isBitmap()) {
                    // If it's a Bitmap, save it to cache and get the file path
                    String filePath = viewModel.saveBitmapToCache(requireContext(), image.getBitmap());
                } else {
                    // If it's an existing image, add the file path directly
                    viewModel.addExistingImageToFilePaths(image.getImageUrl()); // Add the image URL to the file paths list
                }
            }

            // After processing the images, submit all file paths (including the newly added ones)
            ArrayList<String> imageFilePaths = new ArrayList<>(viewModel.getImageFilePaths());


            viewModel.submitCompanyPhotos(imageFilePaths, () -> {
                progressBar.setVisibility(View.GONE);
                navController.getPreviousBackStackEntry()
                        .getSavedStateHandle()
                        .set("shouldRefresh", true);
                navController.navigate(R.id.company);
            });
        });


        return view;
    }




    private void selectImages() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        startActivityForResult(intent, 101);
    }

    private void removeImage(int position) {
        this.uploadedImages.remove(position);
        imagePreviewAdapter.notifyItemRemoved(position);
        imagePreviewAdapter.notifyItemRangeChanged(position, this.uploadedImages.size());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 101 && resultCode == RESULT_OK) {
            if (data.getClipData() != null) {
                // Multiple images selected
                for (int i = 0; i < data.getClipData().getItemCount(); i++) {
                    Uri imageUri = data.getClipData().getItemAt(i).getUri();
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), imageUri);
                        this.uploadedImages.add(new ImagePreviewAdapter.ImagePreviewItem(bitmap));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } else if (data.getData() != null) {
                // Single image selected
                Uri imageUri = data.getData();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), imageUri);
                    this.uploadedImages.add(new ImagePreviewAdapter.ImagePreviewItem(bitmap));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            imagePreviewAdapter.notifyDataSetChanged();
        }
    }


}

