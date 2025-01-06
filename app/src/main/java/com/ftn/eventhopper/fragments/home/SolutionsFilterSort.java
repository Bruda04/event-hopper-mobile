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
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.slider.RangeSlider;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class SolutionsFilterSort extends BottomSheetDialogFragment {

    private HomeViewModel viewModel;
    private CheckBox product;
    private CheckBox service;
    private AutoCompleteTextView categoryAutoComplete;

    private ChipGroup eventTypes;
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
        eventTypes = view.findViewById(R.id.event_types_menu_solutions);
        availabilityAutoComplete = (AutoCompleteTextView) ((TextInputLayout) view.findViewById(R.id.availability_menu)).getEditText();
        priceSlider = view.findViewById(R.id.price_range_slider_filter);
        resetFiltersButton = view.findViewById(R.id.reset_button_solutions);
        sortRadioGroup = view.findViewById(R.id.sort_group);
        applyFiltersButton = view.findViewById(R.id.apply_filters_button_solutions);

        viewModel.fetchCategories();
        viewModel.fetchEventTypes();
        viewModel.getCategories().observe(getViewLifecycleOwner(), categories -> {
                    if (categories == null) return;

                    List<String> categoryNames = categories.stream().map(CategoryDTO::getName).collect(Collectors.toList());
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_dropdown_item_1line, categoryNames);
                    categoryAutoComplete.setAdapter(adapter);

                    categoryAutoComplete.setOnItemClickListener((parent, view1, position, id) -> {
                        CategoryDTO selectedCategory = categories.get(position);
                        List<String> eventTypesList = selectedCategory.getEventTypes().stream()
                                .map(SimpleEventTypeDTO::getName)
                                .collect(Collectors.toList());

                        eventTypes.removeAllViews();

                        for (String eventType : eventTypesList) {
                            Chip chip = new Chip(getContext());
                            chip.setText(eventType);
                            chip.setCheckable(true);
                            eventTypes.addView(chip);
                        }
                    });

                });


        ArrayList<String> availabilityStrings = new ArrayList<>();
        availabilityStrings.add("Available");
        availabilityStrings.add("Unavailable");
        ArrayAdapter<String> availabilityAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_dropdown_item_1line, availabilityStrings);
        availabilityAutoComplete.setAdapter(availabilityAdapter);

        priceSlider.setValues(priceSlider.getValueFrom(), priceSlider.getValueTo());

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
        selectedEventTypes = new ArrayList<>();
        ArrayList<UUID> selectedEventTypesUUIDs = new ArrayList<>();

        for (int i = 0; i < eventTypes.getChildCount(); i++) {
            Chip chip = (Chip) eventTypes.getChildAt(i);
            String chipText = chip.getText().toString();

            if (chip.isChecked() && selectedCategory != null) {

                for (SimpleEventTypeDTO eventType : selectedCategory.getEventTypes()) {
                    if (chipText.equalsIgnoreCase(eventType.getName())) {
                        selectedEventTypes.add(eventType);
                        selectedEventTypesUUIDs.add(eventType.getId());
                        break;
                    }
                }
            }

        }

        viewModel.setSelectedEventTypesProducts(selectedEventTypesUUIDs);


    }

    private void setSelectedCategory() {
        String selectedCategoryText = categoryAutoComplete.getText().toString().trim();
        viewModel.getCategories().observe(getViewLifecycleOwner(), categories -> {
            if (categories != null) {
                for (CategoryDTO categoryDTO : categories) {
                    if (categoryDTO.getName().equalsIgnoreCase(selectedCategoryText)) {
                        selectedCategory = categoryDTO;
                        viewModel.setSelectedCategory(categoryDTO.getId());
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
        String selectedAvailabilityString = availabilityAutoComplete.getText().toString().trim();
        if(selectedAvailabilityString.equals("Available")){
            selectedAvailability = true;
            viewModel.setAvailability(true);
        }else if(selectedAvailabilityString.equals("Unavailable")){
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
        this.product.setChecked(true);
        this.isServiceSelected = true;
        this.service.setChecked(true);
        this.selectedCategory = null;
        this.selectedEventTypes = null;
        this.selectedAvailability = null;
        if (priceSlider != null) {
            priceSlider.setValues(priceSlider.getValueFrom(), priceSlider.getValueTo());
        }
        sortRadioGroup.clearCheck();
        categoryAutoComplete.setText("");
        eventTypes.clearCheck();
        availabilityAutoComplete.setText("");
    }

    private void restorePreviousState(){
        if (viewModel.getIsProduct().getValue() != null) {
            product.setChecked(viewModel.getIsProduct().getValue());
        }

        if (viewModel.getIsService().getValue() != null) {
            service.setChecked(viewModel.getIsService().getValue());
        }

        if (viewModel.getSelectedCategory().getValue() != null) {
            String name = getCategoryNameById(viewModel.getSelectedCategory().getValue());
            categoryAutoComplete.setText(name);

            ArrayList<String> eventTypesList = getEventTypesByCategory(viewModel.getSelectedCategory().getValue());
            for (String eventType : eventTypesList) {
                Chip chip = new Chip(getContext());
                chip.setText(eventType);
                chip.setCheckable(true);
                eventTypes.addView(chip);
            }

            if (viewModel.getSelectedEventTypesProducts().getValue() != null && !viewModel.getSelectedEventTypesProducts().getValue().isEmpty()){

                for(UUID id: viewModel.getSelectedEventTypesProducts().getValue()){
                    for (int i = 0; i < eventTypes.getChildCount(); i++) {
                        Chip chip = (Chip) eventTypes.getChildAt(i);
                        String eventTypeName = getEventTypeNameById(id);
                        if (chip.getText().toString().equalsIgnoreCase(eventTypeName)) {
                            chip.setChecked(true);

                        }
                    }
                }
            }
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


        if ("price".equals(viewModel.getSortFieldProducts().getValue())) {
            sortRadioGroup.check(R.id.sort_solutions_by_price);
        } else if ("name".equals(viewModel.getSortFieldProducts().getValue())) {
            sortRadioGroup.check(R.id.sort_solutions_by_name);
        } else {
            sortRadioGroup.clearCheck();
        }
    }

    private ArrayList<String> getEventTypesByCategory(UUID id) {
        Collection<SimpleEventTypeDTO> eventTypes = new ArrayList<>();
        ArrayList<String> eventTypesNames = new ArrayList<>();
        ArrayList<CategoryDTO> categories = viewModel.getCategories().getValue();
        if(categories!= null){
            for(CategoryDTO categoryDTO: categories){
                if(categoryDTO.getId().equals(id)){
                    eventTypes = categoryDTO.getEventTypes();
                    break;
                }
            }
        }
        for(SimpleEventTypeDTO dto: eventTypes){
            eventTypesNames.add(dto.getName());
        }

        return eventTypesNames;
    }

    private String getEventTypeNameById(UUID id) {
        ArrayList<SimpleEventTypeDTO> eventTypeDTOS = viewModel.getEventTypes().getValue();
        if(eventTypeDTOS!=null){
            for(SimpleEventTypeDTO eventTypeDTO: eventTypeDTOS){
                if(eventTypeDTO.getId().equals(id)){
                    return eventTypeDTO.getName();
                }
            }
        }
        return "";
    }

    public String getCategoryNameById(UUID id){
        ArrayList<CategoryDTO> categories = viewModel.getCategories().getValue();
        if(categories!= null){
            for(CategoryDTO categoryDTO: categories){
                if(categoryDTO.getId().equals(id)){
                    return categoryDTO.getName();
                }
            }
        }
        return "";
    }

}
