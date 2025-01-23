package com.ftn.eventhopper.shared.dtos.profile;


import com.ftn.eventhopper.shared.dtos.location.SimpleLocationDTO;
import com.ftn.eventhopper.shared.models.users.PersonType;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdatePersonDTO {
    private String name;
    private String surname;
    private String phoneNumber;
    private PersonType type;
    private SimpleLocationDTO location;
}

