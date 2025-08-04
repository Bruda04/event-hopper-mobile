package com.ftn.eventhopper.shared.dtos.prices;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UpdatePriceDTO {
    private double basePrice;
    private double discount;
}
