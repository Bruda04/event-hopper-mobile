
package com.ftn.eventhopper.fragments.registration;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

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
public class PupPersonalData1Fragment extends Fragment {

    private NavController navController;
    private TextInputEditText emailField, nameField, passwordField, passwordAgainField, phoneNumberField;
    private TextInputLayout emailLayout, nameLayout, passwordLayout, passwordAgainLayout, phoneNumberLayout;
    private String email, name, password, passwordAgain, phoneNumber;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pup_personal_data1, container, false);
        navController = NavHostFragment.findNavController(this);

        retrieveFields(view);

        view.findViewById(R.id.next_btn).setOnClickListener(v -> {
            retrieveData();
            if(!validateFields()){
                navController.navigate(R.id.action_to_pup_data_2);
            }
        });

        return view;
    }

    private boolean validateFields(){
        boolean hasError = false;

        if (email.isEmpty()) {
            emailLayout.setError("Email is required"); // Show error message
            emailLayout.setBoxStrokeColor(getResources().getColor(R.color.red)); // Highlight in red
            hasError = true;
        } else {
            emailLayout.setError(null); // Clear error
            emailLayout.setBoxStrokeColor(getResources().getColor(R.color.white)); // Reset border color
        }

        // Check password
        if (password.isEmpty()) {
            passwordLayout.setError("Password is required. " + getString(R.string.password_rule));
            passwordLayout.setBoxStrokeColor(getResources().getColor(R.color.red));
            hasError = true;
        }else if(!isValidPassword(password)){
            passwordLayout.setError(getString(R.string.password_rule));
            passwordLayout.setBoxStrokeColor(getResources().getColor(R.color.red));
            hasError = true;
        }
        else {
            passwordLayout.setError(null);
            passwordLayout.setBoxStrokeColor(getResources().getColor(R.color.white)); // Reset border color
        }

        if (name.isEmpty()) {
            nameLayout.setError("Name is required"); // Show error message
            nameLayout.setBoxStrokeColor(getResources().getColor(R.color.red)); // Highlight in red
            hasError = true;
        } else {
            nameLayout.setError(null); // Clear error
            nameLayout.setBoxStrokeColor(getResources().getColor(R.color.white)); // Reset border color
        }

        if (phoneNumber.isEmpty()) {
            phoneNumberLayout.setError("Phone number is required"); // Show error message
            phoneNumberLayout.setBoxStrokeColor(getResources().getColor(R.color.red)); // Highlight in red
            hasError = true;
        } else {
            phoneNumberLayout.setError(null); // Clear error
            phoneNumberLayout.setBoxStrokeColor(getResources().getColor(R.color.white)); // Reset border color
        }

        if (passwordAgain.isEmpty()) {
            passwordAgainLayout.setError("Password is required"); // Show error message
            passwordAgainLayout.setBoxStrokeColor(getResources().getColor(R.color.red)); // Highlight in red
            hasError = true;
        }else if(!passwordAgain.equals(password)) {
            passwordAgainLayout.setError("Password is doesn't match"); // Show error message
            passwordAgainLayout.setBoxStrokeColor(getResources().getColor(R.color.red)); // Highlight in red
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
        emailLayout = view.findViewById(R.id.register_email_layout);
        nameLayout = view.findViewById(R.id.register_name_layout);
        passwordLayout = view.findViewById(R.id.register_password_layout);
        passwordAgainLayout = view.findViewById(R.id.register_password_again_layout);
        phoneNumberLayout = view.findViewById(R.id.register_phone_number_layout);

        emailField = (TextInputEditText) emailLayout.getEditText();
        nameField = (TextInputEditText) nameLayout.getEditText();
        phoneNumberField = (TextInputEditText) phoneNumberLayout.getEditText();
        passwordField = (TextInputEditText) passwordLayout.getEditText();
        passwordAgainField = (TextInputEditText) passwordAgainLayout.getEditText();
    }

    private void retrieveData(){
        email = emailField != null ? emailField.getText().toString().trim() : "";
        name = nameField != null ? nameField.getText().toString().trim() : "";
        phoneNumber = phoneNumberField != null ? phoneNumberField.getText().toString().trim() : "";
        passwordAgain = passwordAgainField != null ? passwordAgainField.getText().toString().trim() : "";
        password = passwordField != null ? passwordField.getText().toString().trim() : "";
    }

}
