package com.ftn.eventhopper.fragments.services;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.ftn.eventhopper.R;
import com.google.android.material.textfield.TextInputEditText;

public class ServiceEditData1Fragment extends Fragment {

    private EditText serviceNameInput, serviceDescriptionInput;
    private TextInputEditText serviceReservationWindowInput, serviceDurationInput, serviceCancellationWindowInput;

    private NavController navController;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_service_edit_data1, container, false);
        navController = NavHostFragment.findNavController(this);

        // Find input fields and the next button
        serviceNameInput = view.findViewById(R.id.service_name);
        serviceDescriptionInput = view.findViewById(R.id.service_description);
        serviceReservationWindowInput = view.findViewById(R.id.reservation_window);
        serviceDurationInput = view.findViewById(R.id.duration);


        // Set click listener for the button
        view.findViewById(R.id.next_button).setOnClickListener(v -> {
            navController.navigate(R.id.action_to_edit_service2);
        });

        return view;
    }


}