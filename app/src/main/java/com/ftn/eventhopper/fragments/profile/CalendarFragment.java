package com.ftn.eventhopper.fragments.profile;

import android.app.AlertDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

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
import com.prolificinteractive.materialcalendarview.*;
import com.prolificinteractive.materialcalendarview.spans.DotSpan;

import java.time.LocalDate;
import java.util.*;

public class CalendarFragment extends Fragment {
    private ProfileViewModel viewModel;
    private MaterialCalendarView calendarView;
    private Button toggleViewButton;
    private boolean isWeekView = false;
    private SwipeRefreshLayout swipeRefreshLayout;

    private List<SimpleEventDTO> myEvents = new ArrayList<>(), attendingEvents = new ArrayList<>();
    private HashMap<CalendarDay, List<SimpleEventDTO>> eventsByDate = new HashMap<>();

    public CalendarFragment() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calendar, container, false);
        viewModel = new ViewModelProvider(requireActivity()).get(ProfileViewModel.class);
        calendarView = view.findViewById(R.id.calendarView);

        adjustTileSize();

        calendarView.setOnMonthChangedListener((widget, date) -> {
            Log.d("CalendarView", "Visible month: " + date.getMonth() + "/" + date.getYear());
        });

        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(() -> fetchProfileInformation(true));
        fetchProfileInformation(false);

        calendarView.setOnDateChangedListener((widget, date, selected) -> {
            CalendarDay selectedDay = CalendarDay.from(date.getYear(), date.getMonth(), date.getDay());
            List<SimpleEventDTO> eventsOnDate = eventsByDate.get(selectedDay);
            if (eventsOnDate != null) {
                if (eventsOnDate.size() == 1) {
                    openEventFragment(eventsOnDate.get(0));
                } else {
                    showEventSelectionDialog(eventsOnDate);
                }
            }
        });

        toggleViewButton = view.findViewById(R.id.toggleViewButton);
        toggleViewButton.setOnClickListener(v -> toggleCalendarView());

        return view;
    }

    private void fetchProfileInformation(boolean refresh) {
        if (refresh || viewModel.getProfile() == null) {
            viewModel.fetchProfile();
        }

        viewModel.getProfileChanged().observe(getViewLifecycleOwner(), changed -> {
            if (changed != null && changed) {
                ProfileForPersonDTO profile = viewModel.getProfile();
                attendingEvents = new ArrayList<>(profile.getAttendingEvents());
                myEvents = profile.getMyEvents() != null ? new ArrayList<>(profile.getMyEvents()) : new ArrayList<>();
                updateEventDecorators(attendingEvents, myEvents);
            }
            swipeRefreshLayout.setRefreshing(false);
        });
    }

    private void updateEventDecorators(List<SimpleEventDTO> attendingEvents, List<SimpleEventDTO> myEvents) {
        eventsByDate.clear();
        Map<CalendarDay, List<SimpleEventDTO>> allEventsByDay = new HashMap<>();

        Set<CalendarDay> organizerDays = new HashSet<>();
        Set<CalendarDay> attendeeDays = new HashSet<>();
        Set<CalendarDay> multiEventDays = new HashSet<>();

        // Track how many events on each day
        for (SimpleEventDTO event : attendingEvents) {
            LocalDate date = event.getTime().toLocalDate();
            CalendarDay day = CalendarDay.from(date.getYear(), date.getMonthValue() - 1, date.getDayOfMonth());
            allEventsByDay.computeIfAbsent(day, k -> new ArrayList<>()).add(event);
            attendeeDays.add(day);
        }

        for (SimpleEventDTO event : myEvents) {
            LocalDate date = event.getTime().toLocalDate();
            CalendarDay day = CalendarDay.from(date.getYear(), date.getMonthValue() - 1, date.getDayOfMonth());
            allEventsByDay.computeIfAbsent(day, k -> new ArrayList<>()).add(event);
            organizerDays.add(day);
        }

        for (Map.Entry<CalendarDay, List<SimpleEventDTO>> entry : allEventsByDay.entrySet()) {
            CalendarDay day = entry.getKey();
            List<SimpleEventDTO> events = entry.getValue();
            eventsByDate.put(day, events);
            if (events.size() > 1) {
                multiEventDays.add(day);
            }
        }

        // Remove overlap so we don't double-decorate
        organizerDays.removeAll(multiEventDays);
        attendeeDays.removeAll(organizerDays);
        attendeeDays.removeAll(multiEventDays);

        calendarView.removeDecorators();
        calendarView.addDecorator(new DotDecorator(Color.parseColor("#9C27B0"), multiEventDays)); // Purple for multiple
        calendarView.addDecorator(new DotDecorator(Color.YELLOW, organizerDays)); // Yellow for organizing
        calendarView.addDecorator(new DotDecorator(Color.BLUE, attendeeDays)); // Blue for attending
        calendarView.invalidateDecorators();
    }

    private void toggleCalendarView() {
        if (isWeekView) {
            calendarView.state().edit().setCalendarDisplayMode(CalendarMode.MONTHS).commit();
            toggleViewButton.setText("Week View");
        } else {
            calendarView.state().edit().setCalendarDisplayMode(CalendarMode.WEEKS).commit();
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

    private void openEventFragment(SimpleEventDTO eventDTO) {
        Bundle bundle = new Bundle();
        bundle.putString("event_id", eventDTO.getId().toString());
        NavHostFragment.findNavController(this).navigate(R.id.action_to_event_page, bundle);
    }

    private void showEventSelectionDialog(List<SimpleEventDTO> events) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Select an Event");

        ListView eventListView = new ListView(requireContext());
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_list_item_1);
        for (SimpleEventDTO event : events) {
            String label = event.getName();
            if (myEvents.contains(event)) {
                label += " ⭐ (Organizing)";
            } else {
                label += " 👤 (Attending)";
            }
            adapter.add(label);
        }
        eventListView.setAdapter(adapter);
        builder.setView(eventListView);

        AlertDialog dialog = builder.create();
        dialog.show();

        eventListView.setOnItemClickListener((parent, view, position, id) -> {
            openEventFragment(events.get(position));
            dialog.dismiss();
        });
    }

    private static class DotDecorator implements DayViewDecorator {
        private final int color;
        private final HashSet<CalendarDay> dates;

        public DotDecorator(int color, Collection<CalendarDay> dates) {
            this.color = color;
            this.dates = new HashSet<>(dates);
        }

        @Override
        public boolean shouldDecorate(CalendarDay day) {
            return dates.contains(day);
        }

        @Override
        public void decorate(DayViewFacade view) {
            view.addSpan(new DotSpan(12, color));
        }
    }
}
