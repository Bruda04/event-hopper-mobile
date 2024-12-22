package com.ftn.eventhopper.shared.dtos.categories;


import com.ftn.eventhopper.shared.models.categories.CategoryStatus;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UpdateCategorySuggestionDTO {
    private CategoryStatus status;
}
