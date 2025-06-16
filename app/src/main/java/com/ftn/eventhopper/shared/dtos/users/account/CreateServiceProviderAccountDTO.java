package com.ftn.eventhopper.shared.dtos.users.account;

import com.ftn.eventhopper.shared.dtos.registration.CreateRegistrationRequestDTO;
import com.ftn.eventhopper.shared.dtos.users.serviceProvider.CreateServiceProviderDTO;
import com.ftn.eventhopper.shared.models.users.PersonType;

import java.time.LocalDateTime;
import java.time.LocalTime;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CreateServiceProviderAccountDTO {
    private String email;
    private String password;
    private boolean isVerified;
    private LocalDateTime suspensionTimeStamp;
    private PersonType type;
    private CreateServiceProviderDTO person;
    private CreateRegistrationRequestDTO registrationRequest;
}
