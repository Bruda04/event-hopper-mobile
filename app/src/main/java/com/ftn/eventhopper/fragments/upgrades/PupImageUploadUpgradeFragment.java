package com.ftn.eventhopper.fragments.upgrades;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.ftn.eventhopper.R;
import com.ftn.eventhopper.adapters.ImagePreviewAdapter;
import com.ftn.eventhopper.fragments.upgrades.viewmodels.UpgradeViewModel;
import com.ftn.eventhopper.shared.dtos.users.serviceProvider.CompanyDetailsDTO;
import com.ftn.eventhopper.shared.models.locations.Location;
import com.google.android.material.button.MaterialButton;

import java.io.IOException;
import java.util.ArrayList;

import lombok.Getter;
import lombok.Setter;


public class PupImageUploadUpgradeFragment extends Fragment {

    private NavController navController;
    private UpgradeViewModel viewModel;

    @Getter
    @Setter
    private ArrayList<ImagePreviewAdapter.ImagePreviewItem> uploadedImages = new ArrayList<>();
    private ImagePreviewAdapter imagePreviewAdapter;
    private RecyclerView imagePreviewRecyclerView;
    private MaterialButton uploadImageButton;
    private ProgressBar progressBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requireActivity().getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                navController.popBackStack(R.id.company_information2, false);
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pup_image_upload_upgrade, container, false);
        navController = NavHostFragment.findNavController(this);
        viewModel = new ViewModelProvider(this).get(UpgradeViewModel.class);

        progressBar = view.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);

        viewModel.getUpgradeSuccess().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean success) {
                if (success != null) {
                    if (success) {
                        progressBar.setVisibility(View.GONE);
                        navController.navigate(R.id.action_to_success_pup);
                    } else {
                        Toast.makeText(getContext(), "Unsuccessful upgrading", Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                    }
                    // Remove observer to prevent multiple calls
                    viewModel.getUpgradeSuccess().removeObservers(getViewLifecycleOwner());
                }
            }
        });


        uploadImageButton = view.findViewById(R.id.upload_images_btn);
        uploadImageButton.setOnClickListener(v -> selectImages());
        imagePreviewAdapter= new ImagePreviewAdapter(this.uploadedImages, this::removeImage);
        imagePreviewRecyclerView = view.findViewById(R.id.image_preview);
        imagePreviewRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        imagePreviewRecyclerView.setAdapter(imagePreviewAdapter);

        view.findViewById(R.id.next_btn).setOnClickListener(v -> {
            //Log.i("NEXT", "Next steka");
            v.setEnabled(false);
            progressBar.setVisibility(View.VISIBLE);
            Bundle receivedBundle = getArguments();

            CompanyDetailsDTO detailsDTO = new CompanyDetailsDTO();

            detailsDTO.setCompanyName(receivedBundle.getString("companyName"));
            detailsDTO.setCompanyEmail(receivedBundle.getString("companyEmail"));
            detailsDTO.setCompanyPhoneNumber(receivedBundle.getString("companyPhoneNumber"));
            detailsDTO.setCompanyDescription(receivedBundle.getString("description"));
            Location companyLocation = new Location();
            companyLocation.setAddress(receivedBundle.getString("address"));
            companyLocation.setCity(receivedBundle.getString("city"));

            detailsDTO.setCompanyLocation(companyLocation);
            //Log.i("NEXT", "prosao dto bundle");
            if (!this.uploadedImages.isEmpty()) {
                for (ImagePreviewAdapter.ImagePreviewItem image : uploadedImages) {
                    if (image.isBitmap()) {
                        viewModel.saveBitmapToCache(requireContext(), image.getBitmap());
                    }
                }

                ArrayList<String> imageFilePaths = new ArrayList<>(viewModel.getImageFilePaths());
                detailsDTO.setCompanyPhotos(imageFilePaths);
            }

            //Log.i("NEXT", "prosao slike");
            viewModel.upgradeToPup(detailsDTO);
            //Log.i("NEXT", "zavrsio");
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