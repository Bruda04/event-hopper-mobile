package com.ftn.eventhopper.fragments.solutions.services.viewmodels;

import android.graphics.Bitmap;
import android.media.Image;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ftn.eventhopper.adapters.ImagePreviewAdapter;
import com.ftn.eventhopper.clients.ClientUtils;
import com.ftn.eventhopper.clients.ImageUtils;
import com.ftn.eventhopper.shared.dtos.categories.CategoryDTO;
import com.ftn.eventhopper.shared.dtos.categories.CreateCategorySuggestionDTO;
import com.ftn.eventhopper.shared.dtos.categories.CreatedCategorySuggestionDTO;
import com.ftn.eventhopper.shared.dtos.solutions.CreateServiceDTO;
import com.ftn.eventhopper.shared.dtos.solutions.ServiceManagementDTO;
import com.ftn.eventhopper.shared.responses.PagedResponse;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import lombok.Getter;
import lombok.Setter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ServiceCreationViewModel extends ViewModel {
    @Setter
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

    private final MutableLiveData<Boolean> createdLiveData = new MutableLiveData<>(false);
    public LiveData<Boolean> getCreated() {
        return createdLiveData;
    }

    @Getter
    @Setter
    private ArrayList<ImagePreviewAdapter.ImagePreviewItem> uploadedImages = new ArrayList<>();

    private boolean isCategorySuggested = false;
    @Setter
    @Getter
    private String suggestedCategoryName = "";

    public void createService() {
        if (isCategorySuggested) {
            CreateCategorySuggestionDTO createCategorySuggestion = new CreateCategorySuggestionDTO();
            createCategorySuggestion.setName(suggestedCategoryName);

            Call<CreatedCategorySuggestionDTO> call = ClientUtils.categoriesService.makeSuggestion(createCategorySuggestion);
            call.enqueue(new Callback<CreatedCategorySuggestionDTO>() {
                @Override
                public void onResponse(Call<CreatedCategorySuggestionDTO> call, Response<CreatedCategorySuggestionDTO> response) {
                    if(response.isSuccessful()){
                        service.setCategoryId(response.body().getId());
                        service.setEventTypesIds(new ArrayList<>());
                        isCategorySuggested = false;
                        suggestedCategoryName = "";
                        callServiceCreation();
                    }else{
                        errorMessage.postValue("Failed to suggest category. Code: "+ response.code());
                    }
                }

                @Override
                public void onFailure(Call<CreatedCategorySuggestionDTO> call, Throwable t) {
                    errorMessage.postValue("Failed to suggest category. Error: "+ t.getMessage());
                }
            });
        } else {
            callServiceCreation();
        }
    }

    private void callServiceCreation() {
        service.setPictures(new ArrayList<>());
        AtomicInteger remainingUploads = new AtomicInteger(uploadedImages.size());
        AtomicBoolean hasUploadFailed = new AtomicBoolean(false);

        for (ImagePreviewAdapter.ImagePreviewItem image : uploadedImages) {
            Call<String> call = ImageUtils.uploadImage(image.getBitmap());
            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    if(response.isSuccessful()){
                        service.getPictures().add(response.body());
                        if (remainingUploads.decrementAndGet() == 0 && !hasUploadFailed.get())  {
                            enqueueServiceCreation();
                        }

                    }else{
                        errorMessage.postValue("Failed to upload image. Code: "+ response.code());
                        hasUploadFailed.set(true);
                        service = new CreateServiceDTO();
                        uploadedImages.clear();
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    errorMessage.postValue("Failed to upload image. Error: "+ t.getMessage());
                    Log.e("Image upload failed", t.getMessage());
                    hasUploadFailed.set(true);
                    service = new CreateServiceDTO();
                    uploadedImages.clear();
                }
            });
        }
    }

    private void enqueueServiceCreation() {
        Call<Void> call = ClientUtils.serviceService.create(service);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.isSuccessful()){
                    errorMessage.postValue(null);
                    createdLiveData.postValue(true);

                }else{
                    errorMessage.postValue("Failed to create service. Code: "+ response.code());
                }
                service = new CreateServiceDTO();
                uploadedImages.clear();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                errorMessage.postValue("Failed to create service. Error: "+ t.getMessage());
                service = new CreateServiceDTO();
                uploadedImages.clear();
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

    public boolean isCategorySuggested() {
        return isCategorySuggested;
    }

    public void setCategorySuggested(boolean categorySuggested) {
        isCategorySuggested = categorySuggested;
    }

}
