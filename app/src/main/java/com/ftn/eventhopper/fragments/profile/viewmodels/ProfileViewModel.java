package com.ftn.eventhopper.fragments.profile.viewmodels;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ftn.eventhopper.clients.ClientUtils;
import com.ftn.eventhopper.clients.services.auth.UserService;
import com.ftn.eventhopper.shared.dtos.profile.ProfileForPersonDTO;

import lombok.Getter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@Getter
public class ProfileViewModel extends ViewModel {
    public void logout(){
        UserService.clearJwtToken();
    }

    private final MutableLiveData<ProfileForPersonDTO> profileData = new MutableLiveData<>();

    public LiveData<ProfileForPersonDTO> getProfileData() {
        return profileData;
    }

    public void fetchProfile() {
        Call<ProfileForPersonDTO> call = ClientUtils.profileService.getProfile();
        call.enqueue(new Callback<ProfileForPersonDTO>() {
            @Override
            public void onResponse(Call<ProfileForPersonDTO> call, Response<ProfileForPersonDTO> response) {
                if (response.isSuccessful() && response.body() != null) {
                    profileData.postValue(response.body());
                } else {
                    Log.e("Profile Fetch", "Failed to fetch profile: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<ProfileForPersonDTO> call, Throwable t) {
                Log.e("Profile Fetch Error", t.getMessage(), t);
            }
        });
    }
}
