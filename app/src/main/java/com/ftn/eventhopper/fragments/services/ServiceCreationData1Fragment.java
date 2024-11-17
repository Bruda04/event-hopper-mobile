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

public class ServiceCreationData1Fragment extends Fragment {

    private EditText serviceNameInput, serviceDescriptionInput;


    private Button nextButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_service_creation_data1, container, false);

        // Find input fields and the next button
        serviceNameInput = view.findViewById(R.id.service_name);
        serviceDescriptionInput = view.findViewById(R.id.service_description);


        nextButton = view.findViewById(R.id.next_button);

        // Set click listener for the button
        nextButton.setOnClickListener(v -> {
            String serviceName = serviceNameInput.getText().toString();
            String serviceDescription = serviceDescriptionInput.getText().toString();



            // Pass data to the next fragment using Bundle
            Bundle bundle = new Bundle();
            bundle.putString("serviceName", serviceName);
            bundle.putString("serviceDescription", serviceDescription);


            NavController navController = NavHostFragment.findNavController(this);
            navController.navigate(R.id.action_to_create_service2);
        });

        return view;
    }

}
