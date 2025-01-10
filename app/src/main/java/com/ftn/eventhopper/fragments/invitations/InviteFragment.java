package com.ftn.eventhopper.fragments.invitations;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ftn.eventhopper.R;
import com.google.android.material.textfield.TextInputEditText;


public class InviteFragment extends Fragment {

    private TextInputEditText emailField;
    private LinearLayout emailsList;
    private Button addButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.fragment_invite, container, false);

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
        return view;
    }

    private void addEmailToDialogList(String email, LinearLayout emailsList) {
        View emailView = LayoutInflater.from(getContext()).inflate(R.layout.invited_email_item, emailsList, false);

        TextView emailLabel = emailView.findViewById(R.id.email_label);
        Button deleteButton = emailView.findViewById(R.id.delete_button);

        emailLabel.setText(email);
        deleteButton.setOnClickListener(view -> emailsList.removeView(emailView));

        emailsList.addView(emailView);
    }
}