package com.ftn.eventhopper.fragments.registration.viewmodels;

import android.os.Bundle;
import android.util.Log;

import androidx.lifecycle.ViewModel;

import com.ftn.eventhopper.adapters.ImagePreviewAdapter;
import com.ftn.eventhopper.clients.ClientUtils;
import com.ftn.eventhopper.clients.ImageUtils;
import com.ftn.eventhopper.shared.dtos.location.CreateLocationDTO;
import com.ftn.eventhopper.shared.dtos.registration.CreateRegistrationRequestDTO;
import com.ftn.eventhopper.shared.dtos.users.account.CreateEventOrganizerAccountDTO;
import com.ftn.eventhopper.shared.dtos.users.account.CreateServiceProviderAccountDTO;
import com.ftn.eventhopper.shared.dtos.users.eventOrganizer.CreateEventOrganizerDTO;
import com.ftn.eventhopper.shared.dtos.users.serviceProvider.CreateServiceProviderDTO;
import com.ftn.eventhopper.shared.models.users.PersonType;


import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PupRegistrationViewModel extends ViewModel {
    public void checkEmail(String email, OrganizerRegistrationViewModel.EmailCheckCallback callback) {
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
        CreateServiceProviderAccountDTO createDTO = new CreateServiceProviderAccountDTO();
        createDTO.setEmail(bundle.getString("email"));
        createDTO.setPassword(bundle.getString("password"));
        createDTO.setVerified(false);
        createDTO.setType(PersonType.SERVICE_PROVIDER);
        createDTO.setRegistrationRequest(new CreateRegistrationRequestDTO());

        CreateServiceProviderDTO personDTO = new CreateServiceProviderDTO();
        personDTO.setCompanyName(bundle.getString("companyName"));
        personDTO.setCompanyEmail(bundle.getString("companyEmail"));
        personDTO.setCompanyDescription(bundle.getString("description"));
        personDTO.setCompanyPhoneNumber(bundle.getString("companyPhoneNumber"));
        CreateLocationDTO companyLocation = new CreateLocationDTO();
        companyLocation.setCity(bundle.getString("companyCity"));
        companyLocation.setAddress(bundle.getString("companyAddress"));
        personDTO.setCompanyLocation(companyLocation);

        personDTO.setName(bundle.getString("name"));
        personDTO.setSurname(bundle.getString("surname"));
        personDTO.setPhoneNumber(bundle.getString("phone"));
        personDTO.setType(PersonType.SERVICE_PROVIDER);
        CreateLocationDTO personLocation = new CreateLocationDTO();
        personLocation.setCity(bundle.getString("city"));
        personLocation.setAddress(bundle.getString("address"));
        personDTO.setLocation(personLocation);

        createDTO.setPerson(personDTO);

        submitProfilePicture(personDTO, createDTO, bundle);
    }





    public void submitProfilePicture(CreateServiceProviderDTO pupDto, CreateServiceProviderAccountDTO createAccountDTO, Bundle bundle){
        if(!bundle.containsKey("profilePicture")){
            submitCompanyPhotos(pupDto, createAccountDTO, bundle);
            return;
        }

        ImagePreviewAdapter.ImagePreviewItem image = (ImagePreviewAdapter.ImagePreviewItem) bundle.getSerializable("profilePicture");

        Call<String> call = ImageUtils.uploadImage(image.getBitmap());
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {

                    pupDto.setProfilePicture(response.body());
                    createAccountDTO.setPerson(pupDto);
                    submitCompanyPhotos(pupDto, createAccountDTO, bundle);
                } else {
                }
            }
            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });


    }

    public void submitCompanyPhotos(CreateServiceProviderDTO pupDto, CreateServiceProviderAccountDTO createAccountDTO, Bundle bundle){
        if(!bundle.containsKey("companyPictures")){
            submitRegistrationData(createAccountDTO);
            return;
        }

        ArrayList<ImagePreviewAdapter.ImagePreviewItem> uploadedImages = (ArrayList<ImagePreviewAdapter.ImagePreviewItem>) bundle.getSerializable("companyPictures");
        AtomicInteger remainingUploads = new AtomicInteger(uploadedImages.size());
        AtomicBoolean hasUploadFailed = new AtomicBoolean(false);

        if(uploadedImages == null){
            submitRegistrationData(createAccountDTO);
            return;
        }
        pupDto.setCompanyPhotos(new ArrayList<>());
        for (ImagePreviewAdapter.ImagePreviewItem image : uploadedImages) {
            Call<String> call = ImageUtils.uploadImage(image.getBitmap());
            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    if (response.isSuccessful()) {
                        pupDto.getCompanyPhotos().add(response.body());
                        if (remainingUploads.decrementAndGet() == 0 && !hasUploadFailed.get())  {
                            createAccountDTO.setPerson(pupDto);
                            submitRegistrationData(createAccountDTO);
                        }

                    } else {
                        Log.d("Error uploading images", "Image failed");
                        uploadedImages.clear();
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    Log.e("Image upload failed", t.getMessage());
                }
            });
        }
    }



    public void submitRegistrationData(CreateServiceProviderAccountDTO createAccountDTO){
        Call<CreateServiceProviderAccountDTO> call = ClientUtils.registrationService.registerServiceProvider(createAccountDTO);
        call.enqueue(new Callback<CreateServiceProviderAccountDTO>() {
            @Override
            public void onResponse(Call<CreateServiceProviderAccountDTO> call, Response<CreateServiceProviderAccountDTO> callResponse) {
                if (callResponse.isSuccessful()) {
                    Log.d("Pup registration", "User registered");
                } else {
                    Log.e("Pup registration", "Registration failed");
                }
            }
            @Override
            public void onFailure(Call<CreateServiceProviderAccountDTO> call, Throwable t) {
                Log.e("Pup registration", "Server error occurred.");
            }
        });
    }

}
