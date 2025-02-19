package com.ftn.eventhopper.clients.services.reservations;

import com.ftn.eventhopper.shared.dtos.reservations.CreateReservationProductDTO;
import com.ftn.eventhopper.shared.dtos.reservations.CreatedReservationProductDTO;


import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;

public interface ReservationService {
    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    @GET("reservations/products")
    Call<ResponseBody> buyProduct(@Body CreateReservationProductDTO createReservationProductDTO);
}
