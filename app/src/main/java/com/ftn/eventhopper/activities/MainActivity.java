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
        UserService.setJwtToken("eyJhbGciOiJIUzUxMiJ9.eyJpc3MiOiJldmVudC1ob3BwZXIiLCJzdWIiOiJwdXAzQGV4YW1wbGUuY29tIiwiYXVkIjoid2ViIiwiaWF0IjoxNzM1MTU5MjAyLCJleHAiOjE3MzUxNzAwMDIsInJvbGUiOiJTRVJWSUNFX1BST1ZJREVSIiwiaWQiOiIzMTU0OGQ2Yi0wMTlmLTQ5MmUtYmEzOS0wN2JlN2ExNDMzZTUifQ.YJXUlbSJZT5NA6LhU-d9B3RJuoHKHnoL69krI-ulhKrn4FRm8k6LUENgDv-Y7dYmxNC_frKDEtciXBAY3lgXRA");

        Log.d("MainActivity", "setContentView called");

        // Correctly referencing the FragmentContainerView ID
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.nav_main_fragment);

        NavController navController = navHostFragment.getNavController();

    }
}
