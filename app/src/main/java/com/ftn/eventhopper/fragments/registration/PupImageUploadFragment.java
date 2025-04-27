package com.ftn.eventhopper.fragments.registration;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ftn.eventhopper.R;
import com.ftn.eventhopper.adapters.ImagePreviewAdapter;
import com.ftn.eventhopper.fragments.registration.viewmodels.PupImageUploadViewModel;
import com.google.android.material.button.MaterialButton;

import java.io.IOException;
import java.util.ArrayList;

import lombok.Getter;
import lombok.Setter;

/**
 * A simple {@link Fragment} subclass.
 * Use the  factory method to
 * create an instance of this fragment.
 */
public class PupImageUploadFragment extends Fragment {

    private NavController navController;

    private PupImageUploadViewModel viewModel;

    @Getter
    @Setter
    private ArrayList<ImagePreviewAdapter.ImagePreviewItem> uploadedImages = new ArrayList<>();
    private ImagePreviewAdapter imagePreviewAdapter;
    private RecyclerView imagePreviewRecyclerView;
    private MaterialButton uploadImageButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requireActivity().getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                navController.popBackStack(R.id.pupCompanyData2Fragment, false);
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pup_image_upload, container, false);
        viewModel = new ViewModelProvider(this).get(PupImageUploadViewModel.class);

        navController = NavHostFragment.findNavController(this);

        uploadImageButton = view.findViewById(R.id.upload_images_btn);
        uploadImageButton.setOnClickListener(v -> selectImages());
        imagePreviewAdapter= new ImagePreviewAdapter(this.uploadedImages, this::removeImage);
        imagePreviewRecyclerView = view.findViewById(R.id.image_preview);
        imagePreviewRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        imagePreviewRecyclerView.setAdapter(imagePreviewAdapter);
        view.findViewById(R.id.next_btn).setOnClickListener(v -> {
            Bundle receivedBundle = getArguments();
            if (!this.uploadedImages.isEmpty()) {
                for (ImagePreviewAdapter.ImagePreviewItem image : uploadedImages) {
                    if (image.isBitmap()) {
                        viewModel.saveBitmapToCache(requireContext(), image.getBitmap());
                    }
                }

                // Add the cached file paths to the bundle
                ArrayList<String> imageFilePaths = new ArrayList<>(viewModel.getImageFilePaths());
                receivedBundle.putStringArrayList("companyPictures", imageFilePaths);
            }

            navController.navigate(R.id.action_to_pup_personal_data, receivedBundle);
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
