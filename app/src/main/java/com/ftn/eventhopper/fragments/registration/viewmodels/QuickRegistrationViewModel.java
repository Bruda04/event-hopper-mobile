package com.ftn.eventhopper.fragments.registration.viewmodels;

import android.os.Bundle;
import android.util.Log;

import androidx.lifecycle.ViewModel;

import com.ftn.eventhopper.adapters.ImagePreviewAdapter;
import com.ftn.eventhopper.clients.ClientUtils;
import com.ftn.eventhopper.clients.ImageUtils;
import com.ftn.eventhopper.shared.dtos.location.CreateLocationDTO;
import com.ftn.eventhopper.shared.dtos.registration.CreateRegistrationRequestDTO;
import com.ftn.eventhopper.shared.dtos.users.account.CreatePersonAccountDTO;
import com.ftn.eventhopper.shared.dtos.users.person.CreatePersonDTO;
import com.ftn.eventhopper.shared.models.users.PersonType;

import java.util.ArrayList;

import lombok.Getter;
import lombok.Setter;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QuickRegistrationViewModel extends ViewModel {

    @Getter
    @Setter
    private ArrayList<ImagePreviewAdapter.ImagePreviewItem> uploadedImages = new ArrayList<>();

    public void checkEmail(String email, QuickRegistrationViewModel.EmailCheckCallback callback) {
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
        CreatePersonAccountDTO createDTO = deconstructBundle(bundle);

        if(this.uploadedImages.isEmpty()){
            submitRegistration(createDTO);
            return;
        }

        for (ImagePreviewAdapter.ImagePreviewItem image : uploadedImages){
            Call<String> call = ImageUtils.uploadImage(image.getBitmap());
            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    if (response.isSuccessful()) {
                        createDTO.getPerson().setProfilePicture(response.body());
                        submitRegistration(createDTO);
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

    public CreatePersonAccountDTO deconstructBundle(Bundle bundle){
        CreatePersonAccountDTO createDTO = new CreatePersonAccountDTO();
        createDTO.setEmail(bundle.getString("email"));
        createDTO.setPassword(bundle.getString("password"));
        createDTO.setVerified(true);
        createDTO.setType(PersonType.AUTHENTICATED_USER);
        createDTO.setRegistrationRequest(new CreateRegistrationRequestDTO());


        CreatePersonDTO userDTO = new CreatePersonDTO();
        userDTO.setName(bundle.getString("name"));
        userDTO.setSurname(bundle.getString("surname"));
        userDTO.setPhoneNumber(bundle.getString("phone"));
        userDTO.setType(PersonType.AUTHENTICATED_USER);

        CreateLocationDTO locationDTO = new CreateLocationDTO();
        locationDTO.setCity(bundle.getString("city"));
        locationDTO.setAddress(bundle.getString("address"));
        userDTO.setLocation(locationDTO);

        createDTO.setPerson(userDTO);
        return createDTO;
    }

    public void submitRegistration(CreatePersonAccountDTO createDTO){

        Call<CreatePersonAccountDTO> call = ClientUtils.registrationService.registerPerson(createDTO);
        call.enqueue(new Callback<CreatePersonAccountDTO>() {
            @Override
            public void onResponse(Call<CreatePersonAccountDTO> call, Response<CreatePersonAccountDTO> callResponse) {
                if (callResponse.isSuccessful()) {
                    Log.d("Person registration", "User registered");
                } else {
                    Log.e("Person registration", "Registration failed");
                }
            }
            @Override
            public void onFailure(Call<CreatePersonAccountDTO> call, Throwable t) {
                Log.e("Person registration", "Server error occurred.");
            }
        });
    }

}
