package com.ftn.eventhopper.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.ftn.eventhopper.fragments.HomeEventsFragment;
import com.ftn.eventhopper.fragments.HomeSolutionsFragment;

public class TabsPagerAdapter extends FragmentStateAdapter {

    public TabsPagerAdapter(@NonNull Fragment fragment) {
        super(fragment);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new HomeEventsFragment(); // Prvi fragment (za prvi tab)
            case 1:
                return new HomeSolutionsFragment(); // Drugi fragment (za drugi tab)
            default:
                return new HomeEventsFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 2; // Broj tabova
    }
}

