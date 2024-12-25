package com.ftn.eventhopper.fragments.solutions.viewmodel;

import android.os.Bundle;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.navigation.NavController;

import com.ftn.eventhopper.R;
import com.ftn.eventhopper.clients.ClientUtils;
import com.ftn.eventhopper.clients.services.users.ProfileService;
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

    public void toggleFavorite() {
        SolutionDetailsDTO solutionDetails = getSolutionDetails().getValue();
        if (solutionDetails != null) {
            if (!solutionDetails.isFavorite())  {
                Call<Void> call = ClientUtils.profileService.addSolutionToFavorites(solutionDetails.getId());
                call.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (response.isSuccessful()) {
                            solutionDetails.setFavorite(true);
                            solutionDetailsLiveData.postValue(solutionDetails);
                        } else {
                            errorMessage.postValue("Failed to add solution to favorites. Code: " + response.code());
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        errorMessage.postValue(t.getMessage());
                    }
                });
            } else {
                Call<Void> call = ClientUtils.profileService.removeSolutionFromFavorites(solutionDetails.getId());
                call.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (response.isSuccessful()) {
                            solutionDetails.setFavorite(false);
                            solutionDetailsLiveData.postValue(solutionDetails);
                        } else {
                            errorMessage.postValue("Failed to remove solution from favorites. Code: " + response.code());
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        errorMessage.postValue(t.getMessage());
                    }
                });
            }
        }
    }

    public void goToProviderPage(NavController navController) {
        SolutionDetailsDTO solutionDetails = getSolutionDetails().getValue();
        if (solutionDetails != null && navController != null && solutionDetails.getProvider() != null) {
            Bundle bundle = new Bundle();
            bundle.putString("id", solutionDetails.getProvider().getId().toString());
            navController.navigate(R.id.action_to_provider_page, bundle);
        }

    }
}
