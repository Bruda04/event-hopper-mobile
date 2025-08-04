package com.ftn.eventhopper.shared.dtos.reservations;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class CreateReservationProductDTO {
    private UUID eventId;
    private UUID productId;
}
