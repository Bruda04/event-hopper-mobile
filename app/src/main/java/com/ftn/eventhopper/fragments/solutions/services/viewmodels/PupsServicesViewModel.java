package com.ftn.eventhopper.fragments.solutions.services.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ftn.eventhopper.clients.ClientUtils;
import com.ftn.eventhopper.shared.dtos.events.SimpleEventDTO;
import com.ftn.eventhopper.shared.dtos.solutions.ServiceManagementDTO;
import com.ftn.eventhopper.shared.models.Service;
import com.ftn.eventhopper.shared.responses.PagedResponse;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PupsServicesViewModel extends ViewModel {
    private final MutableLiveData<PagedResponse<ServiceManagementDTO>> servicesPageLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();

    public LiveData<PagedResponse<ServiceManagementDTO>> getServicesPage() {
        return servicesPageLiveData;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public void fetchAllServicesPage(
            int page,
            int size
    ){

        Map<String,String> queryParams = new HashMap<>();

        queryParams.put("page", String.valueOf(page));
        queryParams.put("size", String.valueOf(size));

        Call<PagedResponse<ServiceManagementDTO>> call = ClientUtils.serviceService.getAllForManagement(
                queryParams
        );
        call.enqueue(new Callback<PagedResponse<ServiceManagementDTO>>() {
            @Override
            public void onResponse(Call<PagedResponse<ServiceManagementDTO>> call, Response<PagedResponse<ServiceManagementDTO>> response) {
                if(response.isSuccessful()){
                    servicesPageLiveData.postValue(response.body());
                    errorMessage.postValue(null);
                }else{
                    errorMessage.postValue("Failed to fetch events. Code: "+ response.code());
                }
            }

            @Override
            public void onFailure(Call<PagedResponse<ServiceManagementDTO>> call, Throwable t) {
                errorMessage.postValue(t.getMessage());
            }
        });
    }
}
