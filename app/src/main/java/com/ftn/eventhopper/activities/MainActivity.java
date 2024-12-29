package com.ftn.eventhopper.activities;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.ftn.eventhopper.R;
import com.ftn.eventhopper.clients.services.auth.UserService;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        UserService.initialize(getApplicationContext());

        UserService.clearJwtToken();
        UserService.setJwtToken("eyJhbGciOiJIUzUxMiJ9.eyJpc3MiOiJldmVudC1ob3BwZXIiLCJzdWIiOiJwdXAzQGV4YW1wbGUuY29tIiwiYXVkIjoid2ViIiwiaWF0IjoxNzM1NDM0NTQ5LCJleHAiOjE3MzU0NDUzNDksInJvbGUiOiJTRVJWSUNFX1BST1ZJREVSIiwiaWQiOiIzMTU0OGQ2Yi0wMTlmLTQ5MmUtYmEzOS0wN2JlN2ExNDMzZTUifQ.AHy6937lk3fje1zzlB3G4EMrHHvKTfRaF-fOsVro_gRYP-2ib3KXjJ7dK0VOcEBbKuvkAiUIcJL4wIPf6wHjMw");

    Log.d("MainActivity", "setContentView called");

        // Correctly referencing the FragmentContainerView ID
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.nav_main_fragment);

        NavController navController = navHostFragment.getNavController();

    }
}
