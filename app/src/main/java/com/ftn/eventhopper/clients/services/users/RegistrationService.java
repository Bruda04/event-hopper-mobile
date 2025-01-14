package com.ftn.eventhopper.clients.services.users;

import com.ftn.eventhopper.shared.dtos.events.SimpleEventDTO;
import com.ftn.eventhopper.shared.dtos.users.account.CreateEventOrganizerAccountDTO;
import com.ftn.eventhopper.shared.dtos.users.account.CreatePersonAccountDTO;
import com.ftn.eventhopper.shared.dtos.users.account.CreateServiceProviderAccountDTO;
import com.ftn.eventhopper.shared.models.registration.VerificationTokenState;

import java.util.UUID;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

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
            "Content-Type:application/json"
    })
    @POST("accounts/person")
    Call<CreatePersonAccountDTO> registerPerson(@Body CreatePersonAccountDTO createDTO);

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type: text/plain"
    })
    @POST("accounts/check-email")
    Call<Boolean> isEmailTaken(@Body RequestBody email);


    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type: text/plain"
    })
    @GET("accounts/verify/{token}")
    Call<VerificationTokenState> verifyToken(@Path("token") String token);

}
