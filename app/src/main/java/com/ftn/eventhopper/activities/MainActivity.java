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
        UserService.setJwtToken("eyJhbGciOiJIUzUxMiJ9.eyJpc3MiOiJldmVudC1ob3BwZXIiLCJzdWIiOiJhZG1pbkBleGFtcGxlLmNvbSIsImF1ZCI6IndlYiIsImlhdCI6MTczNTA0NTc5OSwiZXhwIjoxNzM1MDU2NTk5LCJyb2xlIjoiQURNSU4iLCJpZCI6ImU1MjdiMmRhLTY2YWUtNDA3Ni04MzAxLWQzYTlkYzNjNTg2MCJ9.d1j9RNhXPPP0hpsEe-E1aVGOseaOyNFndVuxXTyApFi5xu8GtT57OWmlCHpLMoEHS8DuZua2dl4bryET5PoTCw");

        Log.d("MainActivity", "setContentView called");

        // Correctly referencing the FragmentContainerView ID
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.nav_main_fragment);

        NavController navController = navHostFragment.getNavController();

    }
}
