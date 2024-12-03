package com.ftn.eventhopper.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.widget.ViewPager2;

import com.ftn.eventhopper.R;
import com.ftn.eventhopper.adapters.EventAdapter;
import com.ftn.eventhopper.adapters.ServiceAdapter;
import com.ftn.eventhopper.adapters.TabsPagerAdapter;
import com.ftn.eventhopper.models.Event;
import com.ftn.eventhopper.models.Service;
import com.github.islamkhsh.CardSliderViewPager;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        EdgeToEdge.enable(requireActivity());

        TabLayout tabLayout = view.findViewById(R.id.tabLayout);
        ViewPager2 viewPager = view.findViewById(R.id.viewPager);

        // Postavljanje adaptera za ViewPager2
        TabsPagerAdapter adapter = new TabsPagerAdapter(this);
        viewPager.setAdapter(adapter);

        // Povezivanje TabLayout sa ViewPager2 pomoću TabLayoutMediator
        new TabLayoutMediator(tabLayout, viewPager, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                switch (position) {
                    case 0:
                        tab.setText(getString(R.string.explore_events));
                        tab.setIcon(R.drawable.event); // Postavljanje ikone ako je potrebno
                        break;
                    case 1:
                        tab.setText(getString(R.string.explore_solutions));
                        tab.setIcon(R.drawable.shopping_cart);
                        break;
                }
            }
        }).attach();


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
