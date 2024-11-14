package com.ftn.eventhopper.fragments.registration;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

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
public class RoleSelectionFragment extends Fragment {

    private Button organizerButton, pupButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_role_selection, container, false);

        organizerButton = view.findViewById(R.id.organizer_btn);
        pupButton = view.findViewById(R.id.pup_btn);

        organizerButton.setOnClickListener(v -> goToPersonalInfo("Organizer"));
        pupButton.setOnClickListener(v -> goToPersonalInfo("PUP"));

        return view;
    }

    private void goToPersonalInfo(String role) {
        // Store the selected role and move to the next fragment
        Bundle bundle = new Bundle();
        bundle.putString("role", role);

        // Use getParentFragmentManager() to ensure the correct fragment manager is used
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();

        Fragment fragment = null;
        if(role.equals("Organizer")){
            fragment = new OrganizerPersonalData1Fragment();
        }else{
            fragment = new PupPersonalData1Fragment();
        }
        fragment.setArguments(bundle);
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
