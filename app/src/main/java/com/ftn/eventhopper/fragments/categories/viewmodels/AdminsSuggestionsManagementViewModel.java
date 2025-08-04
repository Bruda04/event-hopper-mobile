package com.ftn.eventhopper.fragments.categories.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ftn.eventhopper.clients.ClientUtils;
import com.ftn.eventhopper.shared.dtos.categories.CategoryDTO;
import com.ftn.eventhopper.shared.dtos.categories.CategorySuggestionDTO;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.UUID;

import okhttp3.ResponseBody;
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

    public void approveSuggestion(UUID id) {
        Call<ResponseBody> call = ClientUtils.categoriesService.approveCategory(id);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    fetchSuggestions();
                } else {
                    try {
                        String errorBody = response.errorBody().string();
                        JSONObject jsonObject = new JSONObject(errorBody);
                        String errorMessageString = jsonObject.getString("message");
                        errorMessage.postValue(errorMessageString);

                    } catch (Exception e) {
                        errorMessage.postValue("An unexpected error occurred.");
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                errorMessage.postValue(t.getMessage());
            }
        });
    }


    public void rejectSuggestion(UUID id, UUID selectedCategoryId) {
        Call<ResponseBody> call = ClientUtils.categoriesService.rejectCategory(id, selectedCategoryId);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    fetchSuggestions();
                } else {
                    try {
                        String errorBody = response.errorBody().string();
                        JSONObject jsonObject = new JSONObject(errorBody);
                        String errorMessageString = jsonObject.getString("message");
                        errorMessage.postValue(errorMessageString);

                    } catch (Exception e) {
                        errorMessage.postValue("An unexpected error occurred.");
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                errorMessage.postValue(t.getMessage());
            }
        });
    }
}
