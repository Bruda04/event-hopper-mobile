package com.ftn.eventhopper.shared.dtos.solutions;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Collection;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class UpdateProductDTO {
    private String name;
    private String description;
    private Collection<String> pictures;
    private boolean available;
    private boolean visible;
    private Collection<UUID> eventTypesIds;
}
