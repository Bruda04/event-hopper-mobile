package com.ftn.eventhopper.fragments.registration.viewmodels;

import android.os.Bundle;
import android.util.Log;

import androidx.lifecycle.ViewModel;

import com.ftn.eventhopper.clients.ClientUtils;
import com.ftn.eventhopper.shared.dtos.location.CreateLocationDTO;
import com.ftn.eventhopper.shared.dtos.registration.CreateRegistrationRequestDTO;
import com.ftn.eventhopper.shared.dtos.users.account.CreateServiceProviderAccountDTO;
import com.ftn.eventhopper.shared.dtos.users.serviceProvider.CreateServiceProviderDTO;
import com.ftn.eventhopper.shared.models.users.PersonType;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PupRegistrationViewModel extends ViewModel {

    public void register(Bundle bundle){
        CreateServiceProviderAccountDTO createDTO = new CreateServiceProviderAccountDTO();
        createDTO.setEmail(bundle.getString("companyEmail"));
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

        Log.d("HelloOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO" , createDTO.toString());

        Call<CreateServiceProviderAccountDTO> call = ClientUtils.registrationService.registerServiceProvider(createDTO);
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
