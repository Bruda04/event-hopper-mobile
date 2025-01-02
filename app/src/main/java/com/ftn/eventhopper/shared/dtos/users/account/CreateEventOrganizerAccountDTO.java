package com.ftn.eventhopper.shared.dtos.users.account;

import com.ftn.eventhopper.shared.dtos.registration.CreateRegistrationRequestDTO;
import com.ftn.eventhopper.shared.dtos.users.eventOrganizer.CreateEventOrganizerDTO;
import com.ftn.eventhopper.shared.models.users.PersonType;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CreateEventOrganizerAccountDTO {
    private String email;
    private String password;
    private boolean isVerified;
    private LocalDateTime suspensionTimeStamp;
    private PersonType type;
    private CreateEventOrganizerDTO person;
    private CreateRegistrationRequestDTO registrationRequest;
}