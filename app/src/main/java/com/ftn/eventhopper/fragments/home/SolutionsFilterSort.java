package com.ftn.eventhopper.fragments.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.lifecycle.ViewModelProvider;

import com.ftn.eventhopper.R;
import com.ftn.eventhopper.fragments.home.viewmodels.HomeViewModel;
import com.ftn.eventhopper.shared.dtos.categories.CategoryDTO;
import com.ftn.eventhopper.shared.dtos.eventTypes.SimpleEventTypeDTO;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.slider.RangeSlider;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.UUID;

public class SolutionsFilterSort extends BottomSheetDialogFragment {

    private HomeViewModel viewModel;
    private CheckBox product;
    private CheckBox service;
    private AutoCompleteTextView categoryAutoComplete;

    private AutoCompleteTextView eventTypeAutoComplete;
    private AutoCompleteTextView availabilityAutoComplete;
    private RangeSlider priceSlider;
    private RadioGroup sortRadioGroup;
    private Button applyFiltersButton;
    private Button resetFiltersButton;


    private CategoryDTO selectedCategory = null;
    private ArrayList<SimpleEventTypeDTO> selectedEventTypes = null;
    private Boolean selectedAvailability = null;
    private Boolean isServiceSelected = true;
    private Boolean isProductSelected = true;
    private Double minPrice = null;
    private Double maxPrice = null;
    private String selectedSortField ="";


    //Page properties:

    private int currentPage = 0;
    private final int pageSize = 10;


