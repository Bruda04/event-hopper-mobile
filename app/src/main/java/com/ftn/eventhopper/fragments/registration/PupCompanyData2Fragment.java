package com.ftn.eventhopper.fragments.registration;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ftn.eventhopper.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

/**
     * A simple {@link Fragment} subclass.
     * Use the  factory method to
     * create an instance of this fragment.
     */
public class PupCompanyData2Fragment extends Fragment {

    private NavController navController;
    private TextInputEditText descriptionField, cityField, addressField;
    private TextInputLayout descriptionLayout, cityLayout, addressLayout;
    private String description, city, address;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pup_company_data2, container, false);
        navController = NavHostFragment.findNavController(this);

        Bundle receivedBundle = getArguments();

        retrieveFields(view);

        view.findViewById(R.id.next_btn).setOnClickListener(v -> {
            retrieveData();
            if(!validateFields()){
                receivedBundle.putString("description", description);
                receivedBundle.putString("companyCity", city);
                receivedBundle.putString("companyAddress", address);

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

}
