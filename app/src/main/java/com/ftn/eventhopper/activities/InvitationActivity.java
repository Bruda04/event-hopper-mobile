//package com.ftn.eventhopper.activities;
//
//import android.net.Uri;
//import android.os.Bundle;
//import android.util.Log;
//
//import androidx.activity.EdgeToEdge;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.core.graphics.Insets;
//import androidx.core.view.ViewCompat;
//import androidx.core.view.WindowInsetsCompat;
//import androidx.lifecycle.LiveData;
//import androidx.lifecycle.MutableLiveData;
//import androidx.navigation.NavController;
//import androidx.navigation.fragment.NavHostFragment;
//
//import com.ftn.eventhopper.R;
//import com.ftn.eventhopper.clients.ClientUtils;
//import com.ftn.eventhopper.clients.interceptors.JWTInterceptor;
//import com.ftn.eventhopper.shared.dtos.events.SimpleEventDTO;
//import com.ftn.eventhopper.shared.dtos.invitations.InvitationDTO;
//import com.ftn.eventhopper.shared.dtos.users.account.SimpleAccountDTO;
//
//import java.util.ArrayList;
//import java.util.UUID;
//
//import retrofit2.Call;
//import retrofit2.Callback;
//import retrofit2.Response;
//
//public class InvitationActivity extends AppCompatActivity {
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_invitation);
//        Log.i("invitation act", "UPAOOOO");
//        Uri data = getIntent().getData();
//        if (data != null) {
//            String invitationId = data.getLastPathSegment(); // "6666407d-dc7b-4288-a318-a5be27fbdf7d"
//            getInvitationById(UUID.fromString(invitationId));
//
//
//            // Proverite korisnika
////            if (userIsLoggedIn()) {
////                addEventToUser(invitationId);
////            } else {
////                redirectToLoginOrRegistration(invitationId);
////            }
//        }
//    }
//
//    private void getInvitationById(UUID id) {
//
//        Call<InvitationDTO> call = ClientUtils.invitationService.getInvitation(id);
//        call.enqueue(new Callback<InvitationDTO>() {
//
//            @Override
//            public void onResponse(Call<InvitationDTO> call, Response<InvitationDTO> response) {
//                if(response.isSuccessful()){
//                    invitationDTO.postValue(response.body());
//                    getAccountByEmail(invitationDTO.getValue().getTargetEmail());
//                }
//            }
//
//            @Override
//            public void onFailure(Call<InvitationDTO> call, Throwable t) {
//
//            }
//        });
//
//    }
//
//    private void getAccountByEmail(String email) {
//
//        Call<SimpleAccountDTO> call = ClientUtils.profileService.getActiveByEmail(email);
//        call.enqueue(new Callback<SimpleAccountDTO>() {
//
//            @Override
//            public void onResponse(Call<SimpleAccountDTO> call, Response<SimpleAccountDTO> response) {
//                if(response.isSuccessful()){
//                    accountDTO.postValue(response.body());
//                    redirect();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<SimpleAccountDTO> call, Throwable t) {
//
//            }
//        });
//    }
//
//    public void redirect(){
//        if(accountDTO == null){
//            NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
//                    .findFragmentById(R.id.quickRegistrationData1Fragment);
//
//            NavController navController = navHostFragment.getNavController();
//        }else{
//            NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
//                    .findFragmentById(R.id.loginFragment);
//
//            NavController navController = navHostFragment.getNavController();
//        }
//    }
//
//}