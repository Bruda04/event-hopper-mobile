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
import android.widget.Button;

import com.ftn.eventhopper.R;
import com.ftn.eventhopper.fragments.registration.viewmodels.OrganizerRegistrationViewModel;
import com.ftn.eventhopper.fragments.registration.viewmodels.QuickRegistrationViewModel;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class QuickRegistration1Fragment extends Fragment {

    private QuickRegistrationViewModel viewModel;
    private NavController navController;
    private TextInputEditText emailField, nameField, surnameField, passwordField, passwordAgainField;
    private TextInputLayout emailLayout, nameLayout, surnameLayout, passwordLayout, passwordAgainLayout;
    private String email, name, surname, password, passwordAgain;
    private Button nextButton;
    private String attendingEventId;

    public QuickRegistration1Fragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_quick_registration1, container, false);
        navController = NavHostFragment.findNavController(this);
        viewModel = new ViewModelProvider(this).get(QuickRegistrationViewModel.class);

        retrieveFields(view);

        Bundle args = getArguments();
        if(args != null){
            email = args.getString("email");
            emailField.setText(email);
            emailField.setEnabled(false);

            attendingEventId = args.getString("attending-event");
        }

        view.findViewById(R.id.next_btn).setOnClickListener(v -> {
            retrieveData();

            if(!validateFields()){
                viewModel.checkEmail(email, isTaken -> {
                    if (isTaken) {
                        emailLayout.setError("Email is taken.");
                        emailLayout.setBoxStrokeColor(getResources().getColor(R.color.light_error)); // Highlight in red
                    } else {
                        Bundle bundle = new Bundle();
                        bundle.putString("email", email);
                        bundle.putString("name", name);
                        bundle.putString("surname", surname);
                        bundle.putString("password", password);
                        bundle.putString("attending-event", attendingEventId);

                        navController.navigate(R.id.action_to_quick_registration_2, bundle);
                    }
                });
            }
        });

        return view;
    }


    private void retrieveFields(View view) {
        emailLayout = view.findViewById(R.id.register_email_layout);
        nameLayout = view.findViewById(R.id.register_name_layout);
        surnameLayout = view.findViewById(R.id.register_surname_layout);
        passwordLayout = view.findViewById(R.id.register_password_layout);
        passwordAgainLayout = view.findViewById(R.id.register_password_again_layout);

        emailField = (TextInputEditText) emailLayout.getEditText();
        nameField = (TextInputEditText) nameLayout.getEditText();
        surnameField = (TextInputEditText) surnameLayout.getEditText();
        passwordField = (TextInputEditText) passwordLayout.getEditText();
        passwordAgainField = (TextInputEditText) passwordAgainLayout.getEditText();
    }

    private void retrieveData(){
        email = emailField != null ? emailField.getText().toString().trim() : "";
        name = nameField != null ? nameField.getText().toString().trim() : "";
        surname = surnameField != null ? surnameField.getText().toString().trim() : "";
        passwordAgain = passwordAgainField != null ? passwordAgainField.getText().toString().trim() : "";
        password = passwordField != null ? passwordField.getText().toString().trim() : "";
    }

    private boolean validateFields(){
        boolean hasError = false;

        if (email.isEmpty()) {
            emailLayout.setError("Email is required"); // Show error message
            emailLayout.setBoxStrokeColor(getResources().getColor(R.color.light_error)); // Highlight in red
            hasError = true;
        }
        else if(!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            emailLayout.setError("Email is in wrong format");
            emailLayout.setBoxStrokeColor(getResources().getColor(R.color.light_error)); // Highlight in red
            hasError = true;
        }
        else {
            emailLayout.setError(null); // Clear error
            emailLayout.setBoxStrokeColor(getResources().getColor(R.color.white)); // Reset border color
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

        if (passwordAgain.isEmpty()) {
            passwordAgainLayout.setError("Password is required"); // Show error message
            passwordAgainLayout.setBoxStrokeColor(getResources().getColor(R.color.light_error)); // Highlight in red
            hasError = true;
        }else if(!passwordAgain.equals(password)) {
            passwordAgainLayout.setError("Password must match"); // Show error message
            passwordAgainLayout.setBoxStrokeColor(getResources().getColor(R.color.light_error)); // Highlight in red
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
}