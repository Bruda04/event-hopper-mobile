package com.ftn.eventhopper.fragments.home;

import android.content.Context;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.ftn.eventhopper.R;
import com.ftn.eventhopper.adapters.events.EventAdapter;
import com.ftn.eventhopper.adapters.events.TopEventAdapter;
import com.ftn.eventhopper.clients.services.auth.UserService;
import com.ftn.eventhopper.fragments.home.viewmodels.HomeViewModel;
import com.ftn.eventhopper.shared.dtos.events.SimpleEventDTO;
import com.github.islamkhsh.CardSliderViewPager;
import com.google.android.material.search.SearchBar;
import com.google.android.material.search.SearchView;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.UUID;


public class HomeEventsFragment extends Fragment implements SensorEventListener {

    private HomeViewModel viewModel;
    private CardSliderViewPager topEventsRecyclerView;
    private RecyclerView allEventsRecyclerView;
    private Button filterButton;
    private Button searchButton;
    private SearchView searchView;
    private SearchBar searchBar;
    private String searchText = "";
    private String sortDirection = "";
    private Button nextPage;
    private Button previousPage;
    private TextView pager;

    //Page properties:

    private int currentPage = 0;
    private final int pageSize = 10;
    private int totalCount;

    private int totalPages;

    private int lowerNumber;
    private int higherNumber;

    private SensorManager sensorManager;
    private static final int SHAKE_THRESHOLD = 800;
    private static final long MIN_TIME_BETWEEN_SHAKES = 500; // Minimalni razmak između shake događaja u milisekundama
    private long lastFetchTime = 0; // Vremenska oznaka poslednjeg fetch-a
    private long lastUpdate;
    private float last_x;
    private float last_y;
    private float last_z;


