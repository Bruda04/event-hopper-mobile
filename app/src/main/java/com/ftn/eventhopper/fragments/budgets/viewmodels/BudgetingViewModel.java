package com.ftn.eventhopper.fragments.budgets.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ftn.eventhopper.clients.ClientUtils;
import com.ftn.eventhopper.shared.dtos.budgets.BudgetItemManagementDTO;
import com.ftn.eventhopper.shared.dtos.budgets.BudgetManagementDTO;
import com.ftn.eventhopper.shared.dtos.budgets.UpdateBudgetItemDTO;
import com.ftn.eventhopper.shared.dtos.categories.CategoryDTO;
import com.ftn.eventhopper.shared.dtos.categories.CategorySuggestionDTO;
import com.ftn.eventhopper.shared.dtos.categories.SimpleCategoryDTO;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BudgetingViewModel extends ViewModel {
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    private final MutableLiveData<BudgetManagementDTO> budgetLiveData = new MutableLiveData<>();

    public LiveData<BudgetManagementDTO> getBudget() {
        return budgetLiveData;
    }

    private Double spentAmount = 0.0;

    public void fetchBudget(UUID eventId) {
        Call<BudgetManagementDTO> call = ClientUtils.budgetService.getManagement(eventId);
        call.enqueue(new Callback<BudgetManagementDTO>() {
            @Override
            public void onResponse(Call<BudgetManagementDTO> call, Response<BudgetManagementDTO> response) {
                if (response.isSuccessful()) {
                    budgetLiveData.postValue(response.body());
                    spentAmount = response.body().getBudgetItems().stream().mapToDouble(BudgetItemManagementDTO::getAmount).sum() - response.body().getLeftAmount();


                } else {
                    try {
                        String errorBody = response.errorBody().string();
                        JSONObject jsonObject = new JSONObject(errorBody);
                        String errorMessageString = jsonObject.getString("message");
                        errorMessage.postValue(errorMessageString);

                    } catch (Exception e) {
                        errorMessage.postValue("An unexpected error occurred.");
                    }
                }
            }

            @Override
            public void onFailure(Call<BudgetManagementDTO> call, Throwable t) {
                errorMessage.postValue(t.getMessage());
            }
        });
    }


    public void addBudgetItem(SimpleCategoryDTO selectedCategory) {
        BudgetManagementDTO budget = budgetLiveData.getValue();
        if (budget == null) {
            return;
        }
        BudgetItemManagementDTO budgetItem = new BudgetItemManagementDTO();
        budgetItem.setCategory(selectedCategory);
        budgetItem.setAmount(0);
        budgetItem.setMinAmount(0);
        budgetItem.setDeletable(true);
        budgetItem.setId(null);
        budgetItem.setPurchasedProducts(new ArrayList<>());

        budget.getBudgetItems().add(budgetItem);
        budgetLiveData.postValue(budget);
    }

    public void saveBudget(ArrayList<BudgetItemManagementDTO> items) {
        BudgetManagementDTO budget = budgetLiveData.getValue();
        if (budget == null) {
            return;
        }
        
        Call<BudgetManagementDTO> call = ClientUtils.budgetService.update(budget.getEvent().getId(), convertToUpdateDTO(items));
        call.enqueue(new Callback<BudgetManagementDTO>() {
            @Override
            public void onResponse(Call<BudgetManagementDTO> call, Response<BudgetManagementDTO> response) {
                if (response.isSuccessful()) {
                    budgetLiveData.postValue(response.body());
                } else {
                    try {
                        String errorBody = response.errorBody().string();
                        JSONObject jsonObject = new JSONObject(errorBody);
                        String errorMessageString = jsonObject.getString("message");
                        errorMessage.postValue(errorMessageString);

                    } catch (Exception e) {
                        errorMessage.postValue("An unexpected error occurred.");
                    }
                }
            }

            @Override
            public void onFailure(Call<BudgetManagementDTO> call, Throwable t) {
                errorMessage.postValue(t.getMessage());
            }
        });
    }

    private Collection<UpdateBudgetItemDTO> convertToUpdateDTO(ArrayList<BudgetItemManagementDTO> items) {
        Collection<UpdateBudgetItemDTO> updateItems = new ArrayList<>();
        for (BudgetItemManagementDTO item : items) {
            UpdateBudgetItemDTO updateItem = new UpdateBudgetItemDTO();
            updateItem.setId(item.getId());
            updateItem.setAmount(item.getAmount());
            updateItem.setCategoryId(item.getCategory().getId());
            updateItems.add(updateItem);
        }
        return updateItems;
    }

    public void removeBudgetItem(UUID id) {
        BudgetManagementDTO budget = budgetLiveData.getValue();
        if (budget == null) {
            return;
        }
        budget.getBudgetItems().removeIf(item -> item.getId().equals(id));
        budgetLiveData.postValue(budget);
    }

    public void updateLeftAmount(ArrayList<BudgetItemManagementDTO> items) {
        BudgetManagementDTO budget = budgetLiveData.getValue();
        if (budget == null) {
            return;
        }
        double totalAmount = items.stream().mapToDouble(BudgetItemManagementDTO::getAmount).sum();
        budget.setLeftAmount(totalAmount - spentAmount);
        budgetLiveData.postValue(budget);
    }
}
