package com.ftn.eventhopper.shared.dtos.reservations;

import com.ftn.eventhopper.shared.dtos.events.SimpleEventDTO;
import com.ftn.eventhopper.shared.dtos.solutions.SimpleProductDTO;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CreatedReservationServiceDTO {
    private UUID id;
    private SimpleEventDTO event;
    private SimpleProductDTO product;
    private LocalDateTime timestamp;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
}