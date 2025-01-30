package com.ftn.eventhopper.fragments.events.viewmodels;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ftn.eventhopper.clients.ClientUtils;
import com.ftn.eventhopper.shared.dtos.events.SinglePageEventDTO;

import java.util.UUID;

import lombok.Getter;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EventPageViewModel extends ViewModel {
    @Getter
    private final MutableLiveData<SinglePageEventDTO> eventLiveData = new MutableLiveData<>();


    public void loadEventById(String eventId) {
        UUID id = UUID.fromString(eventId);

        ClientUtils.eventService.getEvent(id).enqueue(new Callback<SinglePageEventDTO>() {
            @Override
            public void onResponse(Call<SinglePageEventDTO> call, Response<SinglePageEventDTO> response) {
                if (response.isSuccessful() && response.body() != null) {
                    eventLiveData.setValue(response.body());
                }
            }
            @Override
            public void onFailure(Call<SinglePageEventDTO> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    public LiveData<SinglePageEventDTO> getEvent() {
        return eventLiveData;
    }

    public void toggleFavorite() {
        SinglePageEventDTO currentEvent = eventLiveData.getValue();
        if (currentEvent != null) {
            currentEvent.setFavorite(!currentEvent.isFavorite());
            eventLiveData.setValue(currentEvent);

            RequestBody emptyBody = RequestBody.create(
                    MediaType.parse("application/json"), "{}"
            );
            Call<Void> call = ClientUtils.profileService.addEventToFavorites(currentEvent.getId(), emptyBody);
            call.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.isSuccessful()) {
                        Log.d("Favorite event", "SUCCESS");
                    } else {
                        Log.d("Favorite event", "FAILURE");
                    }
                }
                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Log.d("Favorite event", "ERROR");
                }
            });
        }
    }
}
