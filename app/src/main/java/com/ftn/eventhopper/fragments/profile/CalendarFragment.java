package com.ftn.eventhopper.fragments.profile;

import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.ftn.eventhopper.R;
import com.ftn.eventhopper.fragments.profile.viewmodels.ProfileViewModel;
import com.ftn.eventhopper.shared.dtos.events.SimpleEventDTO;
import com.ftn.eventhopper.shared.dtos.profile.ProfileForPersonDTO;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.spans.DotSpan;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CalendarFragment extends Fragment {
    private ProfileViewModel viewModel;
    private MaterialCalendarView calendarView;
    private List<LocalDateTime> eventDates;
    private Button toggleViewButton;
    private boolean isWeekView = false;

    private Set<SimpleEventDTO> attendingEvents;
    private List<SimpleEventDTO> myEvents;
    private SwipeRefreshLayout swipeRefreshLayout;

    public CalendarFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calendar, container, false);
        viewModel = new ViewModelProvider(requireActivity()).get(ProfileViewModel.class);
        this.fetchEvents(false);

        calendarView = view.findViewById(R.id.calendarView);

        adjustTileSize();

        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(() -> {
            fetchEvents(true);
        });

        // Example List of Event Dates
        eventDates = new ArrayList<>();
        eventDates.add(LocalDateTime.of(2025, 2, 10, 14, 0));
        eventDates.add(LocalDateTime.of(2025, 2, 10, 9, 30));
        eventDates.add(LocalDateTime.of(2025, 2, 20, 17, 45));

        // Highlight events
        calendarView.addDecorator(new EventDecorator(Color.RED, eventDates));


        // Handle date selection
        calendarView.setOnDateChangedListener((widget, date, selected) -> {
            LocalDateTime selectedDate = LocalDateTime.of(date.getYear(), date.getMonth(), date.getDay(), 0, 0);
            openEventFragment(selectedDate);
        });

        toggleViewButton = view.findViewById(R.id.toggleViewButton);

        toggleViewButton.setOnClickListener(v -> toggleCalendarView());

        return view;
    }


    private void fetchEvents(boolean refresh){
        //if you're told to refresh, or this is your first time and you have to
        if(refresh || viewModel.getProfile() == null){
            viewModel.fetchProfile();
        }
        viewModel.getProfileChanged().observe(getViewLifecycleOwner(), changed -> {
            if (changed) {
                ProfileForPersonDTO profile = viewModel.getProfile();
                this.attendingEvents = profile.getAttendingEvents();
                this.myEvents = profile.getMyEvents();
            }
            swipeRefreshLayout.setRefreshing(false);
        });

    }

    private void toggleCalendarView() {
        if (isWeekView) {
            // Switch to Month View
            calendarView.state().edit()
                    .setCalendarDisplayMode(CalendarMode.MONTHS)
                    .commit();
            toggleViewButton.setText("Week View");
        } else {
            // Switch to Week View
            calendarView.state().edit()
                    .setCalendarDisplayMode(CalendarMode.WEEKS)
                    .commit();
            toggleViewButton.setText("Month View");
        }
        isWeekView = !isWeekView;
    }

    private void adjustTileSize() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        requireActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int screenWidth = displayMetrics.widthPixels;

        int tileSize = screenWidth / 7;
        calendarView.setTileSize(tileSize);
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

        public EventDecorator(int color, Set<LocalDateTime> dateTimes) {
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
