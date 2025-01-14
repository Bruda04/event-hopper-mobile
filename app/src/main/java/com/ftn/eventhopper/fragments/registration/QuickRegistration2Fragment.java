package com.ftn.eventhopper.fragments.registration;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ftn.eventhopper.R;
import com.ftn.eventhopper.fragments.registration.viewmodels.QuickRegistrationViewModel;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;


public class QuickRegistration2Fragment extends Fragment {

    private QuickRegistrationViewModel viewModel;
    private NavController navController;
    private TextInputEditText phoneField, cityField, addressField;
    private TextInputLayout phoneLayout, cityLayout, addressLayout;
    private String phone, city, address;

    public QuickRegistration2Fragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_quick_registration2, container, false);

        navController = NavHostFragment.findNavController(this);
        viewModel = new ViewModelProvider(this).get(QuickRegistrationViewModel.class);

        retrieveFields(view);

        view.findViewById(R.id.register_user_btn).setOnClickListener(v -> {
            retrieveData();
            if(!validateFields()){
                Bundle receivedBundle = getArguments();
                receivedBundle.putString("phone", phone);
                receivedBundle.putString("city", city);
                receivedBundle.putString("address", address);

                String eventID = receivedBundle.getString("attending-event");
                Bundle eventBundle = new Bundle();
                eventBundle.putString("attending-event", eventID);

                viewModel.register(receivedBundle);
                navController.navigate(R.id.action_to_login,eventBundle);
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