package com.ftn.eventhopper.clients.services.invitations;

import com.ftn.eventhopper.shared.dtos.invitations.CreateInvitationDTO;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface InvitationService {

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    @POST("invitations")
    Call<Void> create(@Body CreateInvitationDTO invitationDTO);

}
