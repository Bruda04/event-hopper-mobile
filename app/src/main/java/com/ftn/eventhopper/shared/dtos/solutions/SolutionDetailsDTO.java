package com.ftn.eventhopper.shared.dtos.solutions;


import com.ftn.eventhopper.shared.dtos.categories.SimpleCategoryDTO;
import com.ftn.eventhopper.shared.dtos.comments.SimpleCommentDTO;
import com.ftn.eventhopper.shared.dtos.eventTypes.SimpleEventTypeDTO;
import com.ftn.eventhopper.shared.dtos.prices.SimplePriceDTO;
import com.ftn.eventhopper.shared.dtos.users.serviceProvider.SimpleServiceProviderDTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Collection;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class SolutionDetailsDTO {
    private UUID id;
    private String name;
    private String description;
    private boolean available;
    private SimplePriceDTO price;
    private SimpleCategoryDTO category;
    private Collection<SimpleEventTypeDTO> eventTypes;
    private Collection<String> pictures;
    private double rating;
    private Collection<SimpleCommentDTO> comments;
    private SimpleServiceProviderDTO provider;
    private boolean service;
    private int durationMinutes;
    private int reservationWindowDays;
    private int cancellationWindowDays;
}
