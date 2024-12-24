package com.ftn.eventhopper.fragments.home.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ftn.eventhopper.clients.ClientUtils;
import com.ftn.eventhopper.shared.dtos.events.CardEventDTO;
import com.ftn.eventhopper.shared.dtos.events.SimpleEventDTO;

import java.util.ArrayList;

public class HomeViewModel extends ViewModel {

    private final MutableLiveData<ArrayList<CardEventDTO>> allEvents = new MutableLiveData<>();
    private final MutableLiveData<ArrayList<CardEventDTO>> top5Events = new MutableLiveData<>();

    public LiveData<ArrayList<CardEventDTO>> getTop5Events() {
        return top5Events;
    }

    public LiveData<ArrayList<CardEventDTO>> getEvents() {
        return allEvents;
    }
    public void fetchAllEvents() {
        Call<ArrayList<SimpleEventDTO>> call = ClientUtils.eventService.getAll();

    }

    public void fetchTopEvents() {
    }
}
