package com.ftn.eventhopper.activities;

import static androidx.media.session.MediaButtonReceiver.handleIntent;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;
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

public class MainActivity extends AppCompatActivity {

    private MutableLiveData<InvitationDTO> invitationDTO = new MutableLiveData<>();
    private MutableLiveData<SimpleAccountDTO> accountDTO = new MutableLiveData<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        UserService.initialize(getApplicationContext());


    Log.d("MainActivity", "setContentView called");

        // Correctly referencing the FragmentContainerView ID
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.nav_main_fragment);

        NavController navController = navHostFragment.getNavController();

        handleIntent(getIntent());
    }

    private void handleIntent(Intent intent) {
        Uri data = intent.getData();
        if (data != null) {
            String invitationId = data.getLastPathSegment();
            getInvitationById(UUID.fromString(invitationId));

        }
    }

    protected void onStart(Bundle savedInstanceState){

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
                    //redirect();
                }else{
                    Log.i("redirect","register");
                    NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                            .findFragmentById(R.id.nav_main_fragment);
                    NavController navController = navHostFragment.getNavController();

                    NavInflater inflater = navController.getNavInflater();
                    NavGraph navGraph = inflater.inflate(R.navigation.nav_auth);
                    navGraph.setStartDestination(R.id.quickRegistrationData1Fragment);
                    navController.setGraph(navGraph);


                }
            }

            @Override
            public void onFailure(Call<SimpleAccountDTO> call, Throwable t) {

            }
        });
    }

    public void redirect(){
        if(accountDTO == null){
            Log.i("redirect","registre");
            NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.quickRegistrationData1Fragment);

            NavController navController = navHostFragment.getNavController();
        }else{
            Log.i("redirect","login");
            NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.loginFragment);

            NavController navController = navHostFragment.getNavController();
        }
    }
}
