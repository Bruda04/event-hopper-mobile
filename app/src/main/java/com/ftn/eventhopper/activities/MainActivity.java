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
//        UserService.clearJwtToken();
//        UserService.setJwtToken("eyJhbGciOiJIUzUxMiJ9.eyJpc3MiOiJldmVudC1ob3BwZXIiLCJzdWIiOiJhZG1pbkBleGFtcGxlLmNvbSIsImF1ZCI6IndlYiIsImlhdCI6MTczNTA2MDA3MywiZXhwIjoxNzM1MDcwODczLCJyb2xlIjoiQURNSU4iLCJpZCI6ImU1MjdiMmRhLTY2YWUtNDA3Ni04MzAxLWQzYTlkYzNjNTg2MCJ9.ahxhGsttzl8LzP4UjYqhUPeqQOPik4TBtryJz1wntYd9SLvoT2AqdISzrLxT2-FWG5qVwhshuZ9nVg7WJbc8fA");

        Log.d("MainActivity", "setContentView called");

        // Correctly referencing the FragmentContainerView ID
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.nav_main_fragment);

        NavController navController = navHostFragment.getNavController();

    }
}
