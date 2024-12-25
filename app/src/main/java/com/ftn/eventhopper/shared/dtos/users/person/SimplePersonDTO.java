package com.ftn.eventhopper.shared.dtos.users.person;


import com.ftn.eventhopper.shared.dtos.location.SimpleLocationDTO;
import com.ftn.eventhopper.shared.models.users.PersonType;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class SimplePersonDTO {
    private UUID id;
    private String name;
    private String surname;
    private String profilePicture;
    private String phoneNumber;
    private PersonType type;
    private SimpleLocationDTO location;
}
