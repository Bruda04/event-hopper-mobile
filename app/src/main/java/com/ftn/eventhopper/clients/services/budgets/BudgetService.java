package com.ftn.eventhopper.clients.services.budgets;

import com.ftn.eventhopper.shared.dtos.budgets.BudgetManagementDTO;
import com.ftn.eventhopper.shared.dtos.budgets.UpdateBudgetItemDTO;

import java.util.Collection;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface BudgetService {

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    @GET("budgets/{eventId}/management")
    Call<BudgetManagementDTO> getManagement(@Path("eventId") UUID eventId);

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    @PUT("budgets/{eventId}")
    Call<BudgetManagementDTO> update(@Path("eventId") UUID eventId, @Body Collection<UpdateBudgetItemDTO> budgetItems);
}
