package com.ftn.eventhopper.fragments.solutions.products.viewmodels;

import android.os.Bundle;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ftn.eventhopper.adapters.ImagePreviewAdapter;
import com.ftn.eventhopper.clients.ClientUtils;
import com.ftn.eventhopper.clients.ImageUtils;
import com.ftn.eventhopper.shared.dtos.categories.CategoryDTO;
import com.ftn.eventhopper.shared.dtos.solutions.UpdateProductDTO;

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

public class ProductEditViewModel  extends ViewModel {
    @Getter
    @Setter
    private UUID productId = null;

    @Getter
    @Setter
    private UpdateProductDTO productUpdateDTO = new UpdateProductDTO();

    @Getter
    @Setter
    private UUID productCategoryId;

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
                    categoryLiveData.postValue(response.body().stream().filter(category -> category.getId().equals(productCategoryId)).findFirst().orElse(null));
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

    public void updateProduct(){
        AtomicInteger remainingUploads = new AtomicInteger(uploadedImages.size());
        AtomicBoolean hasUploadFailed = new AtomicBoolean(false);

        AtomicInteger remainingDeletions = new AtomicInteger(productUpdateDTO.getPictures().size() - oldImages.size());
        AtomicBoolean hasDeletionFailed = new AtomicBoolean(false);

        if (remainingUploads.get() == 0 && remainingDeletions.get() == 0) {
            enqueueUpdate();
            return;
        }

        for (String picture : productUpdateDTO.getPictures()) {
            if (!oldImages.stream().map(ImagePreviewAdapter.ImagePreviewItem::getImageUrl).collect(Collectors.toList()).contains(picture)) {
                Call<Void> call = ImageUtils.deleteImage(picture);
                call.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (!response.isSuccessful()) {
                            errorMessage.postValue("Failed to delete image. Code: " + response.code());
                            hasDeletionFailed.set(true);
                            productUpdateDTO = new UpdateProductDTO();
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
                        productUpdateDTO = new UpdateProductDTO();
                        uploadedImages.clear();
                        oldImages.clear();
                    }
                });
            }
        }

        productUpdateDTO.setPictures(oldImages.stream().map(ImagePreviewAdapter.ImagePreviewItem::getImageUrl).collect(Collectors.toCollection(ArrayList::new)));
        for (ImagePreviewAdapter.ImagePreviewItem image : uploadedImages) {
            Call<String> call = ImageUtils.uploadImage(image.getBitmap());
            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    if(response.isSuccessful()){
                        productUpdateDTO.getPictures().add(response.body());
                        if (remainingUploads.decrementAndGet() == 0 && !hasUploadFailed.get() && remainingDeletions.get() == 0 && !hasDeletionFailed.get())  {
                            enqueueUpdate();
                        }

                    }else{
                        errorMessage.postValue("Failed to upload image. Code: "+ response.code());
                        hasUploadFailed.set(true);
                        productUpdateDTO = new UpdateProductDTO();
                        uploadedImages.clear();
                        oldImages.clear();
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    errorMessage.postValue("Failed to upload image. Error: "+ t.getMessage());
                    Log.e("Image upload failed", t.getMessage());
                    hasUploadFailed.set(true);
                    productUpdateDTO = new UpdateProductDTO();
                    uploadedImages.clear();
                }
            });
        }
    }

    private void enqueueUpdate() {
        Call<Void> call = ClientUtils.productService.update(productId, productUpdateDTO);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.isSuccessful()){
                    editedLiveData.postValue(true);
                    errorMessage.postValue(null);
                    productUpdateDTO = new UpdateProductDTO();
                    productCategoryId = null;
                    productId = null;
                    uploadedImages.clear();
                    oldImages.clear();
                }else{
                    errorMessage.postValue("Failed to update product. Code: "+ response.code());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                errorMessage.postValue(t.getMessage());
            }
        });
    }

    public void setData(Bundle arguments) {
        productId = UUID.fromString(arguments.getString("productId"));
        productUpdateDTO.setName(arguments.getString("name"));
        productUpdateDTO.setDescription(arguments.getString("description"));
        productUpdateDTO.setVisible(arguments.getBoolean("visibility"));
        productUpdateDTO.setAvailable(arguments.getBoolean("availability"));
        productUpdateDTO.setEventTypesIds(arguments.getStringArrayList("eventTypes").stream().map(UUID::fromString).collect(Collectors.toCollection(ArrayList::new)));
        productUpdateDTO.setPictures(arguments.getStringArrayList("pictures"));
        productCategoryId = UUID.fromString(arguments.getString("categoryId"));
        oldImages = new ArrayList<>();
        for (String imageUrl : productUpdateDTO.getPictures()) {
            oldImages.add(new ImagePreviewAdapter.ImagePreviewItem(imageUrl));
        }

    }

    public void reset() {
        productId = null;
        productUpdateDTO = new UpdateProductDTO();
        productCategoryId = null;
        uploadedImages.clear();
        oldImages.clear();
        errorMessage.postValue(null);
        editedLiveData.postValue(false);
    }

    public void setEdited(boolean b) {
        editedLiveData.postValue(b);
    }
}