    public HomeEventsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home_events, container, false);
        viewModel = new ViewModelProvider(requireActivity()).get(HomeViewModel.class);


        this.searchView = view.findViewById(R.id.search_view_events);
        this.searchBar = view.findViewById(R.id.search_bar_events);
        this.searchButton = view.findViewById(R.id.search_button);

        this.topEventsRecyclerView = view.findViewById(R.id.viewPagerEvents);
        this.allEventsRecyclerView = view.findViewById(R.id.recyclerView_allevents);

        this.nextPage = view.findViewById(R.id.forward_arrow_button);
        this.previousPage = view.findViewById(R.id.back_arrow_button);
        this.pager = view.findViewById(R.id.pager);



        //viewModel.fetchAllEvents();
        UUID usersId = UUID.fromString(UserService.getJwtClaim(
                UserService.getJwtToken(),"id"
        ));
        viewModel.fetchTopEvents(usersId);

        viewModel.getTop5Events().observe(getViewLifecycleOwner(), topEvents ->
        {
            if (topEvents!=null){
                topEventsRecyclerView.setVisibility(View.VISIBLE);
                this.setTop5(topEvents);
            }
        });

        this.fetchEvents();

        viewModel.getEventsPage().observe(getViewLifecycleOwner(), pagedResponse -> {
            if (pagedResponse != null && pagedResponse.getContent() != null) {
                allEventsRecyclerView.setVisibility(View.VISIBLE);
                this.setAll(new ArrayList<>(pagedResponse.getContent()));
                totalCount = (int) viewModel.getEventsPage().getValue().getTotalElements();
                totalPages = (int) viewModel.getEventsPage().getValue().getTotalPages();
                setPager();
            }
        });


        filterButton = view.findViewById(R.id.filterButton);
        filterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                EventsFilterSort bottomSheet = new EventsFilterSort();
                bottomSheet.show(fragmentManager, "FilterSortBottomSheet");
            }
        });

        searchButton.setOnClickListener(v -> {
            searchText = searchView.getText().toString();
            searchBar.setText(searchText);
            searchView.hide();
            viewModel.setSearchTextEvents(searchText);
            this.fetchEvents();

        });

        previousPage.setOnClickListener(v ->
        {
            currentPage--;
            this.fetchEvents();
            setPager();
        });

        nextPage.setOnClickListener(v ->{
            currentPage++;
            this.fetchEvents();
            setPager();
        });


        return view;

    }

    @Override
    public void onResume() {
        super.onResume();
        sensorManager = (SensorManager) requireActivity().getSystemService(Context.SENSOR_SERVICE);
        sensorManager.registerListener(this,
                sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        //binding = null;
        sensorManager.unregisterListener(this);

    }

    void setPager(){

        if(totalPages == 0){
            lowerNumber = 0;
        }else{
            lowerNumber = currentPage+1;
        }
        higherNumber = totalPages;

        String pagerText = lowerNumber + "/" + higherNumber;
        pager.setText(pagerText);


        if (currentPage == 0){
            previousPage.setClickable(false);
            previousPage.setAlpha(0.5f);
        }else{
            previousPage.setClickable(true);
            previousPage.setAlpha(1.0f);
        }

        if(currentPage == totalPages-1){
            nextPage.setClickable(false);
            nextPage.setAlpha(0.5f);
        }else{
            nextPage.setClickable(true);
            nextPage.setAlpha(1.0f);
        }
    }


    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }


    private void setAll(ArrayList<SimpleEventDTO> events){

        EventAdapter adapter = new EventAdapter(getContext(),events,this);
        RecyclerView.LayoutManager layoutManager= new LinearLayoutManager(getContext());
        this.allEventsRecyclerView.setLayoutManager(layoutManager);
        this.allEventsRecyclerView.setItemAnimator(new DefaultItemAnimator());
        this.allEventsRecyclerView.setAdapter(adapter);
    }

    private void setTop5(ArrayList<SimpleEventDTO> top5Events){
        this.topEventsRecyclerView.setAdapter(new TopEventAdapter(top5Events));
    }

    private void fetchEvents() {
        String city = viewModel.getSelectedCity().getValue();
        UUID eventTypeId = viewModel.getSelectedEventTypeEvents().getValue();
        String time = viewModel.getSelectedDate().getValue();
        String searchText = viewModel.getSearchTextEvents().getValue();
        String sortField = viewModel.getSortFieldEvents().getValue();
        String sortDirection = viewModel.getSortDirectionEvents().getValue();

        viewModel.fetchAllEventsPage(city, eventTypeId, time, searchText, sortField,sortDirection,  currentPage, pageSize);
    }


    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {

        //checks if sensor is accelerometer
        if (sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {

            long curTime = System.currentTimeMillis();

            if ((curTime - lastUpdate) > 200) {
                //calculates how much time has last from last update
                long diffTime = (curTime - lastUpdate);
                lastUpdate = curTime;

                //gets acceleration by axis
                float[] values = sensorEvent.values;
                float x = values[0];
                float y = values[1];
                float z = values[2];

                //calculates speed
                float speed = Math.abs(x + y + z - last_x - last_y - last_z) / diffTime * 10000;

                //checks if speed is high enough to be detected as gesture
                if (speed > SHAKE_THRESHOLD ) {

                    if(!viewModel.getSortFieldEvents().getValue().equals("")) {
                        if (curTime - lastFetchTime > MIN_TIME_BETWEEN_SHAKES) {
                            lastFetchTime = curTime;
                            if (sortDirection.equals("asc") || sortDirection.equals("")) {
                                sortDirection = "desc";
                                viewModel.setSortDirectionEvents("desc");
                            } else if (sortDirection.equals("desc")) {
                                sortDirection = "asc";
                                viewModel.setSortDirectionEvents("asc");
                            }
                            fetchEvents();
                        }
                    }
                }
                last_x = x;
                last_y = y;
                last_z = z;
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        if(sensor.getType() == Sensor.TYPE_ACCELEROMETER){
            Log.i("REZ_ACCELEROMETER", String.valueOf(accuracy));
        }
    }
}