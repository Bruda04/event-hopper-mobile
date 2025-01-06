package com.ftn.eventhopper.shared.dtos.eventTypes;

import com.ftn.eventhopper.shared.dtos.categories.SimpleCategoryDTO;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class EventTypeManagementDTO {
    private List<SimpleEventTypeDTO> eventTypes;
    private List<SimpleCategoryDTO> allCategories;
}
