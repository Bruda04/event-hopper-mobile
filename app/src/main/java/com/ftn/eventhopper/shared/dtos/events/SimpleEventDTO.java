package com.ftn.eventhopper.shared.dtos.events;

import com.ftn.event_hopper.dtos.eventTypes.SimpleEventTypeDTO;
import com.ftn.event_hopper.dtos.location.SimpleLocationDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class SimpleEventDTO {
    private UUID id;
    private String name;
    private String description;
    private LocalDateTime time;
    private String picture;
    private SimpleEventTypeDTO eventType;
    private SimpleLocationDTO location;
}
