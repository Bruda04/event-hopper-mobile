package com.ftn.eventhopper.fragments.invitations;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ftn.eventhopper.R;
import com.ftn.eventhopper.fragments.home.viewmodels.HomeViewModel;
import com.ftn.eventhopper.fragments.invitations.viewmodels.InvitationsViewModel;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;


public class InviteFragment extends Fragment {

    private InvitationsViewModel viewModel;
    private TextInputEditText emailField;
    private LinearLayout emailsList;
    private Button addButton;
    private Button inviteButton;

    private ArrayList<String> emails = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.fragment_invite, container, false);
        viewModel = new ViewModelProvider(requireActivity()).get(InvitationsViewModel.class);

        emailField = view.findViewById(R.id.invite_email);
        emailsList = view.findViewById(R.id.emails_list);
        addButton  = view.findViewById(R.id.invite_button);
        addButton.setOnClickListener( v ->{

                String email = emailField.getText().toString().trim();

                if (TextUtils.isEmpty(email)) {     //dodatne provere za email
                    Toast.makeText(getContext(), "Please enter a valid email", Toast.LENGTH_SHORT).show();
                } else {
                    addEmailToDialogList(email, emailsList);
                    emailField.setText("");
                }
        });


        inviteButton = view.findViewById(R.id.invite_people_button);
        inviteButton.setOnClickListener( v ->{
            for(String email: emails){
                Log.i("Invitation",email);
            }
            viewModel.sendInvitations(emails);
            getParentFragmentManager().popBackStack();
        });


        return view;
    }

    private void addEmailToDialogList(String email, LinearLayout emailsList) {
        View emailView = LayoutInflater.from(getContext()).inflate(R.layout.invited_email_item, emailsList, false);

        TextView emailLabel = emailView.findViewById(R.id.email_label);
        Button deleteButton = emailView.findViewById(R.id.delete_button);

        emailLabel.setText(email);
        emails.add(email);
        deleteButton.setOnClickListener(view -> {
            emails.remove(email);
            emailsList.removeView(emailView);
        });

        emailsList.addView(emailView);
    }
}