package com.ftn.eventhopper.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.ftn.eventhopper.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity {

    private String email, password;
    private TextInputLayout emailLayout, passwordLayout;
    private TextInputEditText emailField, passwordField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        // Set padding for system insets
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Button loginButton = findViewById(R.id.login_button); // Assuming the button has this ID

        loginButton.setOnClickListener(v -> {
            retrieveData(v);
            if (!validateFields()) {
                Log.d("Login", "Email " + email);
                Log.d("Login", "Password " + password);

                Intent intent = new Intent(LoginActivity.this, HostActivity.class);
                startActivity(intent);
            }
        });
    }


    private void retrieveData(View v){

        emailLayout = findViewById(R.id.login_email_layout); // Corrected ID
        passwordLayout = findViewById(R.id.login_password_layout); // Corrected ID

        emailField = (TextInputEditText) emailLayout.getEditText();
        passwordField = (TextInputEditText) passwordLayout.getEditText();

        email = emailField != null ? emailField.getText().toString().trim() : "";
        password = passwordField != null ? passwordField.getText().toString().trim() : "";
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

        return hasError;
    }

    private boolean isValidPassword(String password) {
        // Regular expression pattern to check for at least one uppercase letter, one lowercase letter, and one number
        String regex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).+$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }

    public void onRegisterClick(View view) {
        Log.d("Login", "Register link clicked");

        Intent intent = new Intent(LoginActivity.this, RegistrationActivity.class);
        startActivity(intent);
    }

    public void backToHomepage(View view){
        Log.d("Login", "Continue without account clicked");
        Intent intent = new Intent(LoginActivity.this, HostActivity.class);
        startActivity(intent);
    }


    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

}
