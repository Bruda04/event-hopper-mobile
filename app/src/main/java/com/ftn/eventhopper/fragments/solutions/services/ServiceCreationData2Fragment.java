package com.ftn.eventhopper.fragments.solutions.services;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.ftn.eventhopper.R;
import com.ftn.eventhopper.fragments.solutions.services.viewmodels.ServiceCreationViewModel;
import com.ftn.eventhopper.shared.dtos.solutions.CreateServiceDTO;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class ServiceCreationData2Fragment extends Fragment {
    private NavController navController;
    private ServiceCreationViewModel viewModel;
    private Button createButton;
    private Button backButton;

    private TextInputLayout reservationWindowInput, durationInput, cancellationWindowInput, priceInput, discountInput;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_service_creation_data2, container, false);
        navController = NavHostFragment.findNavController(this);
        viewModel = new ViewModelProvider(requireActivity(),
                new ViewModelProvider.NewInstanceFactory()).get(ServiceCreationViewModel.class);

        createButton = view.findViewById(R.id.next_button);
        backButton = view.findViewById(R.id.back_button);
        reservationWindowInput = view.findViewById(R.id.reservation_window);
        durationInput = view.findViewById(R.id.duration);
        cancellationWindowInput = view.findViewById(R.id.cancellation_window);
        priceInput = view.findViewById(R.id.service_price);
        discountInput = view.findViewById(R.id.service_discount);

        setFields();

        // Set up button actions
        createButton.setOnClickListener(v -> {
            patchService();
            viewModel.createService();
            viewModel.setService(new CreateServiceDTO());
            navController.navigate(R.id.action_to_pup_services);
        });
        backButton.setOnClickListener(v -> {
            patchService();
            navController.navigate(R.id.back_to_service_creation1);
        });

        return view;
    }

    private void patchService() {
        if (reservationWindowInput.getEditText().getText() != null) {
            viewModel.getService().setReservationWindowDays(Integer.parseInt(reservationWindowInput.getEditText().getText().toString().trim()));
        }
        if (durationInput.getEditText().getText() != null) {
            viewModel.getService().setDurationMinutes(Integer.parseInt(durationInput.getEditText().getText().toString().trim()));
        }
        if (cancellationWindowInput.getEditText().getText() != null) {
            viewModel.getService().setCancellationWindowDays(Integer.parseInt(cancellationWindowInput.getEditText().getText().toString().trim()));
        }
        if (priceInput.getEditText().getText() != null) {
            viewModel.getService().setBasePrice(Double.parseDouble(priceInput.getEditText().getText().toString().trim()));
        }
        if (discountInput.getEditText().getText() != null) {
            viewModel.getService().setDiscount(Double.parseDouble(discountInput.getEditText().getText().toString().trim()));
        }
    }

    private void setFields() {
        reservationWindowInput.getEditText().setText(String.valueOf(viewModel.getService().getReservationWindowDays()));
        durationInput.getEditText().setText(String.valueOf(viewModel.getService().getDurationMinutes()));
        cancellationWindowInput.getEditText().setText(String.valueOf(viewModel.getService().getCancellationWindowDays()));
        priceInput.getEditText().setText(String.valueOf(viewModel.getService().getBasePrice()));
        discountInput.getEditText().setText(String.valueOf(viewModel.getService().getDiscount()));
    }


}
