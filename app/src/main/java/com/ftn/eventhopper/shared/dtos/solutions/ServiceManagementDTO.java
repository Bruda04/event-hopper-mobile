package com.ftn.eventhopper.shared.dtos.solutions;


import com.ftn.eventhopper.shared.dtos.categories.SimpleCategoryDTO;
import com.ftn.eventhopper.shared.dtos.eventTypes.SimpleEventTypeDTO;
import com.ftn.eventhopper.shared.dtos.prices.SimplePriceDTO;
import com.ftn.eventhopper.shared.models.solutions.ProductStatus;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Collection;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class ServiceManagementDTO {
    private UUID id;
    private String name;
    private String description;
    private SimplePriceDTO price;
    private ProductStatus status;
    private Collection<String> pictures;
    private boolean visible;
    private boolean available;
    private SimpleCategoryDTO category;
    private Collection<SimpleEventTypeDTO> eventTypes;

    private int durationMinutes;
    private int reservationWindowDays;
    private int cancellationWindowDays;
    private boolean autoAccept;
}
