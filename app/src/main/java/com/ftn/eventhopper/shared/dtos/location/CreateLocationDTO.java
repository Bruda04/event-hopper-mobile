package com.ftn.eventhopper.shared.dtos.location;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CreateLocationDTO {
    private String city;
    private String address;
    private double latitude;
    private double longitude;
}