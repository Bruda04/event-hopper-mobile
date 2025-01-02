
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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A simple {@link Fragment} subclass.
 * Use the  factory method to
 * create an instance of this fragment.
 */
public class PupCompanyData1Fragment extends Fragment {

    private NavController navController;
    private TextInputEditText companyEmailField, companyNameField, passwordField, passwordAgainField, companyPhoneNumberField;
    private TextInputLayout companyEmailLayout, companyNameLayout, passwordLayout, passwordAgainLayout, companyPhoneNumberLayout;
    private String companyEmail, companyName, password, passwordAgain, companyPhoneNumber;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pup_company_data1, container, false);
        navController = NavHostFragment.findNavController(this);

        retrieveFields(view);

        view.findViewById(R.id.next_btn).setOnClickListener(v -> {
            retrieveData();
            if(!validateFields()){
                Bundle bundle = new Bundle();
                bundle.putString("companyName", companyName);
                bundle.putString("companyEmail", companyEmail);
                bundle.putString("password", password);
                bundle.putString("companyPhoneNumber", companyPhoneNumber);
                navController.navigate(R.id.action_to_pup_company_2, bundle);
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
        } else {
            companyEmailLayout.setError(null); // Clear error
            companyEmailLayout.setBoxStrokeColor(getResources().getColor(R.color.white)); // Reset border color
        }

        // Check password
        if (password.isEmpty()) {
            passwordLayout.setError("Password is required. " + getString(R.string.password_rule));
            passwordLayout.setBoxStrokeColor(getResources().getColor(R.color.light_error));
            hasError = true;
        }else if(!isValidPassword(password)){
            passwordLayout.setError(getString(R.string.password_rule));
            passwordLayout.setBoxStrokeColor(getResources().getColor(R.color.light_error));
            hasError = true;
        }
        else {
            passwordLayout.setError(null);
            passwordLayout.setBoxStrokeColor(getResources().getColor(R.color.white)); // Reset border color
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
        } else {
            companyPhoneNumberLayout.setError(null); // Clear error
            companyPhoneNumberLayout.setBoxStrokeColor(getResources().getColor(R.color.white)); // Reset border color
        }
        if (passwordAgain.isEmpty()) {
            passwordAgainLayout.setError("Password is required"); // Show error message
            passwordAgainLayout.setBoxStrokeColor(getResources().getColor(R.color.light_error));
            hasError = true;
        }else if(!passwordAgain.equals(password)) {
            passwordAgainLayout.setError("Password is doesn't match"); // Show error message
            passwordAgainLayout.setBoxStrokeColor(getResources().getColor(R.color.light_error));
            hasError = true;
        }
        else {
            passwordAgainLayout.setError(null); // Clear error
            passwordAgainLayout.setBoxStrokeColor(getResources().getColor(R.color.white)); // Reset border color
        }

        return hasError;
    }

    private boolean isValidPassword(String password) {
        // Regular expression pattern to check for at least one uppercase letter, one lowercase letter, and one number
        String regex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).+$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }

    private void retrieveFields(View view){
        companyEmailLayout = view.findViewById(R.id.register_email_layout);
        companyNameLayout = view.findViewById(R.id.register_name_layout);
        passwordLayout = view.findViewById(R.id.register_password_layout);
        passwordAgainLayout = view.findViewById(R.id.register_password_again_layout);
        companyPhoneNumberLayout = view.findViewById(R.id.register_phone_number_layout);

        companyEmailField = (TextInputEditText) companyEmailLayout.getEditText();
        companyNameField = (TextInputEditText) companyNameLayout.getEditText();
        companyPhoneNumberField = (TextInputEditText) companyPhoneNumberLayout.getEditText();
        passwordField = (TextInputEditText) passwordLayout.getEditText();
        passwordAgainField = (TextInputEditText) passwordAgainLayout.getEditText();
    }

    private void retrieveData(){
        companyEmail = companyEmailField != null ? companyEmailField.getText().toString().trim() : "";
        companyName = companyNameField != null ? companyNameField.getText().toString().trim() : "";
        companyPhoneNumber = companyPhoneNumberField != null ? companyPhoneNumberField.getText().toString().trim() : "";
        passwordAgain = passwordAgainField != null ? passwordAgainField.getText().toString().trim() : "";
        password = passwordField != null ? passwordField.getText().toString().trim() : "";
    }

}
