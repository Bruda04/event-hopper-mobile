package com.ftn.eventhopper.shared.dtos.events;

import com.ftn.eventhopper.shared.dtos.location.CreateLocationDTO;
import com.ftn.eventhopper.shared.models.events.EventPrivacyType;

import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class CreateEventDTO {
    private String name;
    private int maxAttendance;
    private String description;
    private EventPrivacyType eventPrivacyType;
    private LocalDateTime time;
    private String picture;
    private UUID eventTypeId;
    private ArrayList<CreateAgendaActivityDTO> agendaActivities = new ArrayList<>();
    private CreateLocationDTO location;
    private ArrayList<UUID> productsIds = new ArrayList<UUID>();
    private ArrayList<UUID> invitationsIds = new ArrayList<UUID>();
    private UUID eventOrganizerId;
}
