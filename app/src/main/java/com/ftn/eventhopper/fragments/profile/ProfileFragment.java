package com.ftn.eventhopper.fragments.profile;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ftn.eventhopper.R;
import com.ftn.eventhopper.activities.MainActivity;
import com.ftn.eventhopper.fragments.login.viewmodels.LoginViewModel;
import com.ftn.eventhopper.fragments.profile.viewmodels.ProfileViewModel;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {

    private ProfileViewModel viewModel;

    public ProfileFragment() {
        // Required empty public constructor
    }

    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        viewModel = new ViewModelProvider(this).get(ProfileViewModel.class);


        NavController navController = NavHostFragment.findNavController(this);

        // Set up the click listener for the "View my services" button
        view.findViewById(R.id.ListItemMyServices).setOnClickListener(v -> {
            // Navigate using the action defined in the NavGraph
            navController.navigate(R.id.action_to_pup_services);
        });

        view.findViewById(R.id.ListItemCategories).setOnClickListener(v -> {
            // Navigate using the action defined in the NavGraph
            navController.navigate(R.id.action_to_manage_categories);
        });

        view.findViewById(R.id.ListItemMyPrices).setOnClickListener(v -> {
            // Navigate using the action defined in the NavGraph
            navController.navigate(R.id.action_to_manage_prices);
        });

        view.findViewById(R.id.ListItemLogOut).setOnClickListener(v -> {
            viewModel.logout();
            ((MainActivity) requireActivity()).navigateToAuthGraph();
        });
        return view;
    }
}