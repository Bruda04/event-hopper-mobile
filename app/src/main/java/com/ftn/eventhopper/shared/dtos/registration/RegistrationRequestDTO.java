package com.ftn.eventhopper.shared.dtos.registration;

import com.ftn.eventhopper.shared.models.registration.RegistrationRequestStatus;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RegistrationRequestDTO {
    private UUID id;
    private LocalDateTime timestamp;
    private RegistrationRequestStatus status;
}

