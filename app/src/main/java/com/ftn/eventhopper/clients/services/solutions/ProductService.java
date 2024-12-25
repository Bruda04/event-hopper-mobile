package com.ftn.eventhopper.clients.services.solutions;

import com.ftn.eventhopper.shared.dtos.prices.PriceManagementDTO;
import com.ftn.eventhopper.shared.dtos.prices.UpdatePriceDTO;
import com.ftn.eventhopper.shared.dtos.solutions.SolutionDetailsDTO;

import java.util.ArrayList;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.PUT;
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
    @GET("prices/management")
    Call<ArrayList<PriceManagementDTO>> getPricesForManagement();

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    @PUT("prices/management/{productId}")
    Call<Void> updateProductsPrice(@Path("productId") UUID productId, @Body UpdatePriceDTO updatePriceDTO);
}
