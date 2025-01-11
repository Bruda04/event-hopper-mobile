package com.ftn.eventhopper.activities;

import android.net.Uri;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.ftn.eventhopper.R;
import com.ftn.eventhopper.clients.ClientUtils;
import com.ftn.eventhopper.clients.interceptors.JWTInterceptor;
import com.ftn.eventhopper.shared.dtos.users.account.SimpleAccountDTO;

public class InvitationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invitation);

        Uri data = getIntent().getData();
        if (data != null) {
            String invitationId = data.getLastPathSegment(); // "6666407d-dc7b-4288-a318-a5be27fbdf7d"
            //dobaviti pozivnicu i iz nje dobaviti email

            //SimpleAccountDTO dto = ClientUtils.profileService.getActiveByEmail();


            // Proverite korisnika
//            if (userIsLoggedIn()) {
//                addEventToUser(invitationId);
//            } else {
//                redirectToLoginOrRegistration(invitationId);
//            }
        }
    }

}