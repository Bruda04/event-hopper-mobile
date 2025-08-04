package com.ftn.eventhopper.fragments.categories.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ftn.eventhopper.clients.ClientUtils;
import com.ftn.eventhopper.shared.dtos.categories.CategoryDTO;
import com.ftn.eventhopper.shared.dtos.categories.CreateCategoryDTO;
import com.ftn.eventhopper.shared.dtos.categories.UpdateCategoryDTO;
import com.ftn.eventhopper.shared.models.categories.CategoryStatus;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.UUID;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminsCategoriesManagementViewModel extends ViewModel {
    private final MutableLiveData<ArrayList<CategoryDTO>> approvedCategoriesLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();

    public LiveData<ArrayList<CategoryDTO>> getApprovedCategories() {
        return approvedCategoriesLiveData;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public void fetchApprovedCategories() {
        Call<ArrayList<CategoryDTO>> call = ClientUtils.categoriesService.getApproved();
        call.enqueue(new Callback<ArrayList<CategoryDTO>>() {
            @Override
            public void onResponse(Call<ArrayList<CategoryDTO>> call, Response<ArrayList<CategoryDTO>> response) {
                if (response.isSuccessful()) {
                    approvedCategoriesLiveData.postValue(response.body());
                    errorMessage.postValue(null);

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

    public void deleteCategory(UUID id) {
        Call<Void> call = ClientUtils.categoriesService.deleteCategory(id);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    fetchApprovedCategories();
                    errorMessage.postValue(null);
                } else {
                    errorMessage.postValue("Failed to delete category. Code: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                errorMessage.postValue(t.getMessage());
            }
        });
    }

    public void editCategory(UUID id, String name, String description, ArrayList<UUID> eventTypesIds, CategoryStatus status) {
        UpdateCategoryDTO updateCategoryDTO  = new UpdateCategoryDTO();
        updateCategoryDTO.setName(name);
        updateCategoryDTO.setDescription(description);
        updateCategoryDTO.setEventTypesIds(eventTypesIds);
        updateCategoryDTO.setStatus(status);

        Call<ResponseBody> call = ClientUtils.categoriesService.updateCategory(id, updateCategoryDTO);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    fetchApprovedCategories();
                    errorMessage.postValue(null);
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

    public void createCategory(String categoryName, String categoryDescription) {
        CreateCategoryDTO createCategoryDTO = new CreateCategoryDTO();
        createCategoryDTO.setName(categoryName);
        createCategoryDTO.setDescription(categoryDescription);

        Call<Void> call = ClientUtils.categoriesService.addCategory(createCategoryDTO);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    fetchApprovedCategories();
                    errorMessage.postValue(null);
                } else {
                    errorMessage.postValue("Failed to create category. Code: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                errorMessage.postValue(t.getMessage());
            }
        });
    }
}
