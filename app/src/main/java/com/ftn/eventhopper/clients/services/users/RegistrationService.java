package com.ftn.eventhopper.clients.services.users;

import com.ftn.eventhopper.shared.dtos.users.account.CreateEventOrganizerAccountDTO;
import com.ftn.eventhopper.shared.dtos.users.account.CreateServiceProviderAccountDTO;
import com.ftn.eventhopper.shared.dtos.users.account.CreatedEventOrganizerAccountDTO;

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
    Call<CreatedEventOrganizerAccountDTO> registerEventOrganizer(@Body CreateEventOrganizerAccountDTO createDTO);


}
