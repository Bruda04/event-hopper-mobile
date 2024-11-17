package com.ftn.eventhopper.activities;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.ftn.eventhopper.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HostActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_host);

        // Set up edge-to-edge layout to adjust for system bars
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0);
            return insets;
        });

        // Set up BottomNavigationView
        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        // Get NavController from the NavHostFragment
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment);
        navController = navHostFragment.getNavController();

        // Set up the BottomNavigationView with the NavController
        NavigationUI.setupWithNavController(bottomNavigationView, navController);

        // Make sure the navigation component starts at the correct destination
        bottomNavigationView.setSelectedItemId(R.id.home);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            if(item.getItemId() == R.id.home){
                navController.navigate(R.id.home);
                navController.navigate(R.id.home);
                return true;
            }else if (item.getItemId() == R.id.calendar){
                navController.popBackStack(R.id.calendar, true);
                navController.navigate(R.id.calendar);
                return true;
            }else if (item.getItemId() == R.id.profile){
                navController.popBackStack(R.id.profile, true); // Clear previous back stack if Profile exists
                navController.navigate(R.id.profile); // Navigate to Profile
                return true;
            }
            return false;
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        // Handle up navigation (when the back button is pressed in the toolbar)
        return navController.navigateUp() || super.onSupportNavigateUp();
    }
}
