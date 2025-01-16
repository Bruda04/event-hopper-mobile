package com.ftn.eventhopper.fragments.upgrades;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ftn.eventhopper.R;
import com.ftn.eventhopper.fragments.upgrades.viewmodels.UpgradeViewModel;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class CompanyData1Fragment extends Fragment {

    private NavController navController;
    private TextInputEditText companyEmailField, companyNameField, companyPhoneNumberField;
    private TextInputLayout companyEmailLayout, companyNameLayout, companyPhoneNumberLayout;
    private String companyEmail, companyName, companyPhoneNumber;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_company_data, container, false);
        navController = NavHostFragment.findNavController(this);

        retrieveFields(view);

        view.findViewById(R.id.next_btn).setOnClickListener(v -> {
            retrieveData();
            if(!validateFields()){
                Bundle bundle = new Bundle();
                bundle.putString("companyName", companyName);
                bundle.putString("companyEmail", companyEmail);
                bundle.putString("companyPhoneNumber", companyPhoneNumber);
                navController.navigate(R.id.action_to_company_data2, bundle);
            }
        });

        return view;
    }

    private boolean validateFields(){
        boolean hasError = false;

        if (companyEmail.isEmpty()) {
            companyEmailLayout.setError("Email is required"); // Show error message
            companyEmailLayout.setBoxStrokeColor(getResources().getColor(R.color.light_error)); // Highlight in red
            hasError = true;
        }
        else if(!android.util.Patterns.EMAIL_ADDRESS.matcher(companyEmail).matches()){
            companyEmailLayout.setError("Email is in wrong format");
            companyEmailLayout.setBoxStrokeColor(getResources().getColor(R.color.light_error)); // Highlight in red
            hasError = true;
        } else {
            companyEmailLayout.setError(null); // Clear error
            companyEmailLayout.setBoxStrokeColor(getResources().getColor(R.color.white)); // Reset border color
        }


        if (companyName.isEmpty()) {
            companyNameLayout.setError("Name is required"); // Show error message
            companyNameLayout.setBoxStrokeColor(getResources().getColor(R.color.light_error)); // Highlight in red
            hasError = true;
        }else if(companyName.length() < 2){
            companyNameLayout.setError("Company name too short"); // Show error message
            companyNameLayout.setBoxStrokeColor(getResources().getColor(R.color.light_error)); // Highlight in red
            hasError = true;
        } else {
            companyNameLayout.setError(null); // Clear error
            companyNameLayout.setBoxStrokeColor(getResources().getColor(R.color.white)); // Reset border color
        }

        if (companyPhoneNumber.isEmpty()) {
            companyPhoneNumberLayout.setError("Phone number is required"); // Show error message
            companyPhoneNumberLayout.setBoxStrokeColor(getResources().getColor(R.color.light_error));
            hasError = true;
        }else if( companyPhoneNumber.length() < 8 ) {
            companyPhoneNumberLayout.setError("Phone number must be >8 digits");
            companyPhoneNumberLayout.setBoxStrokeColor(getResources().getColor(R.color.light_error));
            hasError = true;
        } else {
            companyPhoneNumberLayout.setError(null); // Clear error
            companyPhoneNumberLayout.setBoxStrokeColor(getResources().getColor(R.color.white)); // Reset border color
        }

        return hasError;
    }


    private void retrieveFields(View view){
        companyEmailLayout = view.findViewById(R.id.register_email_layout);
        companyNameLayout = view.findViewById(R.id.register_name_layout);
        companyPhoneNumberLayout = view.findViewById(R.id.register_phone_number_layout);


        companyEmailField = (TextInputEditText) companyEmailLayout.getEditText();
        companyNameField = (TextInputEditText) companyNameLayout.getEditText();
        companyPhoneNumberField = (TextInputEditText) companyPhoneNumberLayout.getEditText();

    }

    private void retrieveData(){
        companyEmail = companyEmailField != null ? companyEmailField.getText().toString().trim() : "";
        companyName = companyNameField != null ? companyNameField.getText().toString().trim() : "";
        companyPhoneNumber = companyPhoneNumberField != null ? companyPhoneNumberField.getText().toString().trim() : "";
    }

}