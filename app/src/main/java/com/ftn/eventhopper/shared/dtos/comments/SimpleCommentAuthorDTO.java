package com.ftn.eventhopper.shared.dtos.comments;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class SimpleCommentAuthorDTO {
    private UUID id;
    private String name;
    private String surname;
    private String profilePicture;
}
