package com.ftn.eventhopper.fragments.solutions.services.viewmodels;

import android.os.Bundle;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ftn.eventhopper.clients.ClientUtils;
import com.ftn.eventhopper.shared.dtos.categories.CategoryDTO;
import com.ftn.eventhopper.shared.dtos.solutions.UpdateServiceDTO;

import java.util.ArrayList;
import java.util.UUID;
import java.util.stream.Collectors;

import lombok.Getter;
import lombok.Setter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ServiceEditViewModel extends ViewModel {
    @Getter
    @Setter
    private UUID serviceId = null;

    @Getter
    @Setter
    private UpdateServiceDTO serviceUpdateDTO = new UpdateServiceDTO();

    @Getter
    @Setter
    private UUID serviceCategoryId;

    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();
    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    private final MutableLiveData<Boolean> editedLiveData = new MutableLiveData<>(false);
    public LiveData<Boolean> getEdited() {
        return editedLiveData;
    }

    private final MutableLiveData<CategoryDTO> categoryLiveData = new MutableLiveData<>();
    public LiveData<CategoryDTO> getCategory() {
        return categoryLiveData;
    }

    public void fetchCategories(){
        Call<ArrayList<CategoryDTO>> call = ClientUtils.categoriesService.getApproved();
        call.enqueue(new Callback<ArrayList<CategoryDTO>>() {
            @Override
            public void onResponse(Call<ArrayList<CategoryDTO>> call, Response<ArrayList<CategoryDTO>> response) {
                if(response.isSuccessful()){
                    categoryLiveData.postValue(response.body().stream().filter(category -> category.getId().equals(serviceCategoryId)).findFirst().orElse(null));
                    errorMessage.postValue(null);
                }else{
                    errorMessage.postValue("Failed to fetch categories. Code: "+ response.code());
                }
            }

            @Override
            public void onFailure(Call<ArrayList<CategoryDTO>> call, Throwable t) {
                errorMessage.postValue(t.getMessage());
            }
        });
    }

    public void updateService(){
        Call<Void> call = ClientUtils.serviceService.update(serviceId, serviceUpdateDTO);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.isSuccessful()){
                    editedLiveData.postValue(true);
                    errorMessage.postValue(null);
                    serviceUpdateDTO = new UpdateServiceDTO();
                    serviceCategoryId = null;
                    serviceId = null;
                }else{
                    errorMessage.postValue("Failed to update service. Code: "+ response.code());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                errorMessage.postValue(t.getMessage());
            }
        });
    }

    public void setData(Bundle arguments) {
        serviceId = UUID.fromString(arguments.getString("serviceId"));
        serviceUpdateDTO.setName(arguments.getString("name"));
        serviceUpdateDTO.setDescription(arguments.getString("description"));
        serviceUpdateDTO.setDurationMinutes(arguments.getInt("duration"));
        serviceUpdateDTO.setReservationWindowDays(arguments.getInt("reservationWindow"));
        serviceUpdateDTO.setCancellationWindowDays(arguments.getInt("cancellationWindow"));
        serviceUpdateDTO.setVisible(arguments.getBoolean("visibility"));
        serviceUpdateDTO.setAvailable(arguments.getBoolean("availability"));
        serviceUpdateDTO.setAutoAccept(arguments.getBoolean("autoAccept"));
        serviceUpdateDTO.setEventTypesIds(arguments.getStringArrayList("eventTypes").stream().map(UUID::fromString).collect(Collectors.toCollection(ArrayList::new)));
        serviceUpdateDTO.setPictures(arguments.getStringArrayList("pictures"));
        serviceCategoryId = UUID.fromString(arguments.getString("categoryId"));

    }
}
