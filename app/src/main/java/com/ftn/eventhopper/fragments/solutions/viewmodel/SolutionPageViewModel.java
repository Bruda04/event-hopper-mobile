package com.ftn.eventhopper.fragments.solutions.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ftn.eventhopper.clients.ClientUtils;
import com.ftn.eventhopper.shared.dtos.solutions.SolutionDetailsDTO;

import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SolutionPageViewModel extends ViewModel {
    private final MutableLiveData<SolutionDetailsDTO> solutionDetailsLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();

    public LiveData<SolutionDetailsDTO> getSolutionDetails() {
        return solutionDetailsLiveData;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public void fetchSolutionDetailsById(UUID solutionId) {
        Call<SolutionDetailsDTO> call = ClientUtils.productService.getSolutionDetailsById(solutionId);
        call.enqueue(new Callback<SolutionDetailsDTO>() {
            @Override
            public void onResponse(Call<SolutionDetailsDTO> call, Response<SolutionDetailsDTO> response) {
                if (response.isSuccessful()) {
                    solutionDetailsLiveData.postValue(response.body());
                } else {
                    errorMessage.postValue("Failed to fetch products. Code: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<SolutionDetailsDTO> call, Throwable t) {
                errorMessage.postValue(t.getMessage());
            }
        });
    }
}
