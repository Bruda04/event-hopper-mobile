package com.ftn.eventhopper.fragments.events.viewmodels;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ftn.eventhopper.clients.ClientUtils;
import com.ftn.eventhopper.shared.dtos.events.SinglePageEventDTO;

import java.util.UUID;

import lombok.Getter;
import lombok.Setter;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EventPageViewModel extends ViewModel {
    @Getter
    private final MutableLiveData<SinglePageEventDTO> eventLiveData = new MutableLiveData<>();

    @Getter
    @Setter
    private boolean isFavorited;

    public void loadEventById(String eventId) {
        UUID id = UUID.fromString(eventId);

        ClientUtils.eventService.getEvent(id).enqueue(new Callback<SinglePageEventDTO>() {
            @Override
            public void onResponse(Call<SinglePageEventDTO> call, Response<SinglePageEventDTO> response) {
                if (response.isSuccessful() && response.body() != null) {
                    eventLiveData.setValue(response.body());
                    setFavorited(response.body().isFavorite());
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
        isFavorited = !isFavorited;
        if (currentEvent != null) {
            currentEvent.setFavorite(!currentEvent.isFavorite());
            eventLiveData.setValue(currentEvent);

            if(currentEvent.isFavorite()){
                Call<Void> call = ClientUtils.profileService.addEventToFavorites(currentEvent.getId());
                call.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (response.isSuccessful()) {
                            Log.d("Adding favorite event", "SUCCESS");
                        } else {
                            Log.d("Adding favorite event", "FAILURE");
                        }
                    }
                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Log.d("Adding favorite event", "ERROR");
                    }
                });
            }else{
                Call<Void> call = ClientUtils.profileService.removeEventFromFavorites(currentEvent.getId());
                call.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (response.isSuccessful()) {
                            Log.d("Removing favorite event", "SUCCESS");
                        } else {
                            Log.d("Removing favorite event", "FAILURE");
                        }
                    }
                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Log.d("Removing favorite event", "ERROR");
                    }
                });
            }

        }
    }
}
