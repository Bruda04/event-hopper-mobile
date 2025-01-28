package com.ftn.eventhopper.shared.dtos.profile;

import com.ftn.eventhopper.shared.dtos.location.LocationDTO;

import java.util.UUID;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UpdateCompanyAccountDTO {
    private UUID id;
    private String companyDescription;
    private LocationDTO companyLocation;
    private String companyPhoneNumber;
}