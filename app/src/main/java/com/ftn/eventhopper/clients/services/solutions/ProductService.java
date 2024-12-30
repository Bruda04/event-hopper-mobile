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
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

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
    @GET("solutions")
    Call<ArrayList<SimpleProductDTO>> getSolutions();

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    @GET("solutions/search")
    Call<PagedResponse<SimpleProductDTO>> getSolutionsPage(
            @Query("isProduct") boolean product,
            @Query("isService") boolean service,
            @Query("categoryId") UUID categoryId,
            @Query("eventTypeIds") ArrayList<UUID> eventTypeIds,
            @Query("minPrice") Double minPrice,
            @Query("maxPrice") Double maxPrice,
            @Query("isAvailable") Boolean availability,
            @Query("searchContent") String searchContent,
            @Query("sortField") String sortField,
            @Query("sortDirection") String sortDirection,
            @Query("page") int page,
            @Query("size") int size
            );

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    @GET("solutions/search")
    Call<PagedResponse<SimpleProductDTO>> getSolutionsPage(
           @QueryMap Map<String,String> queryParams
            );


    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    @GET("solutions/persons-top-5/{id}")
    Call<ArrayList<SimpleProductDTO>> getTop5Solutions(@Path("id") UUID id);

}
