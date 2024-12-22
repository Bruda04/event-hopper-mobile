package com.ftn.eventhopper.fragments.categories.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ftn.eventhopper.clients.ClientUtils;
import com.ftn.eventhopper.shared.dtos.categories.CategoryDTO;
import com.ftn.eventhopper.shared.dtos.categories.CategorySuggestionDTO;

import java.util.ArrayList;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminsSuggestionsManagementViewModel extends ViewModel {
    private final MutableLiveData<ArrayList<CategorySuggestionDTO>> categorySuggestionsLiveData = new MutableLiveData<>();
    private final MutableLiveData<ArrayList<CategoryDTO>> approvedCategoriesLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();

    public LiveData<ArrayList<CategorySuggestionDTO>> getCategorySuggestions() {
        return categorySuggestionsLiveData;
    }

    public LiveData<ArrayList<CategoryDTO>> getApprovedCategories() {
        return approvedCategoriesLiveData;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public void fetchSuggestions() {
        Call<ArrayList<CategorySuggestionDTO>> call = ClientUtils.categoriesService.getSuggestions();
        call.enqueue(new Callback<ArrayList<CategorySuggestionDTO>>() {
            @Override
            public void onResponse(Call<ArrayList<CategorySuggestionDTO>> call, Response<ArrayList<CategorySuggestionDTO>> response) {
                if (response.isSuccessful()) {
                    categorySuggestionsLiveData.postValue(response.body());

                } else {
                    errorMessage.postValue("Failed to fetch category suggestions. Code: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<ArrayList<CategorySuggestionDTO>> call, Throwable t) {
                errorMessage.postValue(t.getMessage());
            }
        });
    }



    public void rejectSuggestion(UUID id) {
    }

    public void fetchApprovedCategories() {
        Call<ArrayList<CategoryDTO>> call = ClientUtils.categoriesService.getApproved();
        call.enqueue(new Callback<ArrayList<CategoryDTO>>() {
            @Override
            public void onResponse(Call<ArrayList<CategoryDTO>> call, Response<ArrayList<CategoryDTO>> response) {
                if (response.isSuccessful()) {
                    approvedCategoriesLiveData.postValue(response.body());

                } else {
                    errorMessage.postValue("Failed to fetch approved categories. Code: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<ArrayList<CategoryDTO>> call, Throwable t) {
                errorMessage.postValue(t.getMessage());
            }
        });
    }
}
