package com.ftn.eventhopper.shared.models.events;

import com.ftn.eventhopper.shared.models.agendaActivities.AgendaActivity;
import com.ftn.eventhopper.shared.models.eventTypes.EventType;
import com.ftn.eventhopper.shared.models.locations.Location;

import lombok.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode

public class Event {
 private UUID id;

    private String name;

    private int maxAttendance;

    private String description;

    private EventPrivacyType privacy;

    private LocalDateTime time;

    private String picture;

    private EventType eventType;

    private Set<AgendaActivity> agendaActivities = new HashSet<AgendaActivity>();

    private Location location;

}
