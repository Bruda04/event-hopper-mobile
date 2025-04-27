package com.ftn.eventhopper.fragments.registration;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ftn.eventhopper.R;
import com.ftn.eventhopper.activities.MainActivity;
import com.ftn.eventhopper.fragments.registration.viewmodels.VerifyEmailViewModel;
import com.ftn.eventhopper.shared.models.registration.VerificationTokenState;

import android.widget.TextView;
/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class VerifyEmailFragment extends Fragment {

    private String token;
    private VerifyEmailViewModel viewModel;
    private TextView announcementText, titleText;

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
        announcementText = view.findViewById(R.id.announcement_text);
        titleText = view.findViewById(R.id.title_text);
        viewModel.getState().observe(getViewLifecycleOwner(), new Observer<VerificationTokenState>() {
            @Override
            public void onChanged(VerificationTokenState state) {
                switch (state) {
                    case ACCEPTED:
                        titleText.setText(R.string.successful_email_confirmed_title);
                        announcementText.setText(R.string.successful_email_confirmed_text);
                        break;
                    case EXPIRED:
                        titleText.setText(R.string.expired_email_confirmed_title);
                        announcementText.setText(R.string.expired_email_confirmed_text);
                        break;
                    case MISSING:
                        titleText.setText(R.string.missing_email_confirmed_title);
                        announcementText.setText(R.string.missing_email_confirmed_text);
                        break;
                }
            }
        });


        view.findViewById(R.id.login_btn).setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);  // Optional: Clear the activity stack
            startActivity(intent);
        });

        return view;
    }
}

























