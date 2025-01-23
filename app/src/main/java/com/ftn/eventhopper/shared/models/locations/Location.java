package com.ftn.eventhopper.shared.models.locations;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Location {

    private UUID id;

    private String city;

    private String address;

    private Double latitude;

    private Double longitude;
}
