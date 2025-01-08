package com.ftn.eventhopper.clients.services.categories;

import com.ftn.eventhopper.shared.dtos.categories.CategoryDTO;
import com.ftn.eventhopper.shared.dtos.categories.CategorySuggestionDTO;
import com.ftn.eventhopper.shared.dtos.categories.CreateCategoryDTO;
import com.ftn.eventhopper.shared.dtos.categories.CreateCategorySuggestionDTO;
import com.ftn.eventhopper.shared.dtos.categories.CreatedCategorySuggestionDTO;
import com.ftn.eventhopper.shared.dtos.categories.UpdateCategoryDTO;

import java.util.ArrayList;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface CategoriesService {
    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    @GET("categories")
    Call<ArrayList<CategoryDTO>> getApproved();

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    @GET("categories/suggestions")
    Call<ArrayList<CategorySuggestionDTO>> getSuggestions();

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    @POST("categories")
    Call<Void> addCategory(@Body CreateCategoryDTO category);

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    @DELETE("categories/{id}")
    Call<Void> deleteCategory(@Path("id") UUID id);

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    @PUT("categories/{id}")
    Call<Void> updateCategory(@Path("id") UUID id, @Body UpdateCategoryDTO category);

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    @PUT("categories/suggestions/{id}/approve")
    Call<Void> approveCategory(@Path("id") UUID id);

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    @PUT("categories/suggestions/{id}/reject/{substituteCategoryId}")
    Call<Void> rejectCategory(@Path("id") UUID id, @Path("substituteCategoryId") UUID substituteCategoryId);

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    @POST("categories/suggestions")
    Call<CreatedCategorySuggestionDTO> makeSuggestion(@Body CreateCategorySuggestionDTO categorySuggestion);

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    @GET("categories/{id}")
    Call<CategoryDTO> getCategory(@Path("id") UUID id);

}
