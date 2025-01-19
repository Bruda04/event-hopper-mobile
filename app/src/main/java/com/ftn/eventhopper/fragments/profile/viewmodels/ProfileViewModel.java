package com.ftn.eventhopper.fragments.profile.viewmodels;

import android.graphics.Bitmap;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ftn.eventhopper.clients.ClientUtils;
import com.ftn.eventhopper.clients.ImageUtils;
import com.ftn.eventhopper.clients.services.auth.UserService;
import com.ftn.eventhopper.clients.services.users.ProfileService;
import com.ftn.eventhopper.shared.dtos.profile.ProfileForPersonDTO;
import com.ftn.eventhopper.shared.dtos.solutions.SolutionDetailsDTO;

import lombok.Getter;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@Getter
public class ProfileViewModel extends ViewModel {
    public void logout(){
        UserService.clearJwtToken();
    }

    private final MutableLiveData<ProfileForPersonDTO> profileData = new MutableLiveData<>();

    @Getter
    private MutableLiveData<String> imageUrlLiveData = new MutableLiveData<>();

    @Getter
    private MutableLiveData<Boolean> deactivateAccountSuccess = new MutableLiveData<>();

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

    public void removeProfilePicture(){
        Call<Void> call = ClientUtils.profileService.removeProfilePicture();
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Log.d("Profile picture removal", "SUCCESS");
            }
            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.d("Profile picture removal", "FAILED");
            }
        });
    }

    public void changeProfilePicture(Bitmap image){
        Call<String> imageUploadCall = ImageUtils.uploadImage(image);
        imageUploadCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> imageUploadCall, Response<String> response) {
                if (response.isSuccessful()) {
                    imageUrlLiveData.setValue(response.body());
                    RequestBody requestBody = RequestBody.create(MediaType.parse("text/plain"), response.body());
                    Call<Void> call = ClientUtils.profileService.changeProfilePicture(requestBody);
                    call.enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                            Log.d("Profile picture change", "SUCCESS");
                        }
                        @Override
                        public void onFailure(Call<Void> call, Throwable t) {
                            Log.d("Profile picture change", "FAILED");
                        }
                    });
                }
            }
            @Override
            public void onFailure(Call<String> imageUploadCall, Throwable t) {
                Log.d("Profile picture change", "FAILED");
            }
        });

    }

    public void deactivateAccount(){
        Call<Void> call = ClientUtils.profileService.deactivateAccount();
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Log.d("Account deactivation", "SUCCESS");
                deactivateAccountSuccess.setValue(true);
                UserService.clearJwtToken();
            }
            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                deactivateAccountSuccess.setValue(false);
                Log.d("Account deactivation", "FAILED");
            }
        });

    }
}
