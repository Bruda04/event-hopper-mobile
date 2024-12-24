package com.ftn.eventhopper.clients.services.events;


import com.ftn.eventhopper.shared.dtos.events.SimpleEventDTO;
import com.ftn.eventhopper.shared.responses.PagedResponse;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface EventService {
    @Headers({
        "User-Agent: Mobile-Android",
        "Content-Type:application/json"
    })
    @GET("events")
    Call<ArrayList<SimpleEventDTO>> getEvents();

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    @GET("events/{id}")
    Call<SimpleEventDTO> getEvent(@Path("id") UUID id);

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    @GET("events/persons-top-5/{usersId}")
    Call<ArrayList<SimpleEventDTO>> getTop5Events(@Path("usersId") UUID id);



    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    @GET("events/search")
    Call<PagedResponse<SimpleEventDTO>> getEventsPage(
            @Query("city") String city,
            @Query("eventTypeId") UUID eventTypeId,
            @Query("time") String time,
            @Query("searchContent") String searchContent,
            @Query("sortField") String sortField,
            @Query("sortDirection") String sortDirection,
            @Query("page") int page,
            @Query("size") int size
    );



}


