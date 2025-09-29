package com.ftn.eventhopper.fragments.login.viewmodels;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ftn.eventhopper.clients.ClientUtils;
import com.ftn.eventhopper.clients.services.auth.UserService;
import com.ftn.eventhopper.clients.services.notifications.NotificationForegroundService;
import com.ftn.eventhopper.shared.dtos.login.LoginDTO;
import com.ftn.eventhopper.shared.dtos.login.LoginResponse;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.UUID;

import lombok.Getter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@Getter
public class LoginViewModel extends ViewModel {
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();

    public void login(String email, String password, Context context) {
        LoginDTO loginDTO = new LoginDTO(email, password);
        Call<LoginResponse> call = ClientUtils.loginService.loginUser(loginDTO);
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> callResponse) {
                if (callResponse.isSuccessful()) {
                    // Success case
                    LoginResponse response = callResponse.body();
                    if (response != null && response.isSuccessful()) {
                        UserService.setJwtToken(response.getToken());
                        if (UserService.isTokenValid()) {
                            ClientUtils.connectWebSocket();
                            Intent serviceIntent = new Intent(context, NotificationForegroundService.class);
                            context.startForegroundService(serviceIntent);
                        }
                        Log.d("Login", "User logged in successfully");
                        errorMessage.postValue("");
                    } else {
                        Log.e("Login failed", response != null ? response.getMessage() : "Unknown error");
                        errorMessage.postValue(response != null ? response.getMessage() : "Unknown error");
                    }
                } else {
                    // Error case - Parse the error body
                    try {
                        String errorBody = callResponse.errorBody().string();
                        Log.e("Error Response", errorBody);

                        // Parse the error body as JSON
                        Gson gson = new Gson();
                        LoginResponse errorResponse = gson.fromJson(errorBody, LoginResponse.class);

                        if (errorResponse != null && errorResponse.getMessage() != null) {
                            Log.e("Login failed", errorResponse.getMessage());
                            errorMessage.postValue(errorResponse.getMessage());
                        } else {
                            errorMessage.postValue("An unknown error occurred.");
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        errorMessage.postValue("Error parsing the server response.");
                    }
                }
            }


            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                errorMessage.postValue("Error occurred on server, please try again later.");
            }
        });
    }

    public void addAttendingEventOnLogin(UUID eventId){
        ClientUtils.profileService.addEventToAttending(eventId).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Log.i("Retrofit", "Event successfully added to attending.");
                } else {
                    Log.e("RetrofitError", "Response code: " + response.code() + ", message: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("RetrofitFailure", "Request failed: " + t.getMessage(), t);
            }
        });
    }

}
