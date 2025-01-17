package com.ftn.eventhopper.fragments.registration;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
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
import android.widget.Button;
import android.widget.EditText;

import com.ftn.eventhopper.R;
import com.ftn.eventhopper.adapters.ImagePreviewAdapter;
import com.ftn.eventhopper.fragments.registration.viewmodels.OrganizerRegistrationViewModel;
import com.ftn.eventhopper.fragments.registration.viewmodels.PupRegistrationViewModel;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.io.IOException;

public class OrganizerPersonalData2Fragment extends Fragment {
    private NavController navController;
    private OrganizerRegistrationViewModel viewModel;
    private MaterialButton uploadImageButton;
    private ImagePreviewAdapter imagePreviewAdapter;
    private RecyclerView imagePreviewRecyclerView;
    private TextInputEditText phoneField, cityField, addressField;
    private TextInputLayout phoneLayout, cityLayout, addressLayout;
    private String phone, city, address;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requireActivity().getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                navController.popBackStack(R.id.organizerPersonalData1Fragment, false);
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_organizer_personal_data2, container, false);
        navController = NavHostFragment.findNavController(this);
        viewModel = new ViewModelProvider(this).get(OrganizerRegistrationViewModel.class);

        retrieveFields(view);


        uploadImageButton = view.findViewById(R.id.upload_profile_picture);
        uploadImageButton.setOnClickListener(v -> selectImages());
        imagePreviewAdapter= new ImagePreviewAdapter(viewModel.getUploadedImages(), this::removeImage);
        imagePreviewRecyclerView = view.findViewById(R.id.image_preview);
        imagePreviewRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        imagePreviewRecyclerView.setAdapter(imagePreviewAdapter);


        view.findViewById(R.id.register_btn).setOnClickListener(v -> {
            retrieveData();
            if(!validateFields()){
                Bundle receivedBundle = getArguments();
                receivedBundle.putString("phone", phone);
                receivedBundle.putString("city", city);
                receivedBundle.putString("address", address);

                viewModel.register(receivedBundle);
                navController.navigate(R.id.action_to_confirm_email);
            }
        });


        return view;
    }

    private void selectImages() {
        //for profile picture there is only one that can be selected at a time
        if(!viewModel.getUploadedImages().isEmpty()){
            this.removeImage(0);
        }
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, false);
        startActivityForResult(intent, 101);
    }

    private void removeImage(int position) {
        viewModel.getUploadedImages().remove(position);
        imagePreviewAdapter.notifyItemRemoved(position);
        imagePreviewAdapter.notifyItemRangeChanged(position, viewModel.getUploadedImages().size());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 101 && resultCode == RESULT_OK) {
            // Single image selected
            Uri imageUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), imageUri);
                viewModel.getUploadedImages().add(new ImagePreviewAdapter.ImagePreviewItem(bitmap));
            } catch (IOException e) {
                e.printStackTrace();
            }
            imagePreviewAdapter.notifyDataSetChanged();
        }
    }


    private void retrieveFields(View view){
        cityLayout = view.findViewById(R.id.register_city_layout);
        phoneLayout = view.findViewById(R.id.register_phone_number_layout);
        addressLayout = view.findViewById(R.id.register_address_layout);

        cityField = (TextInputEditText) cityLayout.getEditText();
        phoneField = (TextInputEditText) phoneLayout.getEditText();
        addressField = (TextInputEditText) addressLayout.getEditText();
    }


    private void retrieveData(){
        phone = phoneField != null ? phoneField.getText().toString().trim() : "";
        city = cityField != null ? cityField.getText().toString().trim() : "";
        address = addressField != null ? addressField.getText().toString().trim() : "";
    }

    private boolean validateFields(){
        boolean hasError = false;

        if (city.isEmpty()) {
            cityLayout.setError("City is required"); // Show error message
            cityLayout.setBoxStrokeColor(getResources().getColor(R.color.light_error)); // Highlight in red
            hasError = true;
        } else {
            cityLayout.setError(null); // Clear error
            cityLayout.setBoxStrokeColor(getResources().getColor(R.color.white)); // Reset border color
        }

        // Check password
        if (phone.isEmpty()) {
            phoneLayout.setError("Phone number is required. ");
            phoneLayout.setBoxStrokeColor(getResources().getColor(R.color.light_error));
            hasError = true;
        }else if( phone.length() < 8 ) {
            phoneLayout.setError("Phone number must be >8 digits");
            phoneLayout.setBoxStrokeColor(getResources().getColor(R.color.light_error));
            hasError = true;
        } else {
            phoneLayout.setError(null);
            phoneLayout.setBoxStrokeColor(getResources().getColor(R.color.white)); // Reset border color
        }

        if (address.isEmpty()) {
            addressLayout.setError("Address is required"); // Show error message
            addressLayout.setBoxStrokeColor(getResources().getColor(R.color.light_error)); // Highlight in red
            hasError = true;
        } else {
            cityLayout.setError(null); // Clear error
            cityLayout.setBoxStrokeColor(getResources().getColor(R.color.white)); // Reset border color
        }

        return hasError;
    }
}
