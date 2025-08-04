package com.ftn.eventhopper.shared.dtos.budgets;

import com.ftn.eventhopper.shared.dtos.events.SimpleEventDTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Collection;

@Getter
@Setter
@NoArgsConstructor
public class BudgetManagementDTO {
    private double leftAmount;
    private Collection<BudgetItemManagementDTO> budgetItems;
    private SimpleEventDTO event;
}
