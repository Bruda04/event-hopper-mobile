package com.ftn.eventhopper.clients.services.solutions;

import com.ftn.eventhopper.shared.dtos.solutions.CreateServiceDTO;
import com.ftn.eventhopper.shared.dtos.solutions.ServiceManagementDTO;
import com.ftn.eventhopper.shared.responses.PagedResponse;

import java.util.Map;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;

public interface ServiceService {
    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    @GET("services/management")
    Call<PagedResponse<ServiceManagementDTO>> getAllForManagement(
        @QueryMap Map<String, String> queryParams
    );

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    @DELETE("services/{id}")
    Call<Void> delete(@Path("id") UUID id);

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    @POST("services")
    Call<Void> create(@Body CreateServiceDTO service);
}
