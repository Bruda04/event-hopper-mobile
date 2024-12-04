package com.ftn.eventhopper.fragments.categories;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ftn.eventhopper.R;
import com.google.android.material.tabs.TabLayout;

public class AdminsCategoriesSuggestionsManagement extends Fragment {

    TabLayout tabLayout;
    ViewPager2 viewPager2;
    CategoriesSuggestionsPagerAdapter pagerAdapter;

    public AdminsCategoriesSuggestionsManagement() {
        // Required empty public constructor
    }

    public static AdminsCategoriesSuggestionsManagement newInstance() {
        AdminsCategoriesSuggestionsManagement fragment = new AdminsCategoriesSuggestionsManagement();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_admins_categories_suggestions_management, container, false);

        tabLayout = view.findViewById(R.id.tab_layout);
        viewPager2 = view.findViewById(R.id.view_pager);
        pagerAdapter = new CategoriesSuggestionsPagerAdapter(getActivity());
        viewPager2.setAdapter(pagerAdapter);

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

    private class CategoriesSuggestionsPagerAdapter extends FragmentStateAdapter {
        public CategoriesSuggestionsPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
            super(fragmentActivity);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            switch (position) {
                case 0:
                    return new AdminsCategoriesManagement();
                case 1:
                    return new AdminsSuggestionsManagement();
                default:
                    return new AdminsCategoriesManagement();
            }

        }

        @Override
        public int getItemCount() {
            return 2;
        }
    }
}