package com.ftn.eventhopper.fragments.invitations.viewmodels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ftn.eventhopper.clients.ClientUtils;
import com.ftn.eventhopper.shared.dtos.events.SimpleEventDTO;
import com.ftn.eventhopper.shared.dtos.invitations.CreateInvitationDTO;
import com.ftn.eventhopper.shared.dtos.location.SimpleLocationDTO;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InvitationsViewModel extends ViewModel {

    //need to implement logic for event when single event will be implemented
    private SimpleEventDTO eventDTO = new SimpleEventDTO();
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();

    public void setEventDTO(SimpleEventDTO dto){
        //eventDTO = dto;

    }

    public void sendInvitations(ArrayList<String> emails) {

        for(String email: emails){

            CreateInvitationDTO invitation = new CreateInvitationDTO();
            invitation.setTargetEmail(email);

            eventDTO.setId(UUID.fromString("3f7b2c9e-4a6f-4d5b-b8c1-7a2f9e3b6d4a"));
            eventDTO.setName("Tech Meetup");
            eventDTO.setDescription("Annual gathering");
            eventDTO.setTime(null);
            eventDTO.setPicture("15.jpg");
            eventDTO.setEventType(null);
            eventDTO.setLocation(new SimpleLocationDTO(UUID.fromString("4e2d5b9f-a6c3-49a1-b8f5-7d9c7b6e3a2c"),"London","789 Oak St"));

            invitation.setEvent(eventDTO);

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
