package com.ftn.eventhopper.fragments.upgrades;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ftn.eventhopper.R;


public class SuccessfulUpgradeFragment extends Fragment {

    private NavController navController;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.fragment_successful_upgrade, container, false);
        navController = NavHostFragment.findNavController(this);

        view.findViewById(R.id.login_btn).setOnClickListener(v -> {
            navController.navigate(R.id.action_successful_to_login);


        });

        return view;
    }
}