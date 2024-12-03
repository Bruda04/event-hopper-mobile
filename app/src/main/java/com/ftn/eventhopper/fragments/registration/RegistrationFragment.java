package com.ftn.eventhopper.fragments.registration;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.ftn.eventhopper.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the  factory method to
 * create an instance of this fragment.
 */
// RoleSelectionFragment.java
public class RegistrationFragment extends Fragment {

    private NavController navController;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_registration, container, false);
        navController = NavHostFragment.findNavController(this);

        view.findViewById(R.id.organizer_btn).setOnClickListener(v -> {
            Log.d("Registration", "Organizer button clicked");
            navController.navigate(R.id.action_register_organizer);
        });

        view.findViewById(R.id.pup_btn).setOnClickListener(v -> {
            Log.d("Registration", "Pup button clicked");
            navController.navigate(R.id.action_register_pup);
        });

        return view;
    }

}
