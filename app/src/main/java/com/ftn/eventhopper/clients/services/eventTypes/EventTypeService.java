package com.ftn.eventhopper.clients.services.eventTypes;

import com.ftn.eventhopper.shared.dtos.eventTypes.EventTypeManagementDTO;
import com.ftn.eventhopper.shared.dtos.eventTypes.SimpleEventTypeDTO;
import com.ftn.eventhopper.shared.dtos.events.SimpleEventDTO;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;

public interface EventTypeService {

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    @GET("event-types")
    Call<EventTypeManagementDTO> getEventTypes();

}
