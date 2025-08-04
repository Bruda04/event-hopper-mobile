package com.ftn.eventhopper.fragments.login;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.ftn.eventhopper.R;
import com.ftn.eventhopper.fragments.login.viewmodels.LoginViewModel;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.textview.MaterialTextView;

import java.util.UUID;

public class LoginFragment extends Fragment {
    private LoginViewModel viewModel;
    private String email, password;
    private TextInputLayout emailLayout, passwordLayout;
    private NavController navController;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        navController = NavHostFragment.findNavController(this);
        viewModel = new ViewModelProvider(this).get(LoginViewModel.class);

        emailLayout = view.findViewById(R.id.login_email_layout);
        passwordLayout = view.findViewById(R.id.login_password_layout);

        Bundle attendingEventBundle = getArguments();

        viewModel.getErrorMessage().observe(getViewLifecycleOwner(), errorMessage -> {
            if (errorMessage.isEmpty()) {
                Log.d("Navigation", "Navigating to home");
                navController.navigate(R.id.action_login_to_host);
                if(attendingEventBundle != null && attendingEventBundle.containsKey("attending-event")){
                    viewModel.addAttendingEventOnLogin(UUID.fromString(attendingEventBundle.getString("attending-event")));
                }
            } else {
                MaterialTextView errorText = view.findViewById(R.id.error_text);
                errorText.setText(errorMessage);
            }
        });

        Button loginButton = view.findViewById(R.id.login_button);
        loginButton.setOnClickListener(v -> {
            retrieveData();
            if (!validateFields()) {
                viewModel.login(this.email, this.password);
            }
        });

        view.findViewById(R.id.register_link).setOnClickListener(v -> {
            navController.navigate(R.id.action_login_to_register);
        });


        view.findViewById(R.id.continue_without).setOnClickListener(v -> {
            navController.navigate(R.id.action_login_to_host);
        });

        return view;
    }

    private void retrieveData() {
        TextInputEditText emailField = (TextInputEditText) emailLayout.getEditText();
        TextInputEditText passwordField = (TextInputEditText) passwordLayout.getEditText();

        email = emailField != null ? emailField.getText().toString().trim() : "";
        password = passwordField != null ? passwordField.getText().toString().trim() : "";
    }

    private boolean validateFields() {
        boolean hasError = false;

        if (email.isEmpty()) {
            emailLayout.setError("Email is required");
            hasError = true;
        } else {
            emailLayout.setError(null);
        }

        if (password.isEmpty() || !isValidPassword(password)) {
            passwordLayout.setError("Password is invalid.");
            hasError = true;
        } else {
            passwordLayout.setError(null);
        }

        return hasError;
    }

    private boolean isValidPassword(String password) {
        String regex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).+$";
        return password.matches(regex);
    }
}
