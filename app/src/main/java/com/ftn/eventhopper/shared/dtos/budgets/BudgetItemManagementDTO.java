package com.ftn.eventhopper.shared.dtos.budgets;

import com.ftn.eventhopper.shared.dtos.categories.SimpleCategoryDTO;
import com.ftn.eventhopper.shared.dtos.solutions.SimpleProductDTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Collection;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class BudgetItemManagementDTO {
    private UUID id;
    private SimpleCategoryDTO category;
    private double amount;
    private double minAmount;
    private boolean deletable;
    private Collection<SimpleProductDTO> purchasedProducts;
}
