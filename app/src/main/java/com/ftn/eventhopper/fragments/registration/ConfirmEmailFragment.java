package com.ftn.eventhopper.fragments.registration;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.ftn.eventhopper.R;
import com.ftn.eventhopper.activities.HostActivity;
import com.ftn.eventhopper.activities.LoginActivity;

/**
 * A simple {@link Fragment} subclass.
 * Use the  factory method to
 * create an instance of this fragment.
 */
public class ConfirmEmailFragment extends Fragment {

    private Button loginBtn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_confirm_email, container, false);


        loginBtn = view.findViewById(R.id.login_btn);

        loginBtn.setOnClickListener(v -> handleBackToHomepage());

        return view;
    }

    private void handleBackToHomepage() {
        Log.d("Confirm email", "Back to login clicked");

        Intent intent = new Intent(getActivity(), LoginActivity.class);
        startActivity(intent);
    }
}
