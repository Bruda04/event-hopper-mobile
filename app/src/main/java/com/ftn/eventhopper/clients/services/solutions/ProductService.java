package com.ftn.eventhopper.clients.services.solutions;

import com.ftn.eventhopper.shared.dtos.solutions.SimpleProductDTO;
import com.ftn.eventhopper.shared.dtos.solutions.SolutionDetailsDTO;
import com.ftn.eventhopper.shared.responses.PagedResponse;

import java.util.ArrayList;
import java.util.Map;
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

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    @GET("solutions/search")
    Call<PagedResponse<SimpleProductDTO>> getSolutionsPage(Map<String, String> queryParams);

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    @GET("solutions/persons-top-5/{id}")
    Call<ArrayList<SimpleProductDTO>> getTop5Solutions(UUID id);

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    @GET("solutions")
    Call<ArrayList<SimpleProductDTO>> getSolutions();
}
