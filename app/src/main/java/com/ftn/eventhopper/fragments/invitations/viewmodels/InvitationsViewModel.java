package com.ftn.eventhopper.fragments.invitations.viewmodels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ftn.eventhopper.clients.ClientUtils;
import com.ftn.eventhopper.shared.dtos.events.SimpleEventDTO;
import com.ftn.eventhopper.shared.dtos.invitations.CreateInvitationDTO;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InvitationsViewModel extends ViewModel {

    private SimpleEventDTO eventDTO = new SimpleEventDTO();
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();

    public void setEventDTO(SimpleEventDTO dto){
        eventDTO = dto;
    }

    public void sendInvitations(ArrayList<String> emails) {

        for(String email: emails){

            CreateInvitationDTO invitation = new CreateInvitationDTO();
            invitation.setTargetEmail(email);


            Call<Void> call = ClientUtils.invitationService.create(invitation);
            call.enqueue(new Callback<Void>() {

                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if(response.isSuccessful()){
                        errorMessage.postValue(null);
                    }else{
                        errorMessage.postValue("Failed to create invitation. Code: "+ response.code());
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    errorMessage.postValue("Failed to create invitation. Error: "+ t.getMessage());
                }
            });
        }
    }
}
