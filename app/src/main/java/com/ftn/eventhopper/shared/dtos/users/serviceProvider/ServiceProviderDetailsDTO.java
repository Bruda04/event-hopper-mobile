package com.ftn.eventhopper.shared.dtos.users.serviceProvider;

import com.ftn.eventhopper.shared.dtos.comments.SimpleCommentDTO;
import com.ftn.eventhopper.shared.dtos.location.LocationDTO;
import com.ftn.eventhopper.shared.models.users.PersonType;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalTime;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class ServiceProviderDetailsDTO {
    private UUID id;
    private String name;
    private String surname;
    private String profilePicture;
    private String phoneNumber;
    private PersonType type;

    private String companyName;
    private String companyEmail;
    private String companyPhoneNumber;
    private String companyDescription;
    private List<String> companyPhotos;
    private LocalTime workStart;
    private LocalTime workEnd;
    private LocationDTO companyLocation;

    private Collection<SimpleCommentDTO> comments;
    private double rating;
}
