package com.ftn.eventhopper.fragments.home.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ftn.eventhopper.clients.ClientUtils;
import com.ftn.eventhopper.shared.dtos.events.CardEventDTO;
import com.ftn.eventhopper.shared.dtos.events.SimpleEventDTO;

import java.util.ArrayList;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeViewModel extends ViewModel {

    private final MutableLiveData<ArrayList<SimpleEventDTO>> allEvents = new MutableLiveData<>();
    private final MutableLiveData<ArrayList<SimpleEventDTO>> top5Events = new MutableLiveData<>();

    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();

    public LiveData<ArrayList<SimpleEventDTO>> getTop5Events() {
        return top5Events;
    }

    public LiveData<ArrayList<SimpleEventDTO>> getEvents() {
        return allEvents;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }
    public void fetchAllEvents() {
        Call<ArrayList<SimpleEventDTO>> call = ClientUtils.eventService.getEvents();
        call.enqueue(new Callback<ArrayList<SimpleEventDTO>>() {
            @Override
            public void onResponse(Call<ArrayList<SimpleEventDTO>> call, Response<ArrayList<SimpleEventDTO>> response) {
                if(response.isSuccessful()){

                    allEvents.postValue(response.body());
                    errorMessage.postValue(null);
                }else{
                    errorMessage.postValue("Failed to fetch events. Code: "+ response.code());
                }
            }

            @Override
            public void onFailure(Call<ArrayList<SimpleEventDTO>> call, Throwable t) {
                errorMessage.postValue(t.getMessage());
            }
        });
    }

    public void fetchTopEvents(UUID id) {
        Call<ArrayList<SimpleEventDTO>> call = ClientUtils.eventService.getTop5Events(id);
        call.enqueue(new Callback<ArrayList<SimpleEventDTO>>() {
            @Override
            public void onResponse(Call<ArrayList<SimpleEventDTO>> call, Response<ArrayList<SimpleEventDTO>> response) {
                if(response.isSuccessful()){

                    top5Events.postValue(response.body());
                    errorMessage.postValue(null);
                }else{
                    errorMessage.postValue("Failed to fetch events. Code: "+ response.code());
                }
            }

            @Override
            public void onFailure(Call<ArrayList<SimpleEventDTO>> call, Throwable t) {
                errorMessage.postValue(t.getMessage());
            }
        });
    }


}
