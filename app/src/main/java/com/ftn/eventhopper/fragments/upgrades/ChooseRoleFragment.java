package com.ftn.eventhopper.fragments.upgrades;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ftn.eventhopper.R;
import com.ftn.eventhopper.fragments.invitations.viewmodels.InvitationsViewModel;
import com.ftn.eventhopper.fragments.upgrades.viewmodels.UpgradeViewModel;


public class ChooseRoleFragment extends Fragment {

    private UpgradeViewModel viewModel;
    private NavController navController;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_choose_role, container, false);
        viewModel = new ViewModelProvider(requireActivity()).get(UpgradeViewModel.class);

        navController = NavHostFragment.findNavController(this);

        view.findViewById(R.id.organizer_btn).setOnClickListener(v -> {

            viewModel.upgradeToOD();
            navController.navigate(R.id.action_to_success);
        });

        view.findViewById(R.id.pup_btn).setOnClickListener(v -> {

            navController.navigate(R.id.action_to_company_data1);
        });

        return view;
    }
}