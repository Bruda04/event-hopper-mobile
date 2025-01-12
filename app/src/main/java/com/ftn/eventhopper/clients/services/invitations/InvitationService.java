package com.ftn.eventhopper.clients.services.invitations;

import com.ftn.eventhopper.shared.dtos.invitations.CreateInvitationDTO;
import com.ftn.eventhopper.shared.dtos.invitations.InvitationDTO;

import java.util.UUID;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface InvitationService {

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    @POST("invitations")
    Call<Void> create(@Body CreateInvitationDTO invitationDTO);

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    @GET("invitations/{id}")
    Call<InvitationDTO> getInvitation(@Path("id") UUID id);

}
