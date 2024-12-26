package com.ftn.eventhopper.fragments.home.viewmodels;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ftn.eventhopper.clients.ClientUtils;
import com.ftn.eventhopper.shared.dtos.eventTypes.SimpleEventTypeDTO;
import com.ftn.eventhopper.shared.dtos.events.SimpleEventDTO;
import com.ftn.eventhopper.shared.dtos.location.LocationDTO;
import com.ftn.eventhopper.shared.responses.PagedResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeViewModel extends ViewModel {

    private final MutableLiveData<ArrayList<SimpleEventDTO>> allEvents = new MutableLiveData<>();
    private final MutableLiveData<PagedResponse<SimpleEventDTO>> allEventsPage = new MutableLiveData<>();
    private final MutableLiveData<ArrayList<SimpleEventDTO>> top5Events = new MutableLiveData<>();
    private final MutableLiveData<ArrayList<LocationDTO>> locations = new MutableLiveData<>();
    private final MutableLiveData<ArrayList<String>> cities = new MutableLiveData<>();
    private final MutableLiveData<ArrayList<SimpleEventTypeDTO>> eventTypes = new MutableLiveData<>();
    private MutableLiveData<String> searchText = new MutableLiveData<>("");
    private MutableLiveData<String> selectedCity = new MutableLiveData<>("");
    private MutableLiveData<UUID> selectedEventType = new MutableLiveData<>();
    private MutableLiveData<String> selectedDate = new MutableLiveData<>("");
    private MutableLiveData<String> sortField = new MutableLiveData<>("");


    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();

    private static final String TAG = "ViewModel";
    public LiveData<ArrayList<SimpleEventDTO>> getTop5Events() {
        return top5Events;
    }

    public LiveData<ArrayList<SimpleEventDTO>> getEvents() {
        return allEvents;
    }

    public LiveData<PagedResponse<SimpleEventDTO>> getEventsPage() {
        return allEventsPage;
    }

    public LiveData<ArrayList<LocationDTO>> getLocations() {
        return locations;
    }
    public LiveData<ArrayList<String>> getCities() {
        return cities;
    }


    public LiveData<ArrayList<SimpleEventTypeDTO>> getEventTypes() {
        return eventTypes;
    }

    public LiveData<String> getSearchText() {
        return searchText;
    }

    public LiveData<String> getSelectedCity() {
        return selectedCity;
    }

    public LiveData<UUID> getSelectedEventType() {
        return selectedEventType;
    }

    public LiveData<String> getSelectedDate() {
        return selectedDate;
    }

    public LiveData<String> getSortField() {
        return sortField;
    }

    public void setSearchText(String text) {
        searchText.setValue(text);
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

    public void fetchAllEventsPage(
            String city,
            UUID eventTypeId,
            String time,
            String searchContent,
            String sortField,
            int page,
            int size
    ){

        Map<String,String> queryParams = new HashMap<>();

        queryParams.put("page", String.valueOf(page));
        queryParams.put("size", String.valueOf(size));
        if (city != null && !city.isEmpty()) {
            queryParams.put("city", city);
        }
        if (eventTypeId != null) {
            queryParams.put("eventTypeId", eventTypeId.toString());
        }
        if (time != null && !time.isEmpty()) {
            queryParams.put("time", time);
        }
        if (searchContent != null && !searchContent.isEmpty()) {
            queryParams.put("searchContent", searchContent);
        }
        if (sortField != null && !sortField.isEmpty()) {
            queryParams.put("sortField", sortField);
        }

        Call<PagedResponse<SimpleEventDTO>> call = ClientUtils.eventService.getEventsPage(
            queryParams
        );
        call.enqueue(new Callback<PagedResponse<SimpleEventDTO>>() {
            @Override
            public void onResponse(Call<PagedResponse<SimpleEventDTO>> call, Response<PagedResponse<SimpleEventDTO>> response) {
                if(response.isSuccessful()){
                    allEventsPage.postValue(response.body());
                    errorMessage.postValue(null);
                }else{
                    errorMessage.postValue("Failed to fetch events. Code: "+ response.code());
                }
            }

            @Override
            public void onFailure(Call<PagedResponse<SimpleEventDTO>> call, Throwable t) {
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
                    errorMessage.postValue("Failed to fetch top events. Code: "+ response.code());
                }
            }

            @Override
            public void onFailure(Call<ArrayList<SimpleEventDTO>> call, Throwable t) {
                errorMessage.postValue(t.getMessage());
            }
        });
    }


    public void fetchLocations(){
        Call<ArrayList<LocationDTO>> call = ClientUtils.locationService.getLocations();
        call.enqueue(new Callback<ArrayList<LocationDTO>>() {
            @Override
            public void onResponse(Call<ArrayList<LocationDTO>> call, Response<ArrayList<LocationDTO>> response) {
                if(response.isSuccessful()){
                    locations.postValue(response.body());
                    errorMessage.postValue(null);
                }else{
                    errorMessage.postValue("Failed to fetch locations. Code: "+ response.code());
                }
            }

            @Override
            public void onFailure(Call<ArrayList<LocationDTO>> call, Throwable t) {
                errorMessage.postValue(t.getMessage());
            }
        });
    }

    public void fetchCities(){
        Call<ArrayList<String>> call = ClientUtils.locationService.getCities();
        call.enqueue(new Callback<ArrayList<String>>() {
            @Override
            public void onResponse(Call<ArrayList<String>> call, Response<ArrayList<String>> response) {
                if(response.isSuccessful()){
                    cities.postValue(response.body());
                    errorMessage.postValue(null);
                }else{
                    errorMessage.postValue("Failed to fetch cities. Code: "+ response.code());
                }
            }

            @Override
            public void onFailure(Call<ArrayList<String>> call, Throwable t) {
                errorMessage.postValue(t.getMessage());
            }
        });
    }

    public void fetchEventTypes(){
        Call<ArrayList<SimpleEventTypeDTO>> call = ClientUtils.eventTypeService.getEventTypes();
        call.enqueue(new Callback<ArrayList<SimpleEventTypeDTO>>() {
            @Override
            public void onResponse(Call<ArrayList<SimpleEventTypeDTO>> call, Response<ArrayList<SimpleEventTypeDTO>> response) {
                if(response.isSuccessful()){
                    eventTypes.postValue(response.body());
                    errorMessage.postValue(null);
                }else{
                    errorMessage.postValue("Failed to fetch event types. Code: "+ response.code());
                }
            }

            @Override
            public void onFailure(Call<ArrayList<SimpleEventTypeDTO>> call, Throwable t) {
                errorMessage.postValue(t.getMessage());
            }
        });
    }
}
