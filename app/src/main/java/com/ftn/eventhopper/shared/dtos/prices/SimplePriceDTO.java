package com.ftn.eventhopper.shared.dtos.prices;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class SimplePriceDTO {
    private UUID id;
    private double basePrice;
    private double discount;
    private double finalPrice;
    private LocalDateTime timestamp;
}
