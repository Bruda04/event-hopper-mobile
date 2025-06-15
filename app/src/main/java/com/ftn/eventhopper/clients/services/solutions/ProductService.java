package com.ftn.eventhopper.clients.services.solutions;

import com.ftn.eventhopper.shared.dtos.comments.CreateCommentDTO;
import com.ftn.eventhopper.shared.dtos.ratings.CreateProductRatingDTO;
import com.ftn.eventhopper.shared.dtos.solutions.CreateProductDTO;
import com.ftn.eventhopper.shared.dtos.solutions.ProductForManagementDTO;
import com.ftn.eventhopper.shared.dtos.solutions.SimpleProductDTO;
import com.ftn.eventhopper.shared.dtos.prices.PriceManagementDTO;
import com.ftn.eventhopper.shared.dtos.prices.UpdatePriceDTO;
import com.ftn.eventhopper.shared.dtos.solutions.SolutionDetailsDTO;
import com.ftn.eventhopper.shared.dtos.solutions.UpdateProductDTO;
import com.ftn.eventhopper.shared.responses.PagedResponse;

import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

public interface ProductService {

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    @GET("products/management")
    Call<PagedResponse<ProductForManagementDTO>> getAllForManagement(
            @QueryMap Map<String, String> queryParams
    );

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    @POST("products")
    Call<Void> create(@Body CreateProductDTO product);

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    @PUT("products/{id}")
    Call<Void> update(@Path("id") UUID id, @Body UpdateProductDTO service);

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    @DELETE("products/{id}")
    Call<Void> delete(@Path("id") UUID id);

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
    @GET("prices/management")
    Call<ArrayList<PriceManagementDTO>> getPricesForManagement();

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
    @GET("solutions/persons-top-5")
    Call<ArrayList<SimpleProductDTO>> getTop5Solutions();


    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    @PUT("prices/{productId}")
    Call<ResponseBody> updateProductsPrice(@Path("productId") UUID productId, @Body UpdatePriceDTO updatePriceDTO);

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    @POST("ratings/solution")
    Call<ResponseBody> rateProduct(@Body CreateProductRatingDTO createProductRatingDTO);

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    @POST("comments")
    Call<ResponseBody> commentProduct(@Body CreateCommentDTO createCommentDTO);

}
