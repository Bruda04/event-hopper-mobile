package com.ftn.eventhopper.fragments.solutions.services;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

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
import android.widget.Button;
import android.widget.TextView;

import com.ftn.eventhopper.R;
import com.ftn.eventhopper.adapters.ImagePreviewAdapter;
import com.ftn.eventhopper.fragments.solutions.services.viewmodels.ServiceEditViewModel;
import com.google.android.material.button.MaterialButton;

import java.io.IOException;
import java.util.ArrayList;

public class ServiceEditData2Fragment extends Fragment {

    private NavController navController;
    private ServiceEditViewModel viewModel;

    private Button saveButton;
    private Button backButton;
    private RecyclerView imagePreviewRecyclerView;
    private ImagePreviewAdapter imagePreviewAdapter;

    private MaterialButton uploadImagesButton;
    private TextView imagesError;
    private ArrayList<ImagePreviewAdapter.ImagePreviewItem> allImages;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_service_edit_data2, container, false);
        navController = NavHostFragment.findNavController(this);

        viewModel = new ViewModelProvider(requireActivity(),
                new ViewModelProvider.NewInstanceFactory()).get(ServiceEditViewModel.class);

        saveButton = view.findViewById(R.id.next_button);
        backButton = view.findViewById(R.id.back_button);
        uploadImagesButton = view.findViewById(R.id.upload_images_btn);
        imagePreviewRecyclerView = view.findViewById(R.id.image_preview_recycler_view);
        imagesError = view.findViewById(R.id.imagesError);

        setFields();

        saveButton.setOnClickListener(v -> {
            if (validate()) {
                viewModel.updateService();
            }
        });

        backButton.setOnClickListener(v -> {
            navController.navigate(R.id.back_to_edit_service_1);
        });

        viewModel.getEdited().observe(getViewLifecycleOwner(), edited -> {
            if (edited) {
                viewModel.setEdited(false);
                navController.navigate(R.id.action_to_pup_services);
            }
        });

        return view;
    }
    private boolean validate() {
        if (viewModel.getUploadedImages().isEmpty() && viewModel.getOldImages().isEmpty()) {
            imagesError.setVisibility(View.VISIBLE);
            return false;
        } else {
            imagesError.setVisibility(View.GONE);
        }
        return true;
    }

    private void setFields() {
        uploadImagesButton.setOnClickListener(v -> selectImages());
        allImages = new ArrayList<>();
        allImages.addAll(viewModel.getOldImages());
        allImages.addAll(viewModel.getUploadedImages());

        imagePreviewAdapter= new ImagePreviewAdapter(allImages, this::removeImage);
        imagePreviewRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        imagePreviewRecyclerView.setAdapter(imagePreviewAdapter);
    }

    private void removeImage(int i) {
        if (i < viewModel.getOldImages().size()) {
            viewModel.getOldImages().remove(i);
        } else {
            viewModel.getUploadedImages().remove(i - viewModel.getOldImages().size());
        }
        allImages.remove(i);
        imagePreviewAdapter.notifyItemRemoved(i);
        imagePreviewAdapter.notifyItemRangeChanged(i, allImages.size());
    }

    private void selectImages() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        startActivityForResult(intent, 101);
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
                        viewModel.getUploadedImages().add(new ImagePreviewAdapter.ImagePreviewItem(bitmap));
                        allImages.add(new ImagePreviewAdapter.ImagePreviewItem(bitmap));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } else if (data.getData() != null) {
                // Single image selected
                Uri imageUri = data.getData();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), imageUri);
                    viewModel.getUploadedImages().add(new ImagePreviewAdapter.ImagePreviewItem(bitmap));
                    allImages.add(new ImagePreviewAdapter.ImagePreviewItem(bitmap));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            imagePreviewAdapter.notifyDataSetChanged();
        }
    }


}