package com.ftn.eventhopper.fragments.registration.viewmodels;

import android.os.Bundle;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ftn.eventhopper.adapters.ImagePreviewAdapter;
import com.ftn.eventhopper.clients.ClientUtils;
import com.ftn.eventhopper.clients.ImageUtils;
import com.ftn.eventhopper.shared.dtos.location.CreateLocationDTO;
import com.ftn.eventhopper.shared.dtos.registration.CreateRegistrationRequestDTO;
import com.ftn.eventhopper.shared.dtos.solutions.CreateServiceDTO;
import com.ftn.eventhopper.shared.dtos.users.account.CreateEventOrganizerAccountDTO;
import com.ftn.eventhopper.shared.dtos.users.eventOrganizer.CreateEventOrganizerDTO;
import com.ftn.eventhopper.shared.models.users.PersonType;

import java.util.ArrayList;

import lombok.Getter;
import lombok.Setter;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrganizerRegistrationViewModel  extends ViewModel {

    @Getter
    @Setter
    private ArrayList<ImagePreviewAdapter.ImagePreviewItem> uploadedImages = new ArrayList<>();

    public void checkEmail(String email, EmailCheckCallback callback) {
        RequestBody requestBody = RequestBody.create(MediaType.parse("text/plain"), email);
        Call<Boolean> call = ClientUtils.registrationService.isEmailTaken(requestBody);

        call.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onResult(response.body());
                } else {
                    callback.onResult(false); // Default to false if something goes wrong
                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                callback.onResult(true); // Default to true on failure
            }
        });
    }

    public interface EmailCheckCallback {
        void onResult(boolean isEmailTaken);
    }


    public void register(Bundle bundle){
        for (ImagePreviewAdapter.ImagePreviewItem image : uploadedImages) {
            Call<String> call = ImageUtils.uploadImage(image.getBitmap());
            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    if (response.isSuccessful()) {
                        CreateEventOrganizerAccountDTO createDTO = new CreateEventOrganizerAccountDTO();
                        createDTO.setEmail(bundle.getString("email"));
                        createDTO.setPassword(bundle.getString("password"));
                        createDTO.setVerified(false);
                        createDTO.setType(PersonType.EVENT_ORGANIZER);
                        createDTO.setRegistrationRequest(new CreateRegistrationRequestDTO());


                        CreateEventOrganizerDTO eventOrganizerDTO = new CreateEventOrganizerDTO();
                        eventOrganizerDTO.setName(bundle.getString("name"));
                        eventOrganizerDTO.setSurname(bundle.getString("surname"));
                        eventOrganizerDTO.setPhoneNumber(bundle.getString("phone"));
                        eventOrganizerDTO.setType(PersonType.EVENT_ORGANIZER);

                        CreateLocationDTO locationDTO = new CreateLocationDTO();
                        locationDTO.setCity(bundle.getString("city"));
                        locationDTO.setAddress(bundle.getString("address"));
                        eventOrganizerDTO.setLocation(locationDTO);

                        eventOrganizerDTO.setProfilePicture(response.body());

                        createDTO.setPerson(eventOrganizerDTO);

                        Call<CreateEventOrganizerAccountDTO> registerCall = ClientUtils.registrationService.registerEventOrganizer(createDTO);
                        registerCall.enqueue(new Callback<CreateEventOrganizerAccountDTO>() {
                            @Override
                            public void onResponse(Call<CreateEventOrganizerAccountDTO> registerCall, Response<CreateEventOrganizerAccountDTO> callResponse) {
                                if (callResponse.isSuccessful()) {
                                    Log.d("Organizer registration", "User registered");
                                } else {
                                    Log.e("Organizer registration", "Registration failed");
                                }
                            }

                            @Override
                            public void onFailure(Call<CreateEventOrganizerAccountDTO> registerCall, Throwable t) {
                                Log.e("Organizer registration", "Server error occurred.");
                            }
                        });


                    } else {
                        uploadedImages.clear();
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    uploadedImages.clear();
                }
            });


        }











    }
}
