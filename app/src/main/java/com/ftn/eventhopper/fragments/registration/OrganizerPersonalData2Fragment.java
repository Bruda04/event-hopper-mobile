package com.ftn.eventhopper.fragments.registration;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.ftn.eventhopper.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

/**
 * A simple {@link Fragment} subclass.
 * Use the  factory method to
 * create an instance of this fragment.
 */
public class OrganizerPersonalData2Fragment extends Fragment {
    private NavController navController;
    private TextInputEditText phoneField, cityField, addressField;
    private TextInputLayout phoneLayout, cityLayout, addressLayout;
    private String phone, city, address;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_organizer_personal_data2, container, false);
        navController = NavHostFragment.findNavController(this);

        retrieveFields(view);

        view.findViewById(R.id.register_btn).setOnClickListener(v -> {
            retrieveData();
            if(!validateFields()){
                navController.navigate(R.id.action_to_confirm_email);
            }
        });


        return view;
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
            cityLayout.setBoxStrokeColor(getResources().getColor(R.color.red)); // Highlight in red
            hasError = true;
        } else {
            cityLayout.setError(null); // Clear error
            cityLayout.setBoxStrokeColor(getResources().getColor(R.color.white)); // Reset border color
        }

        // Check password
        if (phone.isEmpty()) {
            phoneLayout.setError("Phone number is required. ");
            phoneLayout.setBoxStrokeColor(getResources().getColor(R.color.red));
            hasError = true;
        } else {
            phoneLayout.setError(null);
            phoneLayout.setBoxStrokeColor(getResources().getColor(R.color.white)); // Reset border color
        }

        if (address.isEmpty()) {
            addressLayout.setError("Address is required"); // Show error message
            addressLayout.setBoxStrokeColor(getResources().getColor(R.color.red)); // Highlight in red
            hasError = true;
        } else {
            cityLayout.setError(null); // Clear error
            cityLayout.setBoxStrokeColor(getResources().getColor(R.color.white)); // Reset border color
        }

        return hasError;
    }
}
