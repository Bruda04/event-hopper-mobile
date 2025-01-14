package com.ftn.eventhopper.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import androidx.navigation.fragment.NavHostFragment;

import com.ftn.eventhopper.R;
import com.ftn.eventhopper.clients.services.auth.UserService;
import com.ftn.eventhopper.fragments.HostFragment;
import com.ftn.eventhopper.fragments.registration.VerifyEmailFragment;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        UserService.initialize(getApplicationContext());

        Log.d("MainActivity", "setContentView called");
        // Correctly referencing the FragmentContainerView ID

        handleIntent(getIntent());

    }


    private void handleIntent(Intent intent) {
        Uri data = intent.getData();
        if (data != null) {

            String path = data.getPath();
            if (path.startsWith("/invitations/")) {
                String invitationId = data.getLastPathSegment();
                Log.d("Invitations", "Opened from invitation link with ID: " + invitationId);
                // ...
            } else if (path.startsWith("/verify-email/")) {
                String verificationToken = data.getLastPathSegment();
                Log.d("DeepLink", "Opened from verify email link with token: " + verificationToken);
                Bundle bundle = new Bundle();
                UserService.clearJwtToken();
                bundle.putString("token", verificationToken);
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.nav_main_fragment, new VerifyEmailFragment(bundle))
                        .commit();
            }
        }else{
            Fragment fragment;
            if(UserService.isTokenValid()){
                fragment = new HostFragment();
            }else{
                fragment = NavHostFragment.create(R.navigation.nav_auth);
            }

            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.nav_main_fragment, fragment)
                    .commit();
        }
    }


    public void navigateToAuthGraph() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.nav_main_fragment, NavHostFragment.create(R.navigation.nav_auth))
                .commit();
    }
}
