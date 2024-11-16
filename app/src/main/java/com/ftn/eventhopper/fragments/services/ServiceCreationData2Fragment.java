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
import com.ftn.eventhopper.activities.PupsServicesActivity;
import com.google.android.material.textfield.TextInputEditText;

public class ServiceCreationData2Fragment extends Fragment {

    private Button createButton;
    private Button backButton;

    private TextInputEditText serviceReservationWindowInput, serviceDurationInput, serviceCancellationWindowInput;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_service_creation_data2, container, false);

        // Initialize buttons
        createButton = view.findViewById(R.id.next_button);
        backButton = view.findViewById(R.id.back_button);
        serviceReservationWindowInput = view.findViewById(R.id.reservation_window);
        serviceDurationInput = view.findViewById(R.id.duration);
        serviceCancellationWindowInput = view.findViewById(R.id.cancellation_window);

        // Set up button actions
        createButton.setOnClickListener(v -> goToPupsServicesActivity());
        backButton.setOnClickListener(v -> goBackToData1Fragment());

        return view;
    }

    private void goToPupsServicesActivity() {
        // Navigate to PupsServicesActivity
        Intent intent = new Intent(getActivity(), PupsServicesActivity.class);
        startActivity(intent);
        getActivity().finish();
    }

    private void goBackToData1Fragment() {
        // Navigate back to ServiceCreationData1Fragment
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, new ServiceCreationData1Fragment());
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
