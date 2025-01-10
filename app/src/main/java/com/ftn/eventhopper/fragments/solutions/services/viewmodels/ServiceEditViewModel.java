package com.ftn.eventhopper.fragments.solutions.services.viewmodels;

import android.os.Bundle;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ftn.eventhopper.adapters.ImagePreviewAdapter;
import com.ftn.eventhopper.clients.ClientUtils;
import com.ftn.eventhopper.clients.ImageUtils;
import com.ftn.eventhopper.shared.dtos.categories.CategoryDTO;
import com.ftn.eventhopper.shared.dtos.solutions.CreateServiceDTO;
import com.ftn.eventhopper.shared.dtos.solutions.UpdateServiceDTO;

import java.util.ArrayList;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import lombok.Getter;
import lombok.Setter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ServiceEditViewModel extends ViewModel {
    @Getter
    @Setter
    private UUID serviceId = null;

    @Getter
    @Setter
    private UpdateServiceDTO serviceUpdateDTO = new UpdateServiceDTO();

    @Getter
    @Setter
    private UUID serviceCategoryId;

    @Getter
    @Setter
    private ArrayList<ImagePreviewAdapter.ImagePreviewItem> uploadedImages = new ArrayList<>();

    @Getter
    @Setter
    private ArrayList<ImagePreviewAdapter.ImagePreviewItem> oldImages;

    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();
    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    private final MutableLiveData<Boolean> editedLiveData = new MutableLiveData<>(false);
    public LiveData<Boolean> getEdited() {
        return editedLiveData;
    }

    private final MutableLiveData<CategoryDTO> categoryLiveData = new MutableLiveData<>();
    public LiveData<CategoryDTO> getCategory() {
        return categoryLiveData;
    }

    public void fetchCategories(){
        Call<ArrayList<CategoryDTO>> call = ClientUtils.categoriesService.getApproved();
        call.enqueue(new Callback<ArrayList<CategoryDTO>>() {
            @Override
            public void onResponse(Call<ArrayList<CategoryDTO>> call, Response<ArrayList<CategoryDTO>> response) {
                if(response.isSuccessful()){
                    categoryLiveData.postValue(response.body().stream().filter(category -> category.getId().equals(serviceCategoryId)).findFirst().orElse(null));
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

    public void updateService(){
        AtomicInteger remainingUploads = new AtomicInteger(uploadedImages.size());
        AtomicBoolean hasUploadFailed = new AtomicBoolean(false);

        AtomicInteger remainingDeletions = new AtomicInteger(serviceUpdateDTO.getPictures().size() - oldImages.size());
        AtomicBoolean hasDeletionFailed = new AtomicBoolean(false);

        for (String picture : serviceUpdateDTO.getPictures()) {
            if (!oldImages.stream().map(ImagePreviewAdapter.ImagePreviewItem::getImageUrl).collect(Collectors.toList()).contains(picture)) {
                Call<Void> call = ImageUtils.deleteImage(picture);
                call.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (!response.isSuccessful()) {
                            errorMessage.postValue("Failed to delete image. Code: " + response.code());
                            hasDeletionFailed.set(true);
                            serviceUpdateDTO = new UpdateServiceDTO();
                            uploadedImages.clear();
                            oldImages.clear();
                        } else {
                            if (remainingDeletions.decrementAndGet() == 0 && !hasDeletionFailed.get() && remainingUploads.get() == 0 && !hasUploadFailed.get()) {
                                enqueueUpdate();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        errorMessage.postValue("Failed to delete image. Error: " + t.getMessage());
                        Log.e("Image deletion failed", t.getMessage());
                        hasDeletionFailed.set(true);
                        serviceUpdateDTO = new UpdateServiceDTO();
                        uploadedImages.clear();
                        oldImages.clear();
                    }
                });
            }
        }

        serviceUpdateDTO.setPictures(oldImages.stream().map(ImagePreviewAdapter.ImagePreviewItem::getImageUrl).collect(Collectors.toCollection(ArrayList::new)));
        for (ImagePreviewAdapter.ImagePreviewItem image : uploadedImages) {
            Call<String> call = ImageUtils.uploadImage(image.getBitmap());
            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    if(response.isSuccessful()){
                        serviceUpdateDTO.getPictures().add(response.body());
                        if (remainingUploads.decrementAndGet() == 0 && !hasUploadFailed.get() && remainingDeletions.get() == 0 && !hasDeletionFailed.get())  {
                            enqueueUpdate();
                        }

                    }else{
                        errorMessage.postValue("Failed to upload image. Code: "+ response.code());
                        hasUploadFailed.set(true);
                        serviceUpdateDTO = new UpdateServiceDTO();
                        uploadedImages.clear();
                        oldImages.clear();
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    errorMessage.postValue("Failed to upload image. Error: "+ t.getMessage());
                    Log.e("Image upload failed", t.getMessage());
                    hasUploadFailed.set(true);
                    serviceUpdateDTO = new UpdateServiceDTO();
                    uploadedImages.clear();
                }
            });
        }
    }

    private void enqueueUpdate() {
        Call<Void> call = ClientUtils.serviceService.update(serviceId, serviceUpdateDTO);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.isSuccessful()){
                    editedLiveData.postValue(true);
                    errorMessage.postValue(null);
                    serviceUpdateDTO = new UpdateServiceDTO();
                    serviceCategoryId = null;
                    serviceId = null;
                    uploadedImages.clear();
                    oldImages.clear();
                }else{
                    errorMessage.postValue("Failed to update service. Code: "+ response.code());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                errorMessage.postValue(t.getMessage());
            }
        });
    }

    public void setData(Bundle arguments) {
        serviceId = UUID.fromString(arguments.getString("serviceId"));
        serviceUpdateDTO.setName(arguments.getString("name"));
        serviceUpdateDTO.setDescription(arguments.getString("description"));
        serviceUpdateDTO.setDurationMinutes(arguments.getInt("duration"));
        serviceUpdateDTO.setReservationWindowDays(arguments.getInt("reservationWindow"));
        serviceUpdateDTO.setCancellationWindowDays(arguments.getInt("cancellationWindow"));
        serviceUpdateDTO.setVisible(arguments.getBoolean("visibility"));
        serviceUpdateDTO.setAvailable(arguments.getBoolean("availability"));
        serviceUpdateDTO.setAutoAccept(arguments.getBoolean("autoAccept"));
        serviceUpdateDTO.setEventTypesIds(arguments.getStringArrayList("eventTypes").stream().map(UUID::fromString).collect(Collectors.toCollection(ArrayList::new)));
        serviceUpdateDTO.setPictures(arguments.getStringArrayList("pictures"));
        serviceCategoryId = UUID.fromString(arguments.getString("categoryId"));
        oldImages = new ArrayList<>();
        for (String imageUrl : serviceUpdateDTO.getPictures()) {
            oldImages.add(new ImagePreviewAdapter.ImagePreviewItem(imageUrl));
        }

    }
}
