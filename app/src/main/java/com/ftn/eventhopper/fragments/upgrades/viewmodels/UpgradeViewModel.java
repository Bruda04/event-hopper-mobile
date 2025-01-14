package com.ftn.eventhopper.fragments.upgrades.viewmodels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ftn.eventhopper.clients.ClientUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpgradeViewModel extends ViewModel {

    private final MutableLiveData<Boolean> upgradeSuccess = new MutableLiveData<>();

    public MutableLiveData<Boolean> getUpgradeSuccess() {
        return upgradeSuccess;
    }

    public void upgradeToPup() {
        Call<Void> call = ClientUtils.profileService.upgradeToPUP();
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    upgradeSuccess.postValue(true); // Notify success
                } else {
                    upgradeSuccess.postValue(false); // Notify failure
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                upgradeSuccess.postValue(false); // Notify failure
            }
        });
    }

    public void upgradeToOD() {
        Call<Void> call = ClientUtils.profileService.upgradeToOd();
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    upgradeSuccess.postValue(true); // Notify success
                } else {
                    upgradeSuccess.postValue(false); // Notify failure
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                upgradeSuccess.postValue(false); // Notify failure
            }
        });
    }
}
