package com.ftn.eventhopper.fragments.upgrades;

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
import com.ftn.eventhopper.fragments.upgrades.viewmodels.UpgradeViewModel;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.io.IOException;
import java.util.ArrayList;


public class CompanyData2Fragment extends Fragment {

    private NavController navController;
    private TextInputEditText descriptionField, cityField, addressField;
    private TextInputLayout descriptionLayout , cityLayout, addressLayout;
    private String description, city, address;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requireActivity().getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                navController.popBackStack(R.id.company_information, false);
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_company_data2, container, false);
        navController = NavHostFragment.findNavController(this);


        Bundle receivedBundle = getArguments();

        retrieveFields(view);

        view.findViewById(R.id.next_btn).setOnClickListener(v -> {
            retrieveData();
            if(!validateFields()){
                receivedBundle.putString("description", description);
                receivedBundle.putString("city", city);
                receivedBundle.putString("address", address);
            }

            navController.navigate(R.id.action_to_image_upload, receivedBundle);

        });
        return view;
    }

    private void retrieveFields(View view){
        descriptionLayout = view.findViewById(R.id.register_description_layout);
        cityLayout = view.findViewById(R.id.register_city_layout);
        addressLayout = view.findViewById(R.id.register_address_layout);

        descriptionField = (TextInputEditText) descriptionLayout.getEditText();
        cityField = (TextInputEditText) cityLayout.getEditText();
        addressField = (TextInputEditText) addressLayout.getEditText();
    }


    private void retrieveData(){
        description = descriptionField != null ? descriptionField.getText().toString().trim() : "";
        city = cityField != null ? cityField.getText().toString().trim() : "";
        address = addressField != null ? addressField.getText().toString().trim() : "";

    }


    private boolean validateFields(){
        boolean hasError = false;

        if (description.isEmpty()) {
            descriptionLayout.setError("Description is required. ");
            descriptionLayout.setBoxStrokeColor(getResources().getColor(R.color.red));
            hasError = true;
        } else {
            descriptionLayout.setError(null);
            descriptionLayout.setBoxStrokeColor(getResources().getColor(R.color.white)); // Reset border color
        }

        if (city.isEmpty()) {
            cityLayout.setError("City is required"); // Show error message
            cityLayout.setBoxStrokeColor(getResources().getColor(R.color.red)); // Highlight in red
            hasError = true;
        } else {
            cityLayout.setError(null); // Clear error
            cityLayout.setBoxStrokeColor(getResources().getColor(R.color.white)); // Reset border color
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


}