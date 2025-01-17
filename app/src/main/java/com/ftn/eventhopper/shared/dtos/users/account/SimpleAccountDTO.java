package com.ftn.eventhopper.shared.dtos.users.account;

import com.ftn.eventhopper.shared.dtos.users.person.SimplePersonDTO;
import com.ftn.eventhopper.shared.models.users.PersonType;

import java.util.UUID;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SimpleAccountDTO {
    private UUID id;
    private String email;
    private PersonType type;
    private SimplePersonDTO person;
}

