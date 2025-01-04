package com.ftn.eventhopper.fragments.registration.viewmodels;

import android.os.Bundle;
import android.util.Log;

import androidx.lifecycle.ViewModel;

import com.ftn.eventhopper.clients.ClientUtils;
import com.ftn.eventhopper.shared.dtos.location.CreateLocationDTO;
import com.ftn.eventhopper.shared.dtos.registration.CreateRegistrationRequestDTO;
import com.ftn.eventhopper.shared.dtos.users.account.CreateEventOrganizerAccountDTO;
import com.ftn.eventhopper.shared.dtos.users.eventOrganizer.CreateEventOrganizerDTO;
import com.ftn.eventhopper.shared.models.users.PersonType;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrganizerRegistrationViewModel  extends ViewModel {

    public void register(Bundle bundle){
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
        eventOrganizerDTO.setProfilePicture("..");

        createDTO.setPerson(eventOrganizerDTO);

        Call<CreateEventOrganizerAccountDTO> call = ClientUtils.registrationService.registerEventOrganizer(createDTO);
        call.enqueue(new Callback<CreateEventOrganizerAccountDTO>() {
            @Override
            public void onResponse(Call<CreateEventOrganizerAccountDTO> call, Response<CreateEventOrganizerAccountDTO> callResponse) {
                if (callResponse.isSuccessful()) {
                    Log.d("Organizer registration", "User registered");
                } else {
                    Log.e("Organizer registration", "Registration failed");
                }
            }
            @Override
            public void onFailure(Call<CreateEventOrganizerAccountDTO> call, Throwable t) {
                Log.e("Organizer registration", "Server error occurred.");
            }
        });



    }



}
