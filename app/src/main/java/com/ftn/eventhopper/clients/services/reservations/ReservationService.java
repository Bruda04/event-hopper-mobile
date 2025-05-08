package com.ftn.eventhopper.clients.services.reservations;

import com.ftn.eventhopper.shared.dtos.reservations.CreateReservationProductDTO;
import com.ftn.eventhopper.shared.dtos.reservations.CreateReservationServiceDTO;
import com.ftn.eventhopper.shared.dtos.reservations.CreatedReservationProductDTO;


import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ReservationService {
    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    @POST("reservations/products")
    Call<ResponseBody> buyProduct(@Body CreateReservationProductDTO createReservationProductDTO);

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    @POST("reservations/services")
    Call<ResponseBody> bookService(@Body CreateReservationServiceDTO createReservationServiceDTO);

    @Headers("User-Agent: Mobile-Android")
    @GET("reservations/services/{id}/terms/")
    Call<List<LocalDateTime>> getAvailableTerms(@Path("id") UUID serviceId, @Query(value = "date", encoded = true) String date);


}
