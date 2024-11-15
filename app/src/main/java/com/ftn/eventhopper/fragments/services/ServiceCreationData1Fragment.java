package com.ftn.eventhopper.fragments.services;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.ftn.eventhopper.R;
import com.google.android.material.textfield.TextInputEditText;

public class ServiceCreationData1Fragment extends Fragment {

    private EditText serviceNameInput, serviceDescriptionInput;
    private TextInputEditText serviceReservationWindowInput, serviceDurationInput, serviceCancellationWindowInput;


    private Button nextButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_service_creation_data1, container, false);

        // Find input fields and the next button
        serviceNameInput = view.findViewById(R.id.service_name);
        serviceDescriptionInput = view.findViewById(R.id.service_description);
        serviceReservationWindowInput = view.findViewById(R.id.reservation_window);
        serviceDurationInput = view.findViewById(R.id.duration);
        serviceCancellationWindowInput = view.findViewById(R.id.cancellation_window);

        nextButton = view.findViewById(R.id.next_button);

        // Set click listener for the button
        nextButton.setOnClickListener(v -> goToServiceCreationData2());

        return view;
    }

    private void goToServiceCreationData2() {
        // Capture data from input fields
        String serviceName = serviceNameInput.getText().toString();
        String serviceDescription = serviceDescriptionInput.getText().toString();
        String serviceReservationWindow = serviceReservationWindowInput.getText().toString();
        String serviceDuration = serviceDurationInput.getText().toString();
        String serviceCancellationWindow = serviceCancellationWindowInput.getText().toString();


        // Pass data to the next fragment using Bundle
        Bundle bundle = new Bundle();
        bundle.putString("serviceName", serviceName);
        bundle.putString("serviceDescription", serviceDescription);
        bundle.putString("serviceReservationWindow", serviceReservationWindow);
        bundle.putString("serviceDuration", serviceDuration);
        bundle.putString("serviceCancellationWindow", serviceCancellationWindow);


        // Navigate to the next fragment
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        Fragment nextFragment = new ServiceCreationData2Fragment();
        nextFragment.setArguments(bundle); // Set the bundle on the next fragment
        transaction.replace(R.id.fragment_container, nextFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
