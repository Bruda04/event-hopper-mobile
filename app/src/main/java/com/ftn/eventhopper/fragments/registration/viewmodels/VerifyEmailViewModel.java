package com.ftn.eventhopper.fragments.registration.viewmodels;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ftn.eventhopper.clients.ClientUtils;
import com.ftn.eventhopper.shared.models.registration.VerificationTokenState;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VerifyEmailViewModel extends ViewModel {
    private final MutableLiveData<VerificationTokenState> state = new MutableLiveData<>();

    public LiveData<VerificationTokenState> getState() {
        return state;
    }

    public void verifyToken(String token){
        Call<VerificationTokenState> call = ClientUtils.registrationService.verifyToken(token);
        call.enqueue(new Callback<VerificationTokenState>() {
            @Override
            public void onResponse(Call<VerificationTokenState> call, Response<VerificationTokenState> callResponse) {
                state.postValue(callResponse.body());
            }
            @Override
            public void onFailure(Call<VerificationTokenState> call, Throwable t) {
                Log.e("Token verification failed", "Server error occurred.");
            }
        });

    }



}
