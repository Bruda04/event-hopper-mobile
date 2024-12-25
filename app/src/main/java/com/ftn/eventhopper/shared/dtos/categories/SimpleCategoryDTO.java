package com.ftn.eventhopper.shared.dtos.categories;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class SimpleCategoryDTO {
    private UUID id;
    private String name;
}
