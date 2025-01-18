package com.ftn.eventhopper.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.fragment.app.Fragment;

import androidx.navigation.NavController;
import androidx.navigation.NavGraph;
import androidx.navigation.NavInflater;
import androidx.navigation.fragment.NavHostFragment;

import com.ftn.eventhopper.R;
import com.ftn.eventhopper.clients.ClientUtils;
import com.ftn.eventhopper.clients.services.auth.UserService;

import com.ftn.eventhopper.shared.dtos.invitations.InvitationDTO;
import com.ftn.eventhopper.shared.dtos.users.account.SimpleAccountDTO;

import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import com.ftn.eventhopper.fragments.HostFragment;
import com.ftn.eventhopper.fragments.registration.VerifyEmailFragment;

public class MainActivity extends AppCompatActivity {
    private MutableLiveData<InvitationDTO> invitationDTO = new MutableLiveData<>();
    private MutableLiveData<SimpleAccountDTO> accountDTO = new MutableLiveData<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        UserService.initialize(getApplicationContext());
        if (UserService.isTokenValid()) {
            ClientUtils.connectWebSocket();
        }

        Log.d("MainActivity", "setContentView called");

        // Correctly referencing the FragmentContainerView ID
        handleIntent(getIntent());
    }


    private void handleIntent(Intent intent) {
        Uri data = intent.getData();
        if (data != null ) {
            String path = data.getPath();
            if (path.startsWith("/invitations/")) {
                String invitationId = data.getLastPathSegment();
                Log.d("Invitations", "Opened from invitation link with ID: " + invitationId);
                getInvitationById(UUID.fromString(invitationId));
            } else if (path.startsWith("/verify-email/")) {
                String verificationToken = data.getLastPathSegment();
                Log.d("Email verification", "Opened from verify email link with token: " + verificationToken);
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




    private void getInvitationById(UUID id) {

        Call<InvitationDTO> call = ClientUtils.invitationService.getInvitation(id);
        call.enqueue(new Callback<InvitationDTO>() {

            @Override
            public void onResponse(Call<InvitationDTO> call, Response<InvitationDTO> response) {
                if(response.isSuccessful() && response.body() != null){
                    invitationDTO.setValue(response.body());
                    getAccountByEmail(invitationDTO.getValue().getTargetEmail());
                }
            }

            @Override
            public void onFailure(Call<InvitationDTO> call, Throwable t) {

            }
        });

    }

    private void getAccountByEmail(String email) {

        Call<SimpleAccountDTO> call = ClientUtils.profileService.getActiveByEmail(email);
        call.enqueue(new Callback<SimpleAccountDTO>() {

            @Override
            public void onResponse(Call<SimpleAccountDTO> call, Response<SimpleAccountDTO> response) {
                if(response.isSuccessful()){
                    accountDTO.setValue(response.body());
                    Fragment fragment;
                    if(UserService.isTokenValid()){

                        fragment = new HostFragment();
                        ClientUtils.profileService.addEventToAttending(invitationDTO.getValue().getEvent().getId());
                    }else{
                        Bundle bundle = new Bundle();
                        bundle.putString("attending-event", invitationDTO.getValue().getEvent().getId().toString());
                        fragment = NavHostFragment.create(R.navigation.nav_auth, bundle);

                    }

                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.nav_main_fragment, fragment)
                            .commit();

                }else{
                    Log.i("redirect","register");
                    Bundle bundle = new Bundle();
                    bundle.putString("email", invitationDTO.getValue().getTargetEmail());
                    bundle.putString("attending-event", invitationDTO.getValue().getEvent().getId().toString());
                    NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                            .findFragmentById(R.id.nav_main_fragment);
                    NavController navController = navHostFragment.getNavController();

                    NavInflater inflater = navController.getNavInflater();
                    NavGraph navGraph = inflater.inflate(R.navigation.nav_auth);
                    navGraph.setStartDestination(R.id.quickRegistrationData1Fragment);
                    navController.setGraph(navGraph,bundle);
                    //kako da nakon registracije i logina se doda event u attending?


                }
            }

            @Override
            public void onFailure(Call<SimpleAccountDTO> call, Throwable t) {

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ClientUtils.disconnectStompClient();
    }

    @Override
    protected void onStart() {
        super.onStart();
        ClientUtils.connectWebSocket();
    }

}