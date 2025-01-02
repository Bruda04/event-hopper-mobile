package com.ftn.eventhopper.shared.dtos.users.account;

import com.ftn.eventhopper.shared.dtos.registration.RegistrationRequestDTO;
import com.ftn.eventhopper.shared.dtos.users.eventOrganizer.SimpleEventOrganizerDTO;
import com.ftn.eventhopper.shared.models.users.PersonType;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CreatedEventOrganizerAccountDTO {
    private UUID id;
    private String email;
    private boolean isVerified;
    private boolean isActive;
    private LocalDateTime suspensionTimeStamp;
    private PersonType type;
    private SimpleEventOrganizerDTO person;
    private RegistrationRequestDTO registrationRequest;
}