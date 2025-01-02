package com.ftn.eventhopper.shared.dtos.users.person;

import com.ftn.eventhopper.shared.dtos.location.CreateLocationDTO;
import com.ftn.eventhopper.shared.models.users.PersonType;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CreatePersonDTO {
    private String name;
    private String surname;
    private String profilePicture;
    private String phoneNumber;
    private PersonType type;
    private CreateLocationDTO location;
}
