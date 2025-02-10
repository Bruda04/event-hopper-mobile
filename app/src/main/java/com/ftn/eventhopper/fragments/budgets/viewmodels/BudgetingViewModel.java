package com.ftn.eventhopper.fragments.budgets.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ftn.eventhopper.clients.ClientUtils;
import com.ftn.eventhopper.shared.dtos.budgets.BudgetManagementDTO;
import com.ftn.eventhopper.shared.dtos.categories.CategoryDTO;
import com.ftn.eventhopper.shared.dtos.categories.CategorySuggestionDTO;

import java.util.ArrayList;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BudgetingViewModel extends ViewModel {
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    private final MutableLiveData<BudgetManagementDTO> budgetLiveData = new MutableLiveData<>();

    public LiveData<BudgetManagementDTO> getBudget() {
        return budgetLiveData;
    }

    public void fetchBudget(UUID eventId) {
        Call<BudgetManagementDTO> call = ClientUtils.budgetService.getManagement(eventId);
        call.enqueue(new Callback<BudgetManagementDTO>() {
            @Override
            public void onResponse(Call<BudgetManagementDTO> call, Response<BudgetManagementDTO> response) {
                if (response.isSuccessful()) {
                    budgetLiveData.postValue(response.body());

                } else {
                    errorMessage.postValue("Failed to fetch budget data. Code: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<BudgetManagementDTO> call, Throwable t) {
                errorMessage.postValue(t.getMessage());
            }
        });
    }


}
