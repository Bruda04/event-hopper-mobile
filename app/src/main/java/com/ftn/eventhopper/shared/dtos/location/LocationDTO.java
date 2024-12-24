package com.ftn.event_hopper.dtos.location;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class LocationDTO {
    private UUID id;
    private String city;
    private String address;
    private double latitude;
    private double longitude;
}
