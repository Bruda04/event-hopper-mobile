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

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Set padding for system insets
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Access the TextInputLayout and the TextInputEditText directly for both fields
        TextInputLayout emailLayout = findViewById(R.id.login_email_field);
        TextInputLayout passwordLayout = findViewById(R.id.login_password_field);

        TextInputEditText emailField = (TextInputEditText) emailLayout.getEditText();
        TextInputEditText passwordField = (TextInputEditText) passwordLayout.getEditText();

        Button loginButton = findViewById(R.id.login_button); // Assuming the button has this ID

        // Set an OnClickListener for the login button
        loginButton.setOnClickListener(v -> {
            // Capture input values
            String email = emailField != null ? emailField.getText().toString() : "";
            String password = passwordField != null ? passwordField.getText().toString() : "";

            // Log the input values
            Log.d("Logged in user:", "Email: " + email);
            Log.d("Logged in user:", "Password: " + password);

            // Redirect to the new activity (Example: HomeActivity)
            Intent intent = new Intent(MainActivity.this, HomeActivity.class);
            startActivity(intent);
        });
    }


    public void onRegisterClick(View view) {
        Log.d("MainActivity", "Register link clicked");

        Intent intent = new Intent(MainActivity.this, RegistrationActivity.class);
        startActivity(intent);
    }
}
