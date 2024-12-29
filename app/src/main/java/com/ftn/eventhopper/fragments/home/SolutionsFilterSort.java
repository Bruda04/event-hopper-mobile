package com.ftn.eventhopper.fragments.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioGroup;

import com.ftn.eventhopper.R;
import com.ftn.eventhopper.fragments.home.viewmodels.HomeViewModel;
import com.ftn.eventhopper.shared.dtos.categories.SimpleCategoryDTO;
import com.ftn.eventhopper.shared.dtos.eventTypes.SimpleEventTypeDTO;
import com.ftn.eventhopper.shared.dtos.events.SimpleEventDTO;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.slider.RangeSlider;

import java.util.ArrayList;

public class SolutionsFilterSort extends BottomSheetDialogFragment {

    private HomeViewModel viewModel;
    private CheckBox product;
    private CheckBox service;
    private AutoCompleteTextView categoryAutoComplete;

    //dodati za eventtypes
    private AutoCompleteTextView availabilityAutoComplete;
    private RangeSlider price;
    private RadioGroup sortRadioGroup;
    private Button applyFiltersButton;
    private Button resetFiltersButton;


    private SimpleCategoryDTO selectedCategory = null;
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
    private int totalCount = 0;

    @Override

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.filter_solution, container, false);

        // Initialize views
        sortRadioGroup = view.findViewById(R.id.sort_group);
        applyFiltersButton = view.findViewById(R.id.apply_filters_button);

        // Handle the apply button click
        applyFiltersButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get selected sorting option
                int selectedSortId = sortRadioGroup.getCheckedRadioButtonId();
                String sortOption = "";
                if (selectedSortId == R.id.sort_solutions_by_date) {
                    sortOption = "Date";
                } else if (selectedSortId == R.id.sort_solutions_by_date) {
                    sortOption = "Name";
                }

                dismiss();
            }
        });



        return view;
    }





}