    @Override

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.filter_solution, container, false);
        viewModel = new ViewModelProvider(requireActivity()).get(HomeViewModel.class);

        product= view.findViewById(R.id.checkbox_products);
        service= view.findViewById(R.id.checkbox_services);
        categoryAutoComplete = (AutoCompleteTextView) ((TextInputLayout) view.findViewById(R.id.category_menu)).getEditText();
        eventTypeAutoComplete = (AutoCompleteTextView) ((TextInputLayout) view.findViewById(R.id.event_types_menu_solutions)).getEditText();
        availabilityAutoComplete = (AutoCompleteTextView) ((TextInputLayout) view.findViewById(R.id.availability_menu)).getEditText();
        priceSlider = view.findViewById(R.id.price_range_slider_filter);
        resetFiltersButton = view.findViewById(R.id.reset_button_solutions);
        sortRadioGroup = view.findViewById(R.id.sort_group);
        applyFiltersButton = view.findViewById(R.id.apply_filters_button_solutions);

        viewModel.fetchCategories();
        viewModel.getCategories().observe(getViewLifecycleOwner(), categories -> {
            ArrayList<String> categoriesNames = new ArrayList<>();
            for(CategoryDTO categoryDTO:categories){
                categoriesNames.add(categoryDTO.getName());
            }
            if(categories != null && categories.isEmpty()){
                ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_dropdown_item_1line, categoriesNames);
                categoryAutoComplete.setAdapter(categoryAdapter);
            }
        });

        ArrayList<String> availabilityStrings = new ArrayList<>();
        availabilityStrings.add("Available");
        availabilityStrings.add("Unavailable");
        ArrayAdapter<String> availabilityAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_dropdown_item_1line, availabilityStrings);
        availabilityAutoComplete.setAdapter(availabilityAdapter);


        restorePreviousState();

        applyFiltersButton.setOnClickListener( v-> {

            applyFilters();
            dismiss();
        });


        resetFiltersButton.setOnClickListener( v ->
        {
            setOnDefault();
            Toast.makeText(getContext(), "Filters reset to default", Toast.LENGTH_SHORT).show();
        });


        return view;
    }

    private void applyFilters(){
        this.setIsProduct();
        this.setIsService();
        this.setMinPrice();
        this.setMaxPrice();
        this.setAvailability();
        this.setSelectedSortField();
        this.setSelectedCategory();
        this.setSelectedEventTypes();
        String searchText = viewModel.getSearchTextProducts().getValue();
        UUID categoryId = null;
        if (this.selectedCategory != null){
            categoryId = selectedCategory.getId();
        }
        ArrayList<UUID> eventTypesIds = new ArrayList<>();
        if (this.selectedEventTypes != null){
            for(SimpleEventTypeDTO dto : selectedEventTypes){
                eventTypesIds.add(dto.getId());
            }
        }
        viewModel.fetchAllSolutionsPage(isProductSelected,isServiceSelected,categoryId,eventTypesIds, minPrice, maxPrice, selectedAvailability, searchText, selectedSortField, currentPage, pageSize);
    }

    private void setSelectedEventTypes() {
    }

    private void setSelectedCategory() {
        String selectedCategoryText = categoryAutoComplete.getText().toString().trim();
        viewModel.getCategories().observe(getViewLifecycleOwner(), categories -> {
            if (categories != null) {
                for (CategoryDTO categoryDTO : categories) {
                    if (categoryDTO.getName().equalsIgnoreCase(selectedCategoryText)) {
                        selectedCategory = categoryDTO;
                        break;
                    }
                }
            }
        });
    }

    private void setSelectedSortField() {
        int selectedSortId = sortRadioGroup.getCheckedRadioButtonId();
        if (selectedSortId == R.id.sort_solutions_by_price) {
            selectedSortField= "prices";
            viewModel.setSortFieldProducts("prices");
        } else if (selectedSortId == R.id.sort_solutions_by_name) {
            selectedSortField = "name";
            viewModel.setSortFieldProducts("name");
        }
    }

    private void setAvailability() {
        if(availabilityAutoComplete.getText().toString().trim().equals("Available")){
            selectedAvailability = true;
            viewModel.setAvailability(true);
        }else if(availabilityAutoComplete.getText().toString().trim().equals("Unavailable")){
            selectedAvailability = false;
            viewModel.setAvailability(false);
        }else{
            selectedAvailability = null;
            viewModel.setAvailability(null);
        }
    }

    private void setMaxPrice() {
        if (!priceSlider.getValues().isEmpty()) {
            maxPrice = Double.valueOf(priceSlider.getValues().get(1));
            viewModel.setMaxPrice(maxPrice);
        }
    }

    private void setMinPrice() {
        if (!priceSlider.getValues().isEmpty()) {
            minPrice = Double.valueOf(priceSlider.getValues().get(0));
            viewModel.setMinPrice(minPrice);
        }
    }

    private void setIsService() {
        isServiceSelected = service.isChecked();
        viewModel.setIsService(isServiceSelected);
    }

    private void setIsProduct() {
        isProductSelected = product.isChecked();
        viewModel.setIsProduct(isProductSelected);
    }


    private void setOnDefault(){
        this.isProductSelected = true;
        this.isServiceSelected = true;
        this.selectedCategory = null;
        this.selectedEventTypes = null;
        this.selectedAvailability = null;
        if (priceSlider != null) {
            priceSlider.setValues(priceSlider.getValueFrom(), priceSlider.getValueTo());
        }
        sortRadioGroup.clearCheck();
        categoryAutoComplete.setText("");
        //event types
        availabilityAutoComplete.setText("");
    }

    private void restorePreviousState(){
        if (viewModel.getIsProduct().getValue() != null) {
            Log.i("restore", viewModel.getIsProduct().getValue().toString());

            product.setChecked(viewModel.getIsProduct().getValue());
        }

        if (viewModel.getIsService().getValue() != null) {
            service.setChecked(viewModel.getIsService().getValue());
        }

        if (viewModel.getSelectedCategory().getValue() != null) {
            categoryAutoComplete.setText(viewModel.getSelectedCategory().getValue().toString());
        }

        if (viewModel.getAvailability().getValue() != null) {
            if(viewModel.getAvailability().getValue()){
                availabilityAutoComplete.setText("Available");
            }else{
                availabilityAutoComplete.setText("Unavailable");
            }
        }

        if (viewModel.getMinPrice().getValue() != null && viewModel.getMaxPrice().getValue() != null) {
            float minPrice = viewModel.getMinPrice().getValue().floatValue();
            float maxPrice = viewModel.getMaxPrice().getValue().floatValue();
            priceSlider.setValues(minPrice, maxPrice);
        }

        // Restore sorting radio group
        if ("price".equals(viewModel.getSortFieldProducts().getValue())) {
            sortRadioGroup.check(R.id.sort_solutions_by_price);
        } else if ("name".equals(viewModel.getSortFieldProducts().getValue())) {
            sortRadioGroup.check(R.id.sort_solutions_by_name);
        } else {
            sortRadioGroup.clearCheck();
        }
    }
}
