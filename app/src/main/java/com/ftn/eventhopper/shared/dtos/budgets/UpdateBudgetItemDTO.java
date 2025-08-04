package com.ftn.eventhopper.shared.dtos.budgets;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class UpdateBudgetItemDTO {
    private UUID id;
    private UUID categoryId;
    private double amount;
}
