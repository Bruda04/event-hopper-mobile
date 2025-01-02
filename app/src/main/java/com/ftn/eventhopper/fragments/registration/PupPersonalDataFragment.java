package com.ftn.eventhopper.fragments.registration;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ftn.eventhopper.R;
import com.ftn.eventhopper.fragments.login.viewmodels.LoginViewModel;
import com.ftn.eventhopper.fragments.registration.viewmodels.PupRegistrationViewModel;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;


public class PupPersonalDataFragment extends Fragment {

    private NavController navController;
    private PupRegistrationViewModel viewModel;
    private TextInputEditText nameField, surnameField, phoneField, cityField, addressField;
    private TextInputLayout nameLayout, surnameLayout, phoneLayout, cityLayout, addressLayout;
    private String name, surname, phone, city, address;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pup_personal_data, container, false);
        navController = NavHostFragment.findNavController(this);
        viewModel = new ViewModelProvider(this).get(PupRegistrationViewModel.class);

        retrieveFields(view);

        Bundle receivedBundle = getArguments();

        view.findViewById(R.id.next_btn).setOnClickListener(v -> {
            retrieveData();
            Log.d("Pup personal data page", "Next button clicked.");
            if(!validateFields()){
                receivedBundle.putString("name", name);
                receivedBundle.putString("surname", surname);
                receivedBundle.putString("phone", phone);
                receivedBundle.putString("city", city);
                receivedBundle.putString("address", address);

                Log.d("HelloOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO" , receivedBundle.toString());

                viewModel.register(receivedBundle);
                navController.navigate(R.id.action_to_confirm_email);
            }
        });


        return view;
    }

    private void retrieveFields(View view){
        nameLayout = view.findViewById(R.id.register_name_layout);
        surnameLayout = view.findViewById(R.id.register_surname_layout);
        cityLayout = view.findViewById(R.id.register_city_layout);
        phoneLayout = view.findViewById(R.id.register_phone_number_layout);
        addressLayout = view.findViewById(R.id.register_address_layout);

        nameField = (TextInputEditText) nameLayout.getEditText();
        surnameField = (TextInputEditText) surnameLayout.getEditText();
        cityField = (TextInputEditText) cityLayout.getEditText();
        phoneField = (TextInputEditText) phoneLayout.getEditText();
        addressField = (TextInputEditText) addressLayout.getEditText();
    }


    private void retrieveData(){
        name = nameField != null ? nameField.getText().toString().trim() : "";
        surname = surnameField != null ? surnameField.getText().toString().trim() : "";
        phone = phoneField != null ? phoneField.getText().toString().trim() : "";
        city = cityField != null ? cityField.getText().toString().trim() : "";
        address = addressField != null ? addressField.getText().toString().trim() : "";
    }

    private boolean validateFields(){
        boolean hasError = false;

        if (name.isEmpty()) {
            nameLayout.setError("Name is required"); // Show error message
            nameLayout.setBoxStrokeColor(getResources().getColor(R.color.light_error)); // Highlight in red
            hasError = true;
        }else if(name.length() < 3){
            nameLayout.setError("Name is too short"); // Show error message
            nameLayout.setBoxStrokeColor(getResources().getColor(R.color.light_error)); // Highlight in red
            hasError = true;
        } else {
            nameLayout.setError(null); // Clear error
            nameLayout.setBoxStrokeColor(getResources().getColor(R.color.white)); // Reset border color
        }

        if (surname.isEmpty()) {
            surnameLayout.setError("Surname is required"); // Show error message
            surnameLayout.setBoxStrokeColor(getResources().getColor(R.color.light_error)); // Highlight in red
            hasError = true;
        } else {
            surnameLayout.setError(null); // Clear error
            surnameLayout.setBoxStrokeColor(getResources().getColor(R.color.white)); // Reset border color
        }

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