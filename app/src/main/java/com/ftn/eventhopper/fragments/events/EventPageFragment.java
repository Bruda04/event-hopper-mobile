package com.ftn.eventhopper.fragments.events;

import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
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
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;


public class EventPageFragment extends Fragment {

    private TextInputEditText emailField;
    private LinearLayout emailsList;

    private Button inviteBtn;

    public EventPageFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=  inflater.inflate(R.layout.fragment_event_page, container, false);

        inviteBtn = view.findViewById(R.id.invite_people_button);
//        emailField = view.findViewById(R.id.invite_email_field);
//        emailsList = view.findViewById(R.id.emails_list);

        inviteBtn.setOnClickListener(v -> showInviteDialog());

        return view;
    }

    private void showInviteDialog() {
        View dialogView = getLayoutInflater().inflate(R.layout.invite_dialog, null);
        MaterialAlertDialogBuilder dialog = new MaterialAlertDialogBuilder(getContext());

        TextInputEditText emailField = dialogView.findViewById(R.id.invite_email_field);
        LinearLayout emailsList = dialogView.findViewById(R.id.emails_list);
        Button addButton = dialogView.findViewById(R.id.invite_button);

        addButton.setOnClickListener(v -> {
            String email = emailField.getText().toString().trim();
            if (TextUtils.isEmpty(email)) {     //dodatne provere za email
                Toast.makeText(getContext(), "Please enter a valid email", Toast.LENGTH_SHORT).show();
            } else {
                addEmailToDialogList(email, emailsList);
                emailField.setText("");
            }
        });

        dialog.setTitle("Invite people");
        dialog.setView(dialogView);
        dialog.setPositiveButton("Invite", (dialogInterface, i) -> {
        });
        dialog.setNegativeButton("Cancel", (dialogInterface, i) -> {
            dialogInterface.dismiss();
        });
        AlertDialog alertDialog = dialog.create();
        alertDialog.show();
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v1 -> {
            if (invitePoeple(dialogView)) {
                alertDialog.dismiss();
            }
        });
    }

    private void addEmailToDialogList(String email, LinearLayout emailsList) {
        View emailView = LayoutInflater.from(getContext()).inflate(R.layout.invited_email_item, emailsList, false);

        TextView emailLabel = emailView.findViewById(R.id.email_label);
        Button deleteButton = emailView.findViewById(R.id.delete_button);

        emailLabel.setText(email);
        deleteButton.setOnClickListener(view -> emailsList.removeView(emailView));

        emailsList.addView(emailView);
    }

    private boolean invitePoeple(View dialogView){
        boolean isValid = true;

        //dodati logiku za poziv ljudi

        return isValid;
    }


}