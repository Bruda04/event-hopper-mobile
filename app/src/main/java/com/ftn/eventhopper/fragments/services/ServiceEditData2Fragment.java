package com.ftn.eventhopper.fragments.services;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

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
        saveButton.setOnClickListener(v -> goToPupsServicesActivity());
        backButton.setOnClickListener(v -> goBackToData1Fragment());

        return view;
    }

    private void goToPupsServicesActivity() {
        PupsServicesFragment pupsServicesFragment = new PupsServicesFragment();

        // Begin the fragment transaction
        FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();

        // Optionally, add this transaction to the back stack so that the user can navigate back to the previous fragment
        transaction.replace(R.id.nav_host_fragment, pupsServicesFragment);  // Replace with your container ID
        transaction.addToBackStack(null);  // Adds the transaction to the back stack

        // Commit the transaction
        transaction.commit();
    }

    private void goBackToData1Fragment() {
        // Navigate back to ServiceEditData1Fragment
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.nav_host_fragment, new ServiceEditData1Fragment());
        transaction.addToBackStack(null);
        transaction.commit();
    }
}