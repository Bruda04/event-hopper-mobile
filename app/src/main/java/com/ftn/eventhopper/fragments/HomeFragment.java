package com.ftn.eventhopper.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.fragment.app.Fragment;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentManager;

import com.ftn.eventhopper.R;
import com.ftn.eventhopper.adapters.EventAdapter;
import com.ftn.eventhopper.adapters.ServiceAdapter;
import com.ftn.eventhopper.models.Event;
import com.ftn.eventhopper.models.Service;
import com.github.islamkhsh.CardSliderViewPager;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    private Button filterButton_events;
    private Button filterButton_solutions;

    public HomeFragment() {
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

    private ArrayList<Service> loadTop5Services() {
        ArrayList<Service> services = new ArrayList<>();
        Service service = new Service(
                ContextCompat.getDrawable(getContext(), R.drawable.slani_ketering_2022_postavka),
                getString(R.string.catering_title),
                getString(R.string.catering_secondary),
                getString(R.string.catering_description)
        );
        services.add(service);
        return services;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        EdgeToEdge.enable(requireActivity());

        // Set up the Event and Service sliders
        CardSliderViewPager cardSliderViewPager = view.findViewById(R.id.viewPager);
        cardSliderViewPager.setAdapter(new EventAdapter(loadTop5Events()));

        CardSliderViewPager cardSliderViewPager2 = view.findViewById(R.id.viewPager2);
        cardSliderViewPager2.setAdapter(new ServiceAdapter(loadTop5Services()));

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

        // Set up filter button for solutions
        filterButton_solutions = view.findViewById(R.id.filterButtonSolution);
        filterButton_solutions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                SolutionsFilterSort bottomSheet = new SolutionsFilterSort();
                bottomSheet.show(fragmentManager, "FilterSortBottomSheet");
            }
        });

        // Apply window insets for padding adjustment
        ViewCompat.setOnApplyWindowInsetsListener(view.findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        return view;
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
