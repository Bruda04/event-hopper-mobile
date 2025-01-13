package com.ftn.eventhopper.activities;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavGraph;
import androidx.navigation.fragment.NavHostFragment;

import com.ftn.eventhopper.R;
import com.ftn.eventhopper.clients.services.auth.UserService;
import com.ftn.eventhopper.fragments.HostFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        UserService.initialize(getApplicationContext());

        Log.d("MainActivity", "setContentView called");
        // Correctly referencing the FragmentContainerView ID

        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
            .findFragmentById(R.id.nav_main_fragment);

        NavController navController = navHostFragment.getNavController();
        Fragment fragment;
        if(UserService.isTokenValid()){
            fragment = new HostFragment();
        }else{
            fragment = NavHostFragment.create(R.navigation.nav_auth);
        }

        Log.d("TOKEN HERE", String.valueOf(UserService.isTokenValid()));
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.nav_main_fragment, fragment)
                .commit();
    }
}
