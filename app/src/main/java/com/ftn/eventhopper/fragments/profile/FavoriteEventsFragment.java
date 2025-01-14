package com.ftn.eventhopper.fragments.profile;

import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ftn.eventhopper.R;
import com.ftn.eventhopper.adapters.ServiceAdapter;
import com.ftn.eventhopper.shared.models.Service;
import com.github.islamkhsh.CardSliderViewPager;

import java.util.ArrayList;


public class FavoriteEventsFragment extends Fragment {

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

    public FavoriteEventsFragment() {
        // Required empty public constructor
    }
    public static FavoriteEventsFragment newInstance(String param1, String param2) {
        FavoriteEventsFragment fragment = new FavoriteEventsFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_favorite_events, container, false);
        CardSliderViewPager cardSliderViewPager = view.findViewById(R.id.viewPagerSolutions);
        cardSliderViewPager.setAdapter(new ServiceAdapter(loadTop5Services()));

        return view;
    }
}