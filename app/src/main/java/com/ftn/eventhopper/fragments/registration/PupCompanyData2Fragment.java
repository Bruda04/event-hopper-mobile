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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ftn.eventhopper.R;
import com.ftn.eventhopper.adapters.ImagePreviewAdapter;
import com.ftn.eventhopper.fragments.registration.viewmodels.PupImageUploadViewModel;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;

/**
     * A simple {@link Fragment} subclass.
     * Use the  factory method to
     * create an instance of this fragment.
     */
public class PupCompanyData2Fragment extends Fragment {
    private PupImageUploadViewModel viewModel;
    private ArrayList<ImagePreviewAdapter.ImagePreviewItem> uploadedProfilePicture = new ArrayList<>();
    private MaterialButton uploadImageButton;
    private ImagePreviewAdapter imagePreviewAdapter;
    private RecyclerView imagePreviewRecyclerView;
    private NavController navController;
    private TextInputEditText descriptionField, cityField, addressField;
    private TextInputLayout descriptionLayout, cityLayout, addressLayout;
    private String description, city, address;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requireActivity().getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                navController.popBackStack(R.id.pupCompanyData1Fragment, false);
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pup_company_data2, container, false);
        viewModel = new ViewModelProvider(this).get(PupImageUploadViewModel.class);


        navController = NavHostFragment.findNavController(this);
        uploadImageButton = view.findViewById(R.id.upload_profile_picture);
        uploadImageButton.setOnClickListener(v -> selectImages());
        imagePreviewAdapter= new ImagePreviewAdapter(this.uploadedProfilePicture, this::removeImage);
        imagePreviewRecyclerView = view.findViewById(R.id.image_preview);
        imagePreviewRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        imagePreviewRecyclerView.setAdapter(imagePreviewAdapter);



        Bundle receivedBundle = getArguments();

        retrieveFields(view);

        view.findViewById(R.id.next_btn).setOnClickListener(v -> {
            retrieveData();
            if(!validateFields()){
                receivedBundle.putString("description", description);
                receivedBundle.putString("companyCity", city);
                receivedBundle.putString("companyAddress", address);

                if (!this.uploadedProfilePicture.isEmpty()) {
                    Bitmap bitmap = this.uploadedProfilePicture.get(0).getBitmap();
                    if (bitmap != null) {
                        // Save the bitmap to a temporary file and pass its path
                        String filePath = viewModel.saveSingleBitmapToCache(requireContext(), bitmap);

                        // Add the file path to the bundle
                        if (filePath != null) {
                            receivedBundle.putString("profilePicturePath", filePath); // Store file path
                        } else {
                            Log.e("Bitmap Save Error", "Failed to save the profile picture to cache.");
                        }
                    }
                }

                navController.navigate(R.id.action_to_pup_image_upload, receivedBundle);
            }
        });
        return view;
    }



    private void retrieveFields(View view){
        cityLayout = view.findViewById(R.id.register_city_layout);
        descriptionLayout = view.findViewById(R.id.register_description_layout);
        addressLayout = view.findViewById(R.id.register_address_layout);

        cityField = (TextInputEditText) cityLayout.getEditText();
        descriptionField = (TextInputEditText) descriptionLayout.getEditText();
        addressField = (TextInputEditText) addressLayout.getEditText();
    }


    private void retrieveData(){
        description = descriptionField != null ? descriptionField.getText().toString().trim() : "";
        city = cityField != null ? cityField.getText().toString().trim() : "";
        address = addressField != null ? addressField.getText().toString().trim() : "";
    }


    private boolean validateFields(){
        boolean hasError = false;

        if (city.isEmpty()) {
            cityLayout.setError("City is required"); // Show error message
            cityLayout.setBoxStrokeColor(getResources().getColor(R.color.red)); // Highlight in red
            hasError = true;
        } else {
            cityLayout.setError(null); // Clear error
            cityLayout.setBoxStrokeColor(getResources().getColor(R.color.white)); // Reset border color
        }

        if (description.isEmpty()) {
            descriptionLayout.setError("Description is required. ");
            descriptionLayout.setBoxStrokeColor(getResources().getColor(R.color.red));
            hasError = true;
        } else {
            descriptionLayout.setError(null);
            descriptionLayout.setBoxStrokeColor(getResources().getColor(R.color.white)); // Reset border color
        }

        if (address.isEmpty()) {
            addressLayout.setError("Address is required"); // Show error message
            addressLayout.setBoxStrokeColor(getResources().getColor(R.color.red)); // Highlight in red
            hasError = true;
        } else {
            addressLayout.setError(null); // Clear error
            addressLayout.setBoxStrokeColor(getResources().getColor(R.color.white)); // Reset border color
        }

        return hasError;
    }


    private void selectImages() {
        //for profile picture there is only one that can be selected at a time
        if(!this.uploadedProfilePicture.isEmpty()){
            this.removeImage(0);
        }
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, false);
        startActivityForResult(intent, 101);
    }

    private void removeImage(int position) {
        this.uploadedProfilePicture.remove(position);
        imagePreviewAdapter.notifyItemRemoved(position);
        imagePreviewAdapter.notifyItemRangeChanged(position, this.uploadedProfilePicture.size());
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 101 && resultCode == RESULT_OK) {
            // Single image selected
            Uri imageUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), imageUri);
                this.uploadedProfilePicture.add(new ImagePreviewAdapter.ImagePreviewItem(bitmap));
            } catch (IOException e) {
                e.printStackTrace();
            }
            imagePreviewAdapter.notifyDataSetChanged();
        }
    }

}
