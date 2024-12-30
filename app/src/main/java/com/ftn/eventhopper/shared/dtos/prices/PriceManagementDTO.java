package com.ftn.eventhopper.shared.dtos.prices;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class PriceManagementDTO {
    private UUID id;
    private double basePrice;
    private double discount;
    private double finalPrice;
    private UUID productId;
    private String productName;
}
