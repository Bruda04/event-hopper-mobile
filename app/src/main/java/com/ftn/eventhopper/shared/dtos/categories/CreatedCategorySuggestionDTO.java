package com.ftn.eventhopper.shared.dtos.categories;

import com.ftn.eventhopper.shared.models.categories.CategoryStatus;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class CreatedCategorySuggestionDTO {
    private UUID id;
    private String name;
    private CategoryStatus status;
}