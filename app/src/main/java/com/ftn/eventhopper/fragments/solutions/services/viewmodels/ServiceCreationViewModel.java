package com.ftn.eventhopper.fragments.solutions.services.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ftn.eventhopper.clients.ClientUtils;
import com.ftn.eventhopper.shared.dtos.categories.CategoryDTO;
import com.ftn.eventhopper.shared.dtos.solutions.CreateServiceDTO;
import com.ftn.eventhopper.shared.dtos.solutions.ServiceManagementDTO;
import com.ftn.eventhopper.shared.responses.PagedResponse;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ServiceCreationViewModel extends ViewModel {
    private CreateServiceDTO service = new CreateServiceDTO();
    public CreateServiceDTO getService() { return service;}

    private final MutableLiveData<ArrayList<CategoryDTO>> categoriesLiveData = new MutableLiveData<>();
    public LiveData<ArrayList<CategoryDTO>> getCategories() {
        return categoriesLiveData;
    }

    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();
    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public void createService() {
        Call<Void> call = ClientUtils.serviceService.create(service);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.isSuccessful()){
                    errorMessage.postValue(null);
                }else{
                    errorMessage.postValue("Failed to fetch events. Code: "+ response.code());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                errorMessage.postValue("Failed to fetch events. Error: "+ t.getMessage());
            }
        });
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

    public void setService(CreateServiceDTO createServiceDTO) {
        this.service = createServiceDTO;
    }
}
