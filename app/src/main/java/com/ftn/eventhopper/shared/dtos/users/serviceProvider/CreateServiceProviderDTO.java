package com.ftn.eventhopper.shared.dtos.users.serviceProvider;

import com.ftn.eventhopper.shared.dtos.location.CreateLocationDTO;
import com.ftn.eventhopper.shared.dtos.users.person.CreatePersonDTO;

import java.time.LocalTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
public class CreateServiceProviderDTO extends CreatePersonDTO {
    private String companyName;
    private String companyEmail;
    private String companyDescription;
    private String companyPhoneNumber;
    private List<String> companyPhotos;
    private CreateLocationDTO companyLocation;
    private LocalTime workStart;
    private LocalTime workEnd;
}
