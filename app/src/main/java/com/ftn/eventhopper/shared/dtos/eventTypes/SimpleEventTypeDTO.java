package com.ftn.event_hopper.dtos.eventTypes;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SimpleEventTypeDTO {
    private UUID id;
    private String name;
    private String description;
}
