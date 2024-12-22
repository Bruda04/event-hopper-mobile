package com.ftn.eventhopper.fragments.solutions.services;

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

    private NavController navController;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_service_edit_data2, container, false);
        navController = NavHostFragment.findNavController(this);

        view.findViewById(R.id.next_button).setOnClickListener(v -> {
            navController.navigate(R.id.action_to_pup_services);

        });
        view.findViewById(R.id.back_button).setOnClickListener(v -> {
            navController.navigate(R.id.back_to_edit_service_1);
        });

        return view;
    }


}