package com.ftn.eventhopper.fragments.profile;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.ftn.eventhopper.R;
import com.ftn.eventhopper.clients.ClientUtils;
import com.ftn.eventhopper.fragments.home.HomeEventsFragment;
import com.ftn.eventhopper.fragments.home.HomeSolutionsFragment;
import com.ftn.eventhopper.fragments.profile.viewmodels.ProfileViewModel;
import com.ftn.eventhopper.shared.models.users.PersonType;
import com.google.android.material.tabs.TabLayout;


public class FavoritesFragment extends Fragment {

    TabLayout tabLayout;
    ViewPager2 viewPager2;
    FavoritesTabPagerAdapter adapter;
    private ProfileViewModel viewModel;



    public FavoritesFragment() {
        // Required empty public constructor
    }

    public static FavoritesFragment newInstance(String param1, String param2) {
        FavoritesFragment fragment = new FavoritesFragment();
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
        View view = inflater.inflate(R.layout.fragment_favorites, container, false);
        viewModel = new ViewModelProvider(this).get(ProfileViewModel.class);



        tabLayout = view.findViewById(R.id.favorites_tab_layout);
        viewPager2 = view.findViewById(R.id.viewPager);

        adapter = new FavoritesTabPagerAdapter(getActivity());
        viewPager2.setAdapter(adapter);
        
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager2.setCurrentItem(tab.getPosition(), true);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                tabLayout.getTabAt(position).select();
            }
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

    private class FavoritesTabPagerAdapter extends FragmentStateAdapter {

        public FavoritesTabPagerAdapter(@NonNull FragmentActivity fragment) {
            super(fragment);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            switch (position) {
                case 0:
                    return new FavoriteEventsFragment(); // Prvi fragment (za prvi tab)
                case 1:
                    return new FavoriteSolutionsFragment(); // Drugi fragment (za drugi tab)
                default:
                    return new FavoriteEventsFragment();
            }
        }

        @Override
        public int getItemCount() {
            return 2; // Broj tabova
        }
    }
}