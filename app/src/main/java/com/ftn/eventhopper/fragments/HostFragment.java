package com.ftn.eventhopper.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;
import com.ftn.eventhopper.R;
import com.ftn.eventhopper.clients.services.auth.UserService;
import com.ftn.eventhopper.shared.models.users.PersonType;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HostFragment extends Fragment {

    private BottomNavigationView bottomNavigationView;
    private NavController navController;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for the fragment
        return inflater.inflate(R.layout.fragment_host, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Set up edge-to-edge layout to adjust for system bars
        ViewCompat.setOnApplyWindowInsetsListener(view.findViewById(R.id.host), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0);
            return insets;
        });

        // Set up BottomNavigationView
        bottomNavigationView = view.findViewById(R.id.bottomNavigationView);

        // Get NavController from the NavHostFragment
        NavHostFragment navHostFragment = (NavHostFragment) getChildFragmentManager()
                .findFragmentById(R.id.nav_host_fragment);
        if (navHostFragment != null) {
            navController = navHostFragment.getNavController();

            // Set up the BottomNavigationView with the NavController
            NavigationUI.setupWithNavController(bottomNavigationView, navController);

            // Make sure the navigation component starts at the correct destination
            boolean isLoggedIn = UserService.isTokenValid(); // Assuming this method exists in UserService
            bottomNavigationView.getMenu().findItem(R.id.calendar).setVisible(isLoggedIn);
            bottomNavigationView.getMenu().findItem(R.id.company).setVisible(isLoggedIn && UserService.getUserRole() == PersonType.SERVICE_PROVIDER);
            bottomNavigationView.getMenu().findItem(R.id.favorites).setVisible(isLoggedIn);
            bottomNavigationView.setSelectedItemId(R.id.home);

            bottomNavigationView.setOnItemSelectedListener(item -> {
                if (item.getItemId() == R.id.home) {
                    navController.popBackStack(R.id.home, true);
                    navController.navigate(R.id.home);
                    return true;
                } else if (item.getItemId() == R.id.calendar) {
                    navController.popBackStack(R.id.calendar, true);
                    navController.navigate(R.id.calendar);
                    return true;
                } else if (item.getItemId() == R.id.profile) {
                    if(isLoggedIn){
                        navController.popBackStack(R.id.profile, true); // Clear previous back stack if Profile exists
                        navController.navigate(R.id.profile);
                    }else{
                        navController.navigate(R.id.notLoggedIn);
                    }
                    return true;
                } else if (item.getItemId() == R.id.favorites) {
                    navController.popBackStack(R.id.favorites, true); // Clear previous back stack if Profile exists
                    navController.navigate(R.id.favorites);
                    return true;
                }else if (item.getItemId() == R.id.company) {
                    navController.popBackStack(R.id.company, true); // Clear previous back stack if Profile exists
                    navController.navigate(R.id.company);
                    return true;
                }
                return false;
            });
        }

        Button notificationButton = view.findViewById(R.id.notificationIcon);
        notificationButton.setOnClickListener(v -> {
            NavDestination dest = navController.getCurrentDestination();

            if (dest != null && dest.getId() == R.id.notificationsFragment) {
                navController.popBackStack(R.id.notificationsFragment, true);
                notificationButton.setBackgroundColor(getResources().getColor(R.color.theme_blue));
            } else {
                navController.navigate(R.id.notificationsFragment);
                notificationButton.setBackgroundColor(getResources().getColor(R.color.darker_blue));
            }
        });

        // Observe navigation changes to update the button's background dynamically
        navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
            if (destination.getId() == R.id.notificationsFragment) {
                notificationButton.setBackgroundColor(getResources().getColor(R.color.darker_blue));
            } else {
                notificationButton.setBackgroundColor(getResources().getColor(R.color.theme_blue));
            }
        });
    }
}