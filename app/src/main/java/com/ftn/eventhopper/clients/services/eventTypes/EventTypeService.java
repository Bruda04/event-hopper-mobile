package com.ftn.eventhopper.clients.services.eventTypes;

import com.ftn.eventhopper.shared.dtos.eventTypes.CreateEventTypeDTO;
import com.ftn.eventhopper.shared.dtos.eventTypes.EventTypeManagementDTO;
import com.ftn.eventhopper.shared.dtos.eventTypes.SimpleEventTypeDTO;
import com.ftn.eventhopper.shared.dtos.eventTypes.UpdateEventTypeDTO;
import com.ftn.eventhopper.shared.dtos.events.SimpleEventDTO;

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

public interface EventTypeService {

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    @GET("event-types")
    Call<EventTypeManagementDTO> getEventTypes();

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type: application/json"
    })
    @PUT("event-types/{id}")
    Call<Void> updateEventType(@Path("id") UUID id, @Body UpdateEventTypeDTO updateDTO);

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type: application/json"
    })
    @POST("event-types")
    Call<Void> addEventType(@Body CreateEventTypeDTO createDTO);

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type: application/json"
    })
    @DELETE("event-types/{id}")
    Call<Void> deleteEventType(@Path("id") UUID id);
}
