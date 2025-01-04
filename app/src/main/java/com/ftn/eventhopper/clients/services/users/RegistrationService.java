package com.ftn.eventhopper.clients.services.users;

import com.ftn.eventhopper.shared.dtos.users.account.CreateEventOrganizerAccountDTO;
import com.ftn.eventhopper.shared.dtos.users.account.CreateServiceProviderAccountDTO;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface RegistrationService {
    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    @POST("accounts/service-provider")
    Call<CreateServiceProviderAccountDTO> registerServiceProvider(@Body CreateServiceProviderAccountDTO createDTO);


    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    @POST("accounts/event-organizer")
    Call<CreateEventOrganizerAccountDTO> registerEventOrganizer(@Body CreateEventOrganizerAccountDTO createDTO);

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type: text/plain"
    })
    @POST("accounts/check-email")
    Call<Boolean> isEmailTaken(@Body RequestBody email);

}
