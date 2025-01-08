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
import com.ftn.eventhopper.fragments.solutions.services.viewmodels.ServiceEditViewModel;
import com.google.android.material.button.MaterialButton;

public class ServiceEditData2Fragment extends Fragment {

    private NavController navController;
    private ServiceEditViewModel viewModel;

    private Button saveButton;
    private Button backButton;

    private MaterialButton uploadImagesButton;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_service_edit_data2, container, false);
        navController = NavHostFragment.findNavController(this);

        viewModel = new ViewModelProvider(requireActivity(),
                new ViewModelProvider.NewInstanceFactory()).get(ServiceEditViewModel.class);

        saveButton = view.findViewById(R.id.next_button);
        backButton = view.findViewById(R.id.back_button);
        uploadImagesButton = view.findViewById(R.id.upload_images_btn);

        setFields();

        saveButton.setOnClickListener(v -> {
            if (validate()) {
                patchService();
                viewModel.updateService();
            }
            navController.navigate(R.id.action_to_pup_services);

        });

        backButton.setOnClickListener(v -> {
            navController.navigate(R.id.back_to_edit_service_1);
        });

        viewModel.getEdited().observe(getViewLifecycleOwner(), edited -> {
            if (edited) {
                navController.navigate(R.id.action_to_pup_services);
            }
        });

        return view;
    }

    private void patchService() {
    }

    private boolean validate() {
        return true;
    }

    private void setFields() {
    }


}