package com.ftn.eventhopper.fragments.solutions.products.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ftn.eventhopper.clients.ClientUtils;
import com.ftn.eventhopper.shared.dtos.categories.CategoryDTO;
import com.ftn.eventhopper.shared.dtos.solutions.ProductForManagementDTO;
import com.ftn.eventhopper.shared.responses.PagedResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import lombok.Getter;
import lombok.Setter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PupsProductsViewModel extends ViewModel {
    private final MutableLiveData<PagedResponse<ProductForManagementDTO>> productsPageLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();

    public LiveData<PagedResponse<ProductForManagementDTO>> getProductsPage() {
        return productsPageLiveData;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }
    private final MutableLiveData<Map<String, String>> filtersLiveData = new MutableLiveData<>();
    public LiveData<Map<String, String>> getFilters() {
        return filtersLiveData;
    }

    @Getter
    @Setter
    private String searchText;

    private final MutableLiveData<ArrayList<CategoryDTO>> categoriesLiveData = new MutableLiveData<>();

    public LiveData<ArrayList<CategoryDTO>> getCategories() {
        return categoriesLiveData;
    }
    public void fetchAllProductsPage(
            int page,
            int size
    ){

        Map<String,String> queryParams = new HashMap<>();

        queryParams.put("page", String.valueOf(page));
        queryParams.put("size", String.valueOf(size));

        if (filtersLiveData.getValue() != null) {
            queryParams.putAll(filtersLiveData.getValue());
        }

        if (!queryParams.containsKey("sortField")) {
            queryParams.put("sortField", "name");
        }

        if (searchText != null && !searchText.isEmpty()) {
            queryParams.put("searchContent", searchText);
        }

        Call<PagedResponse<ProductForManagementDTO>> call = ClientUtils.productService.getAllForManagement(
                queryParams
        );
        call.enqueue(new Callback<PagedResponse<ProductForManagementDTO>>() {
            @Override
            public void onResponse(Call<PagedResponse<ProductForManagementDTO>> call, Response<PagedResponse<ProductForManagementDTO>> response) {
                if(response.isSuccessful()){
                    productsPageLiveData.postValue(response.body());
                    errorMessage.postValue(null);
                }else{
                    errorMessage.postValue("Failed to fetch products. Code: "+ response.code());
                }
            }

            @Override
            public void onFailure(Call<PagedResponse<ProductForManagementDTO>> call, Throwable t) {
                errorMessage.postValue(t.getMessage());
            }
        });
    }

    public void setFilters(Map<String, String> filters) {
        filtersLiveData.postValue(filters);
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

    public void deleteProduct(UUID id) {
        Call<Void> call = ClientUtils.productService.delete(id);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    fetchAllProductsPage(0, 10);

                    errorMessage.postValue(null);
                } else {
                    errorMessage.postValue("Failed to delete product. Code: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                errorMessage.postValue(t.getMessage());
            }
        });
    }
}
