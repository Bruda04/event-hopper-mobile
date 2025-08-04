package com.ftn.eventhopper.shared.dtos.categories;

import com.ftn.eventhopper.shared.models.categories.CategoryStatus;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Collection;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class UpdateCategoryDTO {
    private String name;
    private String description;
    private CategoryStatus status;
    private Collection<UUID> eventTypesIds;
}
