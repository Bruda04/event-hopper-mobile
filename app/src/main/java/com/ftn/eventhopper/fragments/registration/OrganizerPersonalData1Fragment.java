package com.ftn.eventhopper.fragments.registration;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.ftn.eventhopper.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the  factory method to
 * create an instance of this fragment.
 */
public class OrganizerPersonalData1Fragment extends Fragment {

    private EditText emailInput, nameInput, surnameInput, passwordInput, passwordAgainInput;
    private Button nextButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_organizer_personal_data1, container, false);


        emailInput = view.findViewById(R.id.register_email);
        nameInput = view.findViewById(R.id.register_name);
        surnameInput = view.findViewById(R.id.register_surname);
        passwordInput = view.findViewById(R.id.register_password);
        passwordAgainInput = view.findViewById(R.id.register_password_again);
        nextButton = view.findViewById(R.id.next_btn);

        nextButton.setOnClickListener(v -> goToPersonalData2());

        return view;
    }

    private void goToPersonalData2() {
        // Capture the name and surname input
        String name = nameInput.getText().toString();
        String surname = surnameInput.getText().toString();
        String email = emailInput.getText().toString();
        String password = passwordInput.getText().toString();

        // Pass this data to the next fragment
        Bundle bundle = new Bundle();
        bundle.putString("name", name);
        bundle.putString("surname", surname);
        bundle.putString("email", email);
        bundle.putString("password", password);



        // Navigate to the next fragment
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        Fragment fragment = new OrganizerPersonalData2Fragment();
        fragment.setArguments(bundle);
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
