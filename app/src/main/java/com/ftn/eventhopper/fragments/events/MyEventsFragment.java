package com.ftn.eventhopper.fragments.events;

import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ftn.eventhopper.R;
import com.ftn.eventhopper.adapters.events.EventAdapter;
import com.ftn.eventhopper.fragments.events.viewmodels.MyEventsViewModel;
import com.ftn.eventhopper.fragments.profile.viewmodels.ProfileViewModel;
import com.ftn.eventhopper.shared.dtos.events.SimpleEventDTO;
import com.ftn.eventhopper.shared.dtos.profile.ProfileForPersonDTO;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MyEventsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyEventsFragment extends Fragment {
    private MyEventsViewModel viewModel;
    private RecyclerView allEventsRecyclerView;
    private TextView emptyMessage;
    private SwipeRefreshLayout swipeRefreshLayout;
    private NavController navController;


    public MyEventsFragment() {
        // Required empty public constructor
    }

    public static MyEventsFragment newInstance() {
        MyEventsFragment fragment = new MyEventsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requireActivity().getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                navController.popBackStack(R.id.view_my_events, true);
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_events, container, false);
        viewModel = new ViewModelProvider(requireActivity()).get(MyEventsViewModel.class);
        navController = NavHostFragment.findNavController(this);

        this.emptyMessage = view.findViewById(R.id.emptyMessage);

        disableLayoutTransitions(view);

        this.allEventsRecyclerView = view.findViewById(R.id.recyclerView_allevents);
        if (allEventsRecyclerView.getParent() instanceof ViewGroup) {
            ((ViewGroup) allEventsRecyclerView.getParent()).setLayoutTransition(null);
        }
        allEventsRecyclerView.setLayoutTransition(null);

        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnChildScrollUpCallback((parent, child) -> {
            // Prevent refresh if RecyclerView is not at the top
            return allEventsRecyclerView.canScrollVertically(-1);
        });
        swipeRefreshLayout.setOnRefreshListener(() -> {
            fetchMyEvents(true);
        });

        this.fetchMyEvents(true);

        return view;
    }

    private void fetchMyEvents(boolean refresh){
        if(refresh || viewModel.getEventLiveData().getValue() == null){
            viewModel.getMyEvents();
        }
        viewModel.getEventLiveData().observe(getViewLifecycleOwner(), myEvents -> {
            if (myEvents != null && !myEvents.isEmpty()) {
                allEventsRecyclerView.setVisibility(View.VISIBLE);
                emptyMessage.setVisibility(View.GONE);
                this.setAll(new ArrayList<>(myEvents));
            } else {
                allEventsRecyclerView.setVisibility(View.GONE);
                emptyMessage.setVisibility(View.VISIBLE);
            }
            swipeRefreshLayout.setRefreshing(false); // Stop the refresh animation
        });
    }


    private void disableLayoutTransitions(View view) {
        if (view instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) view;
            viewGroup.setLayoutTransition(null);
            for (int i = 0; i < viewGroup.getChildCount(); i++) {
                disableLayoutTransitions(viewGroup.getChildAt(i));
            }
        }
    }

    private void setAll(ArrayList<SimpleEventDTO> events){
        EventAdapter adapter = new EventAdapter(getContext(), events, this);
        RecyclerView.LayoutManager layoutManager= new LinearLayoutManager(getContext());
        this.allEventsRecyclerView.setLayoutManager(layoutManager);
        this.allEventsRecyclerView.setItemAnimator(new DefaultItemAnimator());
        this.allEventsRecyclerView.setAdapter(adapter);
    }
}