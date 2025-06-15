package com.ftn.eventhopper.shared.dtos.solutions;


import com.ftn.eventhopper.shared.dtos.categories.SimpleCategoryDTO;
import com.ftn.eventhopper.shared.dtos.eventTypes.SimpleEventTypeDTO;
import com.ftn.eventhopper.shared.dtos.prices.SimplePriceDTO;
import com.ftn.eventhopper.shared.models.solutions.ProductStatus;

import java.util.List;
import java.util.UUID;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ProductForManagementDTO {
    private UUID id;
    private String name;
    private String description;
    private List<String> pictures;
    private boolean available;
    private boolean visible;
    private ProductStatus status;
    private SimplePriceDTO price;
    private SimpleCategoryDTO category;
    private List<SimpleEventTypeDTO> eventTypes;
}

