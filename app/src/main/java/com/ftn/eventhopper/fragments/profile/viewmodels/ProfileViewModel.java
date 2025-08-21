package com.ftn.eventhopper.fragments.profile.viewmodels;

import android.graphics.Bitmap;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ftn.eventhopper.clients.ClientUtils;
import com.ftn.eventhopper.clients.ImageUtils;
import com.ftn.eventhopper.clients.services.auth.UserService;
import com.ftn.eventhopper.shared.dtos.location.LocationDTO;
import com.ftn.eventhopper.shared.dtos.location.SimpleLocationDTO;
import com.ftn.eventhopper.shared.dtos.notifications.NotificationDTO;
import com.ftn.eventhopper.shared.dtos.profile.ChangePasswordDTO;
import com.ftn.eventhopper.shared.dtos.profile.ProfileForPersonDTO;
import com.ftn.eventhopper.shared.dtos.profile.UpdateCompanyAccountDTO;
import com.ftn.eventhopper.shared.dtos.profile.UpdatePersonDTO;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;

import lombok.Getter;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@Getter
public class ProfileViewModel extends ViewModel {
    @Getter
    private final MutableLiveData<Boolean> profileChanged = new MutableLiveData<>();
    @Getter
    private ProfileForPersonDTO profile;

    @Getter
    private final MutableLiveData<String> errorMessagePassword = new MutableLiveData<>();

    @Getter
    private final MutableLiveData<Boolean> editPersonProfileSuccess = new MutableLiveData<>();

    @Getter
    private final MutableLiveData<Boolean> editCompanyProfileSuccess = new MutableLiveData<>();

    private final MutableLiveData<ArrayList<NotificationDTO>> notificationsLiveData = new MutableLiveData<>(new ArrayList<>());

    @Getter
    private MutableLiveData<String> imageUrlLiveData = new MutableLiveData<>();

    @Getter
    private MutableLiveData<Boolean> deactivateAccountSuccess = new MutableLiveData<>();


    public LiveData<ArrayList<NotificationDTO>> getNotificationsLiveData() {
        return notificationsLiveData;
    }

    // Dodaj novu notifikaciju i osveži UI
    public void addNotification(NotificationDTO notification) {
        ArrayList<NotificationDTO> current = notificationsLiveData.getValue();
        if (current == null) current = new ArrayList<>();
        current.add(0, notification);
        notificationsLiveData.postValue(current);
    }

    public void loadProfileNotifications(ProfileForPersonDTO profile) {
        if (profile != null && profile.getNotifications() != null) {
            ArrayList<NotificationDTO> sorted = new ArrayList<>(profile.getNotifications());
            Collections.reverse(sorted);
            notificationsLiveData.postValue(sorted);        }
    }

    public void logout(){
        UserService.clearJwtToken();
        ClientUtils.disconnectStompClient();
        this.clearData();
    }
    public void clearData() {
        profileChanged.setValue(null);
        imageUrlLiveData.setValue(null);
        deactivateAccountSuccess.setValue(null);
        errorMessagePassword.setValue(null);
        profile = null;
    }

    public void fetchProfile() {
        Call<ProfileForPersonDTO> call = ClientUtils.profileService.getProfile();
        call.enqueue(new Callback<ProfileForPersonDTO>() {
            @Override
            public void onResponse(Call<ProfileForPersonDTO> call, Response<ProfileForPersonDTO> response) {
                if (response.isSuccessful() && response.body() != null) {
                    profile = response.body();
                    Log.d("in viewmodel", String.valueOf(profile));
                    profileChanged.setValue(true);
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
                    profile.setProfilePicture(response.body());
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


    public void editPerson(String name, String surname, String phoneNumber, String address, String city){
        SimpleLocationDTO newLocation = new SimpleLocationDTO();
        newLocation.setCity(city);
        newLocation.setAddress(address);
        UpdatePersonDTO updatePersonDTO = new UpdatePersonDTO(name, surname, phoneNumber, UserService.getUserRole(), newLocation);

        Call<Void> updatePersonCall = ClientUtils.profileService.editProfileInformation(updatePersonDTO);
        updatePersonCall.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> updatePersonCall, Response<Void> response) {
                editPersonProfileSuccess.setValue(true);
                Log.d("Profile information change", "SUCCESS");
                profile.setName(name);
                profile.setSurname(surname);
                profile.setPhoneNumber(phoneNumber);
                profile.setLocation(newLocation);
            }
            @Override
            public void onFailure(Call<Void> updatePersonCall, Throwable t) {
                editPersonProfileSuccess.setValue(false);
                Log.d("Profile information change", "FAILED");
            }
        });
    }

    public void editCompany(String companyDescription, String companyPhoneNumber, String address, String city){
        LocationDTO newLocation = new LocationDTO();
        newLocation.setCity(city);
        newLocation.setAddress(address);
        UpdateCompanyAccountDTO updateCompanyAccountDTO = new UpdateCompanyAccountDTO();
        updateCompanyAccountDTO.setCompanyDescription(companyDescription);
        updateCompanyAccountDTO.setCompanyPhoneNumber(companyPhoneNumber);
        updateCompanyAccountDTO.setCompanyLocation(newLocation);

        Call<Void> updateCompanyCall = ClientUtils.profileService.editCompanyInformation(updateCompanyAccountDTO);
        updateCompanyCall.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> updateCompanyCall, Response<Void> response) {
                editCompanyProfileSuccess.setValue(true);
                Log.d("Company information change", "SUCCESS");
                profile.setCompanyDescription(companyDescription);
                SimpleLocationDTO location = new SimpleLocationDTO();
                location.setCity(city);
                location.setAddress(address);
                profile.setCompanyLocation(location);
                profile.setCompanyPhoneNumber(companyPhoneNumber);
            }
            @Override
            public void onFailure(Call<Void> updateCompanyCall, Throwable t) {
                editCompanyProfileSuccess.setValue(false);
                Log.d("Company information change", "FAILED");
            }
        });
    }


    public void changePassword(String oldPassword, String newPassword) {
        ChangePasswordDTO changePasswordDTO = new ChangePasswordDTO(oldPassword, newPassword);
        Call<ResponseBody> call = ClientUtils.profileService.changePassword(changePasswordDTO);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    errorMessagePassword.setValue("Password changed successfully!");
                    Log.d("SUCCESS", "in response success");
                } else {
                    Log.d("BACK", String.valueOf(response));
                    try {
                        String errorBody = response.errorBody().string();
                        JSONObject jsonObject = new JSONObject(errorBody);
                        String errorMessage = jsonObject.getString("message");
                        errorMessagePassword.setValue(errorMessage);
                        Log.d("FAIL", "in custom message fail");

                    } catch (Exception e) {
                        errorMessagePassword.setValue("An unexpected error occurred.");
                        Log.d("FAIL", "in unexpectedd errror ");

                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                errorMessagePassword.setValue("Failed to connect to the server. Please try again later.");
                Log.d("FAIL", "in on failure ");

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
