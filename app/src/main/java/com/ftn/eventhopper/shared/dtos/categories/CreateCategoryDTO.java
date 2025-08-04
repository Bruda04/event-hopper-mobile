package com.ftn.eventhopper.shared.dtos.categories;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CreateCategoryDTO {
    private String name;
    private String description;
}
