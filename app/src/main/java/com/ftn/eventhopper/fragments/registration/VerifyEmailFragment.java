package com.ftn.eventhopper.fragments.registration;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ftn.eventhopper.R;
import com.ftn.eventhopper.activities.MainActivity;
import com.ftn.eventhopper.clients.services.auth.UserService;
import com.ftn.eventhopper.fragments.HostFragment;
import com.ftn.eventhopper.fragments.registration.viewmodels.VerifyEmailViewModel;
import com.ftn.eventhopper.shared.models.registration.VerificationTokenState;

import android.widget.TextView;

import java.util.UUID;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class VerifyEmailFragment extends Fragment {

    private String token;
    private VerifyEmailViewModel viewModel;
    private TextView announcementText;

    public VerifyEmailFragment(Bundle bundle) {
        this.token = bundle.getString("token");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_verify_email, container, false);
        viewModel = new ViewModelProvider(this).get(VerifyEmailViewModel.class);
        viewModel.verifyToken(this.token);

        viewModel.getState().observe(getViewLifecycleOwner(), new Observer<VerificationTokenState>() {
            @Override
            public void onChanged(VerificationTokenState state) {
                switch (state) {
                    case ACCEPTED:
                        announcementText.setText("Congratulations!\nYour account has been verified. You can now log in and start using your account.");
                        break;
                    case EXPIRED:
                        announcementText.setText("Oops! Too late...\nYour verification expired, please re-register.");
                        break;
                    case MISSING:
                        announcementText.setText("Oops! Something went wrong...\nWe couldn't find your account, your account may have been suspended or already verified.");
                        break;
                }
            }
        });


        view.findViewById(R.id.login_btn).setOnClickListener(v -> {
            ((MainActivity) requireActivity()).navigateToAuthGraph();
        });

        return view;
    }
}

























