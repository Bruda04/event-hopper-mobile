package com.ftn.eventhopper.shared.dtos.users.serviceProvider;



import com.ftn.eventhopper.shared.dtos.location.LocationDTO;
import com.ftn.eventhopper.shared.dtos.users.person.SimplePersonDTO;

import lombok.*;

import java.time.LocalTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
public class SimpleServiceProviderDTO extends SimplePersonDTO {
    private String companyName;
    private String companyEmail;
    private String companyPhoneNumber;
    private String companyDescription;
    private List<String> companyPhotos;
    private LocalTime workStart;
    private LocalTime workEnd;

    private LocationDTO companyLocation;
}
