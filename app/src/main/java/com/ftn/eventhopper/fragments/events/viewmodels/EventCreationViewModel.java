package com.ftn.eventhopper.fragments.events.viewmodels;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ftn.eventhopper.adapters.ImagePreviewAdapter;
import com.ftn.eventhopper.clients.ClientUtils;
import com.ftn.eventhopper.clients.ImageUtils;
import com.ftn.eventhopper.shared.dtos.eventTypes.EventTypeManagementDTO;
import com.ftn.eventhopper.shared.dtos.eventTypes.SimpleEventTypeDTO;
import com.ftn.eventhopper.shared.dtos.events.CreateAgendaActivityDTO;
import com.ftn.eventhopper.shared.dtos.events.CreateEventDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import lombok.Getter;
import lombok.Setter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EventCreationViewModel extends ViewModel {
    @Setter
    @Getter
    private CreateEventDTO event = new CreateEventDTO();

    @Setter
    @Getter
    private MutableLiveData<ArrayList<CreateAgendaActivityDTO>> agendas= new MutableLiveData<>();
    private final MutableLiveData<List<SimpleEventTypeDTO>> eventTypesLiveData = new MutableLiveData<>();
    public LiveData<List<SimpleEventTypeDTO>> getEventTypes() {
        return eventTypesLiveData;
    }


    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();
    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    private final MutableLiveData<Boolean> createdLiveData = new MutableLiveData<>(false);
    public LiveData<Boolean> getCreated() {
        return createdLiveData;
    }

    @Getter
    @Setter
    private ArrayList<ImagePreviewAdapter.ImagePreviewItem> uploadedImages = new ArrayList<>();


    //images
    public void uploadImages() {
        event.setPicture("");
        AtomicInteger remainingUploads = new AtomicInteger(uploadedImages.size());
        AtomicBoolean hasUploadFailed = new AtomicBoolean(false);

        for (ImagePreviewAdapter.ImagePreviewItem image : uploadedImages) {
            Call<String> call = ImageUtils.uploadImage(image.getBitmap());
            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    if(response.isSuccessful()){
                        event.setPicture(response.body());
                        Log.d("IMPORTANT IMAGE", event.getPicture());
                        if (remainingUploads.decrementAndGet() == 0 && !hasUploadFailed.get())  {
                            return;
                        }

                    }else{
                        errorMessage.postValue("Failed to upload image. Code: "+ response.code());
                        hasUploadFailed.set(true);
                        uploadedImages.clear();
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    errorMessage.postValue("Failed to upload image. Error: "+ t.getMessage());
                    Log.e("Image upload failed", t.getMessage());
                    hasUploadFailed.set(true);
                    uploadedImages.clear();
                }
            });
        }
    }

    public void createEvent() {
        event.setAgendaActivities(getAgendas().getValue());

        Call<Void> call = ClientUtils.eventService.create(event);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.isSuccessful()){
                    errorMessage.postValue(null);
                    createdLiveData.postValue(true);
                }else{
                    errorMessage.postValue("Failed to create event. Code: "+ response.code());
                }
                event = new CreateEventDTO();
                uploadedImages.clear();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                errorMessage.postValue("Failed to create event. Error: "+ t.getMessage());
                event = new CreateEventDTO();
                uploadedImages.clear();
            }
        });
    }

    public void fetchEventTypes(){
        Call<EventTypeManagementDTO> call = ClientUtils.eventTypeService.getEventTypes();
        call.enqueue(new Callback<EventTypeManagementDTO>() {
            @Override
            public void onResponse(Call<EventTypeManagementDTO> call, Response<EventTypeManagementDTO> response) {
                if(response.isSuccessful()){
                    assert response.body() != null;
                    List<SimpleEventTypeDTO> eventTypesDTOs = response.body().getEventTypes();
                    SimpleEventTypeDTO empty = new SimpleEventTypeDTO();
                    empty.setName("All");
                    empty.setDescription("None are selected");
                    eventTypesDTOs.add(empty);
                    eventTypesLiveData.postValue(eventTypesDTOs);
                    errorMessage.postValue(null);
                }else{
                    errorMessage.postValue("Failed to fetch event types. Code: "+ response.code());
                }
            }

            @Override
            public void onFailure(Call<EventTypeManagementDTO> call, Throwable t) {
                errorMessage.postValue(t.getMessage());
            }
        });
    }

    public void setCreated(boolean b) {
        createdLiveData.postValue(b);
    }

    public void reset() {
        event = new CreateEventDTO();
        uploadedImages.clear();
        setCreated(false);
        errorMessage.postValue(null);
    }
}
