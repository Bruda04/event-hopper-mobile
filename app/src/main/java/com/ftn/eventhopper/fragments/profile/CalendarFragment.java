package com.ftn.eventhopper.fragments.profile;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.ftn.eventhopper.R;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.spans.DotSpan;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class CalendarFragment extends Fragment {

    private MaterialCalendarView calendarView;
    private Button prevMonthButton, nextMonthButton;
    private TextView currentMonthText;
    private List<LocalDateTime> eventDates;

    public CalendarFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calendar, container, false);

        calendarView = view.findViewById(R.id.calendarView);
        prevMonthButton = view.findViewById(R.id.prevMonthButton);
        nextMonthButton = view.findViewById(R.id.nextMonthButton);
        currentMonthText = view.findViewById(R.id.currentMonthText);

        // Example List of Event Dates
        eventDates = new ArrayList<>();
        eventDates.add(LocalDateTime.of(2025, 2, 10, 14, 0));
        eventDates.add(LocalDateTime.of(2025, 2, 15, 9, 30));
        eventDates.add(LocalDateTime.of(2025, 2, 20, 17, 45));

        // Set the initial month
        updateMonthText();

        // Highlight events
        calendarView.addDecorator(new EventDecorator(Color.RED, eventDates));

        // Handle date selection
        calendarView.setOnDateChangedListener((widget, date, selected) -> {
            LocalDateTime selectedDate = LocalDateTime.of(date.getYear(), date.getMonth(), date.getDay(), 0, 0);
            openEventFragment(selectedDate);
        });

        // Previous Month Button
        prevMonthButton.setOnClickListener(v -> {
            calendarView.goToPrevious();
            updateMonthText();
        });

        // Next Month Button
        nextMonthButton.setOnClickListener(v -> {
            calendarView.goToNext();
            updateMonthText();
        });

        return view;
    }

    private void updateMonthText() {
        CalendarDay currentDate = calendarView.getCurrentDate();
        String monthYear = currentDate.getMonth() + " " + currentDate.getYear();
        currentMonthText.setText(monthYear);
    }

    private void openEventFragment(LocalDateTime date) {
        Bundle bundle = new Bundle();
        bundle.putString("selectedDate", date.toString());
        NavHostFragment.findNavController(this).navigate(R.id.action_to_event_page, bundle);
    }

    // Event Decorator Class for Highlighting Event Dates
    private static class EventDecorator implements DayViewDecorator {
        private final int color;
        private final HashSet<CalendarDay> dates;

        public EventDecorator(int color, List<LocalDateTime> dateTimes) {
            this.color = color;
            this.dates = new HashSet<>();
            for (LocalDateTime dateTime : dateTimes) {
                dates.add(CalendarDay.from(dateTime.getYear(), dateTime.getMonthValue(), dateTime.getDayOfMonth()));
            }
        }

        @Override
        public boolean shouldDecorate(CalendarDay day) {
            return dates.contains(day);
        }

        @Override
        public void decorate(DayViewFacade view) {
            view.addSpan(new DotSpan(8, color));
        }
    }
}
