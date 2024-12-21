package com.ftn.eventhopper.clients.services;

import com.ftn.eventhopper.shared.dtos.solutions.SolutionDetailsDTO;

import java.util.UUID;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;

public interface ProductService {

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    @GET("solutions/{id}/details")
    Call<SolutionDetailsDTO> getSolutionDetailsById(@Path("id") UUID id);
}
