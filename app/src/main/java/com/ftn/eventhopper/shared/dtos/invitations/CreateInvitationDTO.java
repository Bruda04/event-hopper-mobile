package com.ftn.eventhopper.shared.dtos.invitations;

import com.ftn.eventhopper.shared.dtos.events.SimpleEventDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateInvitationDTO {
    private String targetEmail;
    private String picture;
    private SimpleEventDTO event;
}
