package com.ftn.eventhopper.clients.services;

import com.ftn.eventhopper.shared.dtos.solutions.SolutionDetailsDTO;
import com.ftn.eventhopper.shared.dtos.users.serviceProvider.ServiceProviderDetailsDTO;

import java.util.UUID;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;

public interface ProfileService {

    // TODO: Implement this with JWT
    Call<Void> addSolutionToFavorites(UUID id);

    // TODO: Implement this with JWT
    Call<Void> removeSolutionFromFavorites(UUID id);

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    @GET("service-providers/{id}/details")
    Call<ServiceProviderDetailsDTO> getServiceProviderDetailsById(@Path("id") UUID id);
}
