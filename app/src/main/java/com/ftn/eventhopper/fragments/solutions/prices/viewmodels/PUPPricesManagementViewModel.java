package com.ftn.eventhopper.fragments.solutions.prices.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ftn.eventhopper.clients.ClientUtils;
import com.ftn.eventhopper.shared.dtos.prices.PriceManagementDTO;
import com.ftn.eventhopper.shared.dtos.prices.UpdatePriceDTO;

import java.util.ArrayList;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PUPPricesManagementViewModel extends ViewModel {
    private final MutableLiveData<ArrayList<PriceManagementDTO>> pricesLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();

    public LiveData<ArrayList<PriceManagementDTO>> getPrices() {
        return pricesLiveData;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public void fetchPrices() {
        Call<ArrayList<PriceManagementDTO>> call = ClientUtils.productService.getPricesForManagement();
        call.enqueue(new Callback<ArrayList<PriceManagementDTO>>() {
            @Override
            public void onResponse(Call<ArrayList<PriceManagementDTO>> call, Response<ArrayList<PriceManagementDTO>> response) {
                if (response.isSuccessful()) {
                    pricesLiveData.postValue(response.body());
                    errorMessage.postValue(null);

                } else {
                    errorMessage.postValue("Failed to fetch prices for management. Code: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<ArrayList<PriceManagementDTO>> call, Throwable t) {
                errorMessage.postValue(t.getMessage());
            }
        });
    }

    public void editPrice(UUID productId, double basePrice, double discount) {
        UpdatePriceDTO updatePriceDTO = new UpdatePriceDTO();
        updatePriceDTO.setBasePrice(basePrice);
        updatePriceDTO.setDiscount(discount);

        Call<Void> call = ClientUtils.productService.updateProductsPrice(productId, updatePriceDTO);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    fetchPrices();
                    errorMessage.postValue(null);
                } else {
                    errorMessage.postValue("Failed to edit price. Code: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                errorMessage.postValue(t.getMessage());
            }
        });
    }
}
