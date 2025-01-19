package com.ftn.eventhopper.clients.services.users;

import com.ftn.eventhopper.shared.dtos.profile.ProfileForPersonDTO;
import com.ftn.eventhopper.shared.dtos.users.account.SimpleAccountDTO;
import com.ftn.eventhopper.shared.dtos.users.serviceProvider.ServiceProviderDetailsDTO;

import java.util.UUID;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
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

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    @GET("accounts/profile")
    Call<ProfileForPersonDTO> getProfile();

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:text/plain"
    })
    @POST("accounts/change-profile-picture")
    Call<Void> changeProfilePicture(@Body RequestBody newProfilePicture);

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    @POST("accounts/remove-profile-picture")
    Call<Void> removeProfilePicture();

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    @POST("accounts/deactivate")
    Call<Void> deactivateAccount();
}
