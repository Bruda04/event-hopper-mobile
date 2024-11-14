package com.ftn.eventhopper.fragments.registration;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
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
public class OrganizerPersonalData2Fragment extends Fragment {

    private EditText phoneInput, cityInput, addressInput;
    private Button registerBtn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_organizer_personal_data2, container, false);

        cityInput = view.findViewById(R.id.register_city);
        phoneInput = view.findViewById(R.id.register_phone_number);
        addressInput = view.findViewById(R.id.register_address);

        registerBtn = view.findViewById(R.id.register_btn);

        registerBtn.setOnClickListener(v -> handleRegistration(savedInstanceState));

        return view;
    }

    private void handleRegistration(Bundle savedInstanceState) {
        String phone = phoneInput.getText().toString();
        String city = cityInput.getText().toString();
        String address = addressInput.getText().toString();

        //savedInstanceState.putString("phone", phone);
        //savedInstanceState.putString("city", city);
        //savedInstanceState.putString("address", address);


        // You can now handle the registration logic with these values
        //Log.d("Registration", "User: " + savedInstanceState.toString());

        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        Fragment fragment = new ConfirmEmailFragment();
        fragment.setArguments(savedInstanceState);
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
