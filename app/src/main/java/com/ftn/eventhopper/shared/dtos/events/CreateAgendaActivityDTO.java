package com.ftn.eventhopper.shared.dtos.events;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class CreateAgendaActivityDTO {
    private String name;
    private String description;
    private String locationName;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
}

