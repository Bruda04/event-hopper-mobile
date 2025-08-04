package com.ftn.eventhopper.fragments.events.viewmodels;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ftn.eventhopper.clients.ClientUtils;
import com.ftn.eventhopper.shared.dtos.events.SimpleEventDTO;

import java.util.ArrayList;

import lombok.Getter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyEventsViewModel extends ViewModel {
    @Getter
    private final MutableLiveData<ArrayList<SimpleEventDTO>> eventLiveData = new MutableLiveData<>();

    public void getMyEvents() {
        Call<ArrayList<SimpleEventDTO>> call = ClientUtils.eventService.getOrganizerEvents();
        call.enqueue(new Callback<ArrayList<SimpleEventDTO>>() {
            @Override
            public void onResponse(Call<ArrayList<SimpleEventDTO>> call, Response<ArrayList<SimpleEventDTO>> response) {
                if (response.isSuccessful()) {
                    eventLiveData.postValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<ArrayList<SimpleEventDTO>> call, Throwable t) {
                Log.d("Fetching my events", "ERROR");
            }
        });
    }
}
