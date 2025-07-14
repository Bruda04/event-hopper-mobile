package com.ftn.eventhopper.clients.services.events;


import com.ftn.eventhopper.shared.dtos.events.CreateEventDTO;
import com.ftn.eventhopper.shared.dtos.events.GetEventAgendasDTO;
import com.ftn.eventhopper.shared.dtos.events.SimpleEventDTO;
import com.ftn.eventhopper.shared.dtos.events.SinglePageEventDTO;
import com.ftn.eventhopper.shared.dtos.solutions.CreateServiceDTO;
import com.ftn.eventhopper.shared.dtos.users.account.SimpleAccountDTO;
import com.ftn.eventhopper.shared.responses.PagedResponse;


import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

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
    @GET("events/organizer")
    Call<ArrayList<SimpleEventDTO>> getOrganizerEvents();

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    @POST("events")
    Call<Void> create(@Body CreateEventDTO event);

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    @GET("events/{id}")
    Call<SinglePageEventDTO> getEvent(@Path("id") UUID id);

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    @GET("events/persons-top-5")
    Call<ArrayList<SimpleEventDTO>> getTop5Events();


    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    @GET("events/search")
    Call<PagedResponse<SimpleEventDTO>> getEventsPage(@QueryMap Map<String, String> queryParams);

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    @GET("events/guest-list/{eventId}")
    Call<ArrayList<SimpleAccountDTO>> getGuestList(@Path("eventId") UUID id);

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    @GET("events/{id}/agenda")
    Call<GetEventAgendasDTO> getAgendaForEvent(@Path("id") UUID id);
}


