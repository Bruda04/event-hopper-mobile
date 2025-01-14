
package com.ftn.eventhopper.shared.dtos.location;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SimpleLocationDTO {
    private UUID id;
    private String city;
    private String address;
}
