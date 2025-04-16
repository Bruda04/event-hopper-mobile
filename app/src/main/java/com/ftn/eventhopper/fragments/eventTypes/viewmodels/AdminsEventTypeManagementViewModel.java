package com.ftn.eventhopper.fragments.eventTypes.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ftn.eventhopper.clients.ClientUtils;
import com.ftn.eventhopper.shared.dtos.categories.SimpleCategoryDTO;
import com.ftn.eventhopper.shared.dtos.eventTypes.CreateEventTypeDTO;
import com.ftn.eventhopper.shared.dtos.eventTypes.EventTypeManagementDTO;
import com.ftn.eventhopper.shared.dtos.eventTypes.SimpleEventTypeDTO;
import com.ftn.eventhopper.shared.dtos.eventTypes.UpdateEventTypeDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminsEventTypeManagementViewModel extends ViewModel {
    private final MutableLiveData<ArrayList<SimpleEventTypeDTO>> eventTypes = new MutableLiveData<>();
    private final MutableLiveData<ArrayList<SimpleCategoryDTO>> categories = new MutableLiveData<>();

    private final MutableLiveData<Boolean> dataLoaded = new MutableLiveData<>(Boolean.FALSE);

    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();

    public LiveData<ArrayList<SimpleEventTypeDTO>> getEventTypes() {
        return eventTypes;
    }

    public LiveData<ArrayList<SimpleCategoryDTO>> getCategories(){
        return categories;
    }

    public LiveData<Boolean> isLoaded(){
        return dataLoaded;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public void fetchEventTypes() {
        dataLoaded.postValue(Boolean.FALSE);
        Call<EventTypeManagementDTO> call = ClientUtils.eventTypeService.getEventTypes();
        call.enqueue(new Callback<EventTypeManagementDTO>() {
            @Override
            public void onResponse(Call<EventTypeManagementDTO> call, Response<EventTypeManagementDTO> response) {
                if (response.isSuccessful()) {
                    EventTypeManagementDTO res = response.body();
                    categories.postValue((ArrayList<SimpleCategoryDTO>) res.getAllCategories());
                    eventTypes.postValue((ArrayList<SimpleEventTypeDTO>) res.getEventTypes());
                    dataLoaded.postValue(Boolean.TRUE);
                    errorMessage.postValue(null);
                } else {
                    errorMessage.postValue("Failed to fetch approved categories. Code: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<EventTypeManagementDTO> call, Throwable t) {
                errorMessage.postValue(t.getMessage());
            }
        });
    }

    public void deleteEventType(UUID id) {
        Call<Void> call = ClientUtils.eventTypeService.deleteEventType(id);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    fetchEventTypes();
                    errorMessage.postValue(null);
                } else {
                    errorMessage.postValue("Failed to delete event type. Code: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                errorMessage.postValue(t.getMessage());
            }
        });
    }

    public void updateEventType(UUID id, String description, List<SimpleCategoryDTO> suggestedCategories) {
        UpdateEventTypeDTO updateEventTypeDTO  = new UpdateEventTypeDTO();
        updateEventTypeDTO.setDescription(description);
        updateEventTypeDTO.setSuggestedCategories(suggestedCategories);

        Call<Void> call = ClientUtils.eventTypeService.updateEventType(id, updateEventTypeDTO);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    fetchEventTypes();
                    errorMessage.postValue(null);
                } else {
                    errorMessage.postValue("Failed to edit event type. Code: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                errorMessage.postValue(t.getMessage());
            }
        });
    }

    public void createEventType(String eventTypeName, String eventTypeDescription) {
        CreateEventTypeDTO createEventTypeDTO = new CreateEventTypeDTO();
        createEventTypeDTO.setName(eventTypeName);
        createEventTypeDTO.setDescription(eventTypeDescription);

        Call<Void> call = ClientUtils.eventTypeService.addEventType(createEventTypeDTO);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    fetchEventTypes();
                    errorMessage.postValue(null);
                } else {
                    errorMessage.postValue("Failed to create event type. Code: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                errorMessage.postValue(t.getMessage());
            }
        });
    }

}
