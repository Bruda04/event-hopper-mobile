package com.ftn.eventhopper.shared.dtos.profile;


import com.ftn.eventhopper.shared.dtos.events.SimpleEventDTO;
import com.ftn.eventhopper.shared.dtos.location.SimpleLocationDTO;
import com.ftn.eventhopper.shared.dtos.solutions.SimpleProductDTO;

import java.util.List;
import java.util.Set;
import java.util.UUID;

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
/*Main difference between this and simple is that the favorite events/products are included, as well as
 * the attending events necessary for the calendar*/
public class ProfileForPersonDTO {
    private UUID id;
    private String email;
    private String name;
    private String surname;
    private String profilePicture;
    private String phoneNumber;
    private SimpleLocationDTO location;
    private Set<SimpleEventDTO> attendingEvents;
    private Set<SimpleEventDTO> favoriteEvents;
    private Set<SimpleProductDTO> favoriteProducts;
    private List<SimpleEventDTO> myEvents;
    private String companyName;
    private String companyEmail;
    private String companyPhoneNumber;
    private String companyDescription;
    private SimpleLocationDTO companyLocation;
    private List<String> companyPhotos;
}

