package com.ftn.eventhopper.shared.dtos.users.serviceProvider;

import com.ftn.eventhopper.shared.models.locations.Location;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class CompanyDetailsDTO {
    private String companyName;
    private String companyEmail;
    private String companyPhoneNumber;
    private String companyDescription;
    private List<String> companyPhotos;
    private Location companyLocation;
}
