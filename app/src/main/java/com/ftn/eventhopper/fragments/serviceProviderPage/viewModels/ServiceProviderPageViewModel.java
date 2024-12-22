package com.ftn.eventhopper.fragments.serviceProviderPage.viewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ftn.eventhopper.clients.ClientUtils;
import com.ftn.eventhopper.shared.dtos.users.serviceProvider.ServiceProviderDetailsDTO;

import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ServiceProviderPageViewModel extends ViewModel {
    private final MutableLiveData<ServiceProviderDetailsDTO> providerDetailsLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();

    public LiveData<ServiceProviderDetailsDTO> getProviderDetails() {
        return providerDetailsLiveData;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public void fetchProviderDetailsById(UUID solutionId) {
        Call<ServiceProviderDetailsDTO> call = ClientUtils.profileService.getServiceProviderDetailsById(solutionId);
        call.enqueue(new Callback<ServiceProviderDetailsDTO>() {
            @Override
            public void onResponse(Call<ServiceProviderDetailsDTO> call, Response<ServiceProviderDetailsDTO> response) {
                if (response.isSuccessful()) {
                    providerDetailsLiveData.postValue(response.body());
                } else {
                    errorMessage.postValue("Failed to fetch provider. Code: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<ServiceProviderDetailsDTO> call, Throwable t) {
                errorMessage.postValue(t.getMessage());
            }
        });
    }

}
