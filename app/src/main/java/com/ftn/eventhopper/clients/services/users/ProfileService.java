package com.ftn.eventhopper.clients.services.users;

import com.ftn.eventhopper.shared.dtos.users.account.SimpleAccountDTO;
import com.ftn.eventhopper.shared.dtos.users.serviceProvider.ServiceProviderDetailsDTO;

import java.util.UUID;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ProfileService {

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    @POST("persons/favorite-solutions/{id}")
    Call<Void> addSolutionToFavorites(@Path("id") UUID id);

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    @DELETE("persons/favorite-solutions/{id}")
    Call<Void> removeSolutionFromFavorites(@Path("id") UUID id);

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    @GET("service-providers/{id}/details")
    Call<ServiceProviderDetailsDTO> getServiceProviderDetailsById(@Path("id") UUID id);


    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    @GET("accounts/active/{email}")
    Call<SimpleAccountDTO> getActiveByEmail(@Path("email") String email);
}
