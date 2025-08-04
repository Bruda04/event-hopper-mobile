package com.ftn.eventhopper.fragments.registration.viewmodels;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ftn.eventhopper.clients.ClientUtils;
import com.ftn.eventhopper.clients.ImageUtils;
import com.ftn.eventhopper.shared.dtos.location.CreateLocationDTO;
import com.ftn.eventhopper.shared.dtos.registration.CreateRegistrationRequestDTO;
import com.ftn.eventhopper.shared.dtos.users.account.CreateServiceProviderAccountDTO;
import com.ftn.eventhopper.shared.dtos.users.serviceProvider.CreateServiceProviderDTO;
import com.ftn.eventhopper.shared.models.users.PersonType;


import java.io.File;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import lombok.Getter;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PupRegistrationViewModel extends ViewModel {
    @Getter
    private MutableLiveData<Boolean> registrationComplete = new MutableLiveData<>();

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

        LocalTime startTime = LocalTime.parse(bundle.getString("workStart"));
        LocalTime endTime = LocalTime.parse(bundle.getString("workEnd"));
        personDTO.setWorkStart(startTime);
        personDTO.setWorkEnd(endTime);

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





    public void submitProfilePicture(CreateServiceProviderDTO pupDto, CreateServiceProviderAccountDTO createAccountDTO, Bundle bundle) {
        if (!bundle.containsKey("profilePicturePath")) {
            submitCompanyPhotos(pupDto, createAccountDTO, bundle);
            return;
        }

        String imagePath = bundle.getString("profilePicturePath"); // Retrieve byte array

        if (imagePath == null || imagePath.isEmpty()) {
            submitRegistrationData(createAccountDTO);
            return;
        }

        Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
        if (bitmap == null) {
            Log.e("Image Load Error", "Failed to load bitmap from file: " + imagePath);
            submitCompanyPhotos(pupDto, createAccountDTO, bundle);
        }

        Call<String> call = ImageUtils.uploadImage(bitmap);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    pupDto.setProfilePicture(response.body());
                    createAccountDTO.setPerson(pupDto);
                    submitCompanyPhotos(pupDto, createAccountDTO, bundle);
                } else {
                    Log.e("Image Upload", "Failed to upload profile picture");
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e("Image Upload Error", t.getMessage());
            }
        });

    }


    public void submitCompanyPhotos(CreateServiceProviderDTO pupDto, CreateServiceProviderAccountDTO createAccountDTO, Bundle bundle) {
        if (!bundle.containsKey("companyPictures")) {
            submitRegistrationData(createAccountDTO);
            return;
        }

        // Retrieve the file paths of the images from the bundle
        ArrayList<String> imageFilePaths = bundle.getStringArrayList("companyPictures");
        if (imageFilePaths == null || imageFilePaths.isEmpty()) {
            submitRegistrationData(createAccountDTO);
            return;
        }

        AtomicInteger remainingUploads = new AtomicInteger(imageFilePaths.size());
        AtomicBoolean hasUploadFailed = new AtomicBoolean(false);

        pupDto.setCompanyPhotos(new ArrayList<>());

        for (String filePath : imageFilePaths) {
            Bitmap bitmap = BitmapFactory.decodeFile(filePath);
            if (bitmap == null) {
                Log.e("Image Load Error", "Failed to load bitmap from file: " + filePath);
                continue;
            }

            Call<String> call = ImageUtils.uploadImage(bitmap);
            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    if (response.isSuccessful()) {
                        pupDto.getCompanyPhotos().add(response.body());
                        if (remainingUploads.decrementAndGet() == 0 && !hasUploadFailed.get()) {
                            createAccountDTO.setPerson(pupDto);
                            registrationComplete.setValue(true);
                            submitRegistrationData(createAccountDTO);
                        }
                    } else {
                        Log.e("Image Upload Error", "Failed to upload image: " + filePath);
                        hasUploadFailed.set(true);
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    Log.e("Image Upload Failure", "Error uploading image: " + filePath, t);
                    hasUploadFailed.set(true);
                    if (remainingUploads.decrementAndGet() == 0 && !hasUploadFailed.get()) {
                        submitRegistrationData(createAccountDTO);
                    }
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
                    registrationComplete.setValue(false);
                    Log.e("Pup registration", "Registration failed");
                }
            }
            @Override
            public void onFailure(Call<CreateServiceProviderAccountDTO> call, Throwable t) {
                Log.e("Pup registration", "Server error occurred.");
            }
        });
    }


    public void cleanupCache(Context context) {
        File cacheDir = context.getCacheDir();
        for (File file : cacheDir.listFiles()) {
            if (file.getName().startsWith("image_")) {
                file.delete();
            }
        }
    }

}
