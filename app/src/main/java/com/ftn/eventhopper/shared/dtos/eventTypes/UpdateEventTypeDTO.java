package com.ftn.eventhopper.shared.dtos.eventTypes;

import com.ftn.eventhopper.shared.dtos.categories.SimpleCategoryDTO;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateEventTypeDTO {
    private String description;
    private List<SimpleCategoryDTO> suggestedCategories;
}
