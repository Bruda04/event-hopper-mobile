package com.ftn.eventhopper.fragments.home;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.lifecycle.ViewModelProvider;

import com.ftn.eventhopper.R;
import com.ftn.eventhopper.fragments.home.viewmodels.HomeViewModel;
import com.ftn.eventhopper.shared.dtos.categories.CategoryDTO;
import com.ftn.eventhopper.shared.dtos.eventTypes.SimpleEventTypeDTO;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.textfield.TextInputLayout;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

public class EventsFilterSort extends BottomSheetDialogFragment {

    private HomeViewModel viewModel;
    private RadioGroup sortRadioGroup;
    private Button applyFiltersButton;
    private Button resetFiltersButton;
    private Button openDatePickerButton;
    private AutoCompleteTextView cityAutoComplete;
    private AutoCompleteTextView eventTypeAutoComplete;
    private String selectedCity = "";
    private SimpleEventTypeDTO eventType = null;
    private String selectedSortField = "";
    private String selectedDate = "";

    //Page properties:

    private int currentPage = 0;
    private final int pageSize = 10;


    @Override

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.filter_events, container, false);
        viewModel = new ViewModelProvider(requireActivity()).get(HomeViewModel.class);

        this.sortRadioGroup = view.findViewById(R.id.sort_group);

        openDatePickerButton = view.findViewById(R.id.open_date_picker_button);

        cityAutoComplete = (AutoCompleteTextView) ((TextInputLayout) view.findViewById(R.id.location_menu)).getEditText();
        eventTypeAutoComplete = (AutoCompleteTextView) ((TextInputLayout) view.findViewById(R.id.event_type_menu)).getEditText();

        viewModel.fetchCities();
        viewModel.getCities().observe(getViewLifecycleOwner(), cities -> {
            if (cities != null && !cities.isEmpty()) {
                ArrayAdapter<String> cityAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_dropdown_item_1line, cities);
                cityAutoComplete.setAdapter(cityAdapter);
            }
        });

        viewModel.fetchEventTypes();
        viewModel.getEventTypes().observe(getViewLifecycleOwner(), eventTypes -> {
            ArrayList<String> eventTypesNames = new ArrayList<>();
            if(eventTypes != null && !eventTypes.isEmpty()) {
                Log.i("if","upao");
                for(SimpleEventTypeDTO eventTypeDTO: eventTypes){
                    Log.i("for",eventTypeDTO.getName());
                    eventTypesNames.add(eventTypeDTO.getName());
                }
                ArrayAdapter<String> eventTypeAdapter = new ArrayAdapter<>(getContext(),android.R.layout.simple_dropdown_item_1line, eventTypesNames);
                eventTypeAutoComplete.setAdapter(eventTypeAdapter);
            }else if(eventTypes == null){
                Log.i("null","null je brt");
            }else if(eventTypes.isEmpty()){
                Log.i("prazan","prazan je lol");
            }else{
                Log.i("ne znam","nez");
            }
        });


        restorePreviousState();


        applyFiltersButton = view.findViewById(R.id.apply_filters_button);
        applyFiltersButton.setOnClickListener(v -> {
            applyFilters();
            //setOnDefault();
            dismiss();
        });
        this.setUpDatePickerButton();

        resetFiltersButton = view.findViewById(R.id.reset_button);
        resetFiltersButton.setOnClickListener(v -> {
            setOnDefault();
            Toast.makeText(getContext(), "Filters reset to default", Toast.LENGTH_SHORT).show();
        });

        return view;
    }
    private void applyFilters() {

        this.setSelectedSortOption();
        this.setSelectedCity();
        this.setSelectedEventType();
        String searchText = viewModel.getSearchTextEvents().getValue();
        UUID eventTypeId = null;
        if (this.eventType != null){
            Log.i("event type","upao");
            eventTypeId = eventType.getId();
        }
        String sortDirection = viewModel.getSortDirectionEvents().getValue();
        viewModel.fetchAllEventsPage(selectedCity,eventTypeId, selectedDate, searchText, selectedSortField,sortDirection ,currentPage, pageSize);
    }


    private void setSelectedSortOption() {
        int selectedSortId = sortRadioGroup.getCheckedRadioButtonId();
        if (selectedSortId == R.id.sort_events_by_date) {
            selectedSortField= "time";
            viewModel.setSortFieldEvents("time");
        } else if (selectedSortId == R.id.sort_events_by_name) {
            selectedSortField = "name";
            viewModel.setSortFieldEvents("name");
        }
    }

    private void setSelectedCity(){
        selectedCity = cityAutoComplete.getText().toString().trim();
        viewModel.setSelectedCity(selectedCity);
    }

    private void setSelectedEventType(){
        String selectedEventTypeText = eventTypeAutoComplete.getText().toString().trim();
        Log.i("setter", selectedEventTypeText);
        viewModel.getEventTypes().observe(getViewLifecycleOwner(), eventTypes -> {
            if (eventTypes != null) {
                Log.i("setter", "upao u razlicito od null");
                for (SimpleEventTypeDTO eventTypeDTO : eventTypes) {
                    Log.i("setter", eventTypeDTO.getName());
                    if (eventTypeDTO.getName().equals(selectedEventTypeText)) {
                        Log.i("setter", "upao");
                        this.eventType = eventTypeDTO;
                        viewModel.setSelectedEventTypeEvents(eventTypeDTO.getId());
                        break;
                    }
                }
            }
        });

    }


    private void setUpDatePickerButton() {
        openDatePickerButton.setText("Choose date");
        openDatePickerButton.setOnClickListener(v -> {
            showDatePicker();
        });
    }

    private void showDatePicker() {
        MaterialDatePicker<Long> datePicker = MaterialDatePicker.Builder.datePicker()
                .setTitleText("Select a Date")
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                .setCalendarConstraints(new CalendarConstraints.Builder().build())
                .build();

        datePicker.show(getChildFragmentManager(), "DATE_PICKER");

        datePicker.addOnPositiveButtonClickListener(selection -> {

            Date date = new Date(selection);
            String formattedDate = formatDate(date);

            this.selectedDate = formattedDate;
            viewModel.setSelectedDate(selectedDate);
            Toast.makeText(getContext(), "Selected Date: " + formattedDate, Toast.LENGTH_SHORT).show();
        });
    }


    public String formatDate(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());
        return sdf.format(date);
    }

    private void setOnDefault(){
        this.selectedSortField = "";
        this.selectedCity = "";
        this.eventType = null;
        this.selectedDate = "";

        viewModel.setSortFieldEvents("");
    //        viewModel.setSortDirectionEvents("");

        sortRadioGroup.clearCheck();
        cityAutoComplete.setText("");
        eventTypeAutoComplete.setText("");
        openDatePickerButton.setText("Choose date");
    }

    private void restorePreviousState() {
        if (viewModel.getSelectedCity().getValue() != null ) {
            cityAutoComplete.setText(viewModel.getSelectedCity().getValue());
        }

        if (viewModel.getSelectedEventTypeEvents().getValue() != null ) {
            String name = getEventTypeNameById(viewModel.getSelectedEventTypeEvents().getValue());
            eventTypeAutoComplete.setText(name);
        }

        if (viewModel.getSelectedDate().getValue() != null || !viewModel.getSelectedDate().getValue().isEmpty() ) {
            openDatePickerButton.setText(viewModel.getSelectedDate().getValue().replace("T"," "));
        }else{
            openDatePickerButton.setText("Choose date");
        }

        if ("time".equals(viewModel.getSortFieldEvents().getValue())) {
            sortRadioGroup.check(R.id.sort_events_by_date);
        } else if ("name".equals(viewModel.getSortFieldEvents().getValue())) {
            sortRadioGroup.check(R.id.sort_events_by_name);
        }
    }

    public String getEventTypeNameById(UUID id){
        ArrayList<SimpleEventTypeDTO> eventTypes = viewModel.getEventTypes().getValue();
        if(eventTypes!= null){
            for(SimpleEventTypeDTO eventTypeDTO: eventTypes){
                if(eventTypeDTO.getId().equals(id)){
                    return eventTypeDTO.getName();
                }
            }
        }
        return "";
    }
}
