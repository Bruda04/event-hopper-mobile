package com.ftn.eventhopper.fragments.home;

import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.ftn.eventhopper.R;
import com.ftn.eventhopper.adapters.EventAdapter;
import com.ftn.eventhopper.shared.models.Event;
import com.github.islamkhsh.CardSliderViewPager;

import java.util.ArrayList;


public class HomeEventsFragment extends Fragment {

    private Button filterButton_events;



    public HomeEventsFragment() {
        // Required empty public constructor
    }
    private ArrayList<Event> loadTop5Events() {
        ArrayList<Event> events = new ArrayList<>();

        Event event = new Event(
                ContextCompat.getDrawable(getContext(), R.drawable.wedding), // Drawable resource
                getString(R.string.jennet_and_john_wedding),    // String resource for title
                getString(R.string.wedding_secondary),         // String resource for secondary text
                getString(R.string.wedding_supporting)         // String resource for description
        );
        Event event2 = new Event(
                ContextCompat.getDrawable(getContext(), R.drawable.concert), // Drawable resource
                getString(R.string.concert_title),    // String resource for title
                getString(R.string.concert_secondary),         // String resource for secondary text
                getString(R.string.concert_supporting)         // String resource for description
        );
        events.add(event);
        events.add(event2);

        return events;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home_events, container, false);

        // Set up the Event and Service sliders
        CardSliderViewPager cardSliderViewPager = view.findViewById(R.id.viewPagerEvents);
        cardSliderViewPager.setAdapter(new EventAdapter(loadTop5Events()));


        // Set up filter button for events
        filterButton_events = view.findViewById(R.id.filterButton);
        filterButton_events.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                EventsFilterSort bottomSheet = new EventsFilterSort();
                bottomSheet.show(fragmentManager, "FilterSortBottomSheet");
            }
        });

        return view;
        //return inflater.inflate(R.layout.fragment_home_events, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}