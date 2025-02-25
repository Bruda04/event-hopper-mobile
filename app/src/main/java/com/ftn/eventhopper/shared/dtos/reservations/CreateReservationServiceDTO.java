package com.ftn.eventhopper.shared.dtos.reservations;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CreateReservationServiceDTO {
    private UUID eventId;
    private UUID productId;
    private LocalDateTime from;
    private LocalDateTime to;
}