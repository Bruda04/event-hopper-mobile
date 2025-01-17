package com.ftn.eventhopper.shared.dtos.invitations;

import com.ftn.eventhopper.shared.dtos.events.SimpleEventDTO;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class InvitationDTO {
    private UUID id;
    private String targetEmail;
    private LocalDateTime timestamp;
    private InvitationStatus status;
    private String picture;
    private SimpleEventDTO event;
}
