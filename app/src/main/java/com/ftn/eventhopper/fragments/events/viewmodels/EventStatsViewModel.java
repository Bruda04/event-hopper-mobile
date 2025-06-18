package com.ftn.eventhopper.fragments.events.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ftn.eventhopper.clients.ClientUtils;
import com.ftn.eventhopper.clients.services.events.EventService;
import com.ftn.eventhopper.shared.dtos.events.GraphDataDTO;
import com.ftn.eventhopper.shared.dtos.events.SimpleEventDTO;

import java.util.ArrayList;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EventStatsViewModel extends ViewModel {

    private final MutableLiveData<GraphDataDTO> stats = new MutableLiveData<>();

    public LiveData<GraphDataDTO> getStats() {
        return stats;
    }

    public void fetchStats(String eventId) {
        ClientUtils.eventService.getEventGraph(UUID.fromString(eventId)).enqueue(new Callback<GraphDataDTO>() {
            @Override
            public void onResponse(Call<GraphDataDTO> call, Response<GraphDataDTO> response) {
                if (response.isSuccessful()) {
                    stats.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<GraphDataDTO> call, Throwable t) {
                stats.setValue(null);
            }
        });
    }
}
