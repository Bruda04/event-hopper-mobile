package com.ftn.eventhopper.fragments.solutions.services.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.ftn.eventhopper.clients.ClientUtils;
import com.ftn.eventhopper.shared.dtos.categories.CategoryDTO;
import com.ftn.eventhopper.shared.dtos.events.SimpleEventDTO;
import com.ftn.eventhopper.shared.dtos.solutions.ServiceManagementDTO;
import com.ftn.eventhopper.shared.models.Service;
import com.ftn.eventhopper.shared.responses.PagedResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

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
    private final MutableLiveData<Map<String, String>> filtersLiveData = new MutableLiveData<>();
    public LiveData<Map<String, String>> getFilters() {
        return filtersLiveData;
    }
    private final MutableLiveData<ArrayList<CategoryDTO>> categoriesLiveData = new MutableLiveData<>();

    public LiveData<ArrayList<CategoryDTO>> getCategories() {
        return categoriesLiveData;
    }
    public void fetchAllServicesPage(
            int page,
            int size
    ){

        Map<String,String> queryParams = new HashMap<>();

        queryParams.put("page", String.valueOf(page));
        queryParams.put("size", String.valueOf(size));

        if (filtersLiveData.getValue() != null) {
            queryParams.putAll(filtersLiveData.getValue());
        }

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

    public void setFilters(Map<String, String> filters) {
        filtersLiveData.postValue(filters);
    }

    public void fetchCategories(){
        Call<ArrayList<CategoryDTO>> call = ClientUtils.categoriesService.getApproved();
        call.enqueue(new Callback<ArrayList<CategoryDTO>>() {
            @Override
            public void onResponse(Call<ArrayList<CategoryDTO>> call, Response<ArrayList<CategoryDTO>> response) {
                if(response.isSuccessful()){
                    categoriesLiveData.postValue(response.body());
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


    public void deleteService(UUID id) {
        Call<Void> call = ClientUtils.serviceService.deleteService(id);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    fetchAllServicesPage(0, 10);

                    errorMessage.postValue(null);
                } else {
                    errorMessage.postValue("Failed to delete service. Code: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                errorMessage.postValue(t.getMessage());
            }
        });
    }
}
