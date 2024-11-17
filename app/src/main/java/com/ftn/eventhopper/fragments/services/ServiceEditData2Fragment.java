package com.ftn.eventhopper.fragments.services;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.ftn.eventhopper.R;

public class ServiceEditData2Fragment extends Fragment {

    private Button saveButton;
    private Button backButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_service_edit_data2, container, false);

        // Initialize buttons
        saveButton = view.findViewById(R.id.next_button);
        backButton = view.findViewById(R.id.back_button);

        // Set up button actions
        saveButton.setOnClickListener(v -> {
            NavController navController = NavHostFragment.findNavController(this);
            navController.navigate(R.id.pup_services); // Navigate to PupsServicesFragment

        });
        backButton.setOnClickListener(v -> {
            NavController navController = NavHostFragment.findNavController(this);
            navController.navigate(R.id.service_edit); // Navigate to PupsServicesFragment


        });

        return view;
    }


}