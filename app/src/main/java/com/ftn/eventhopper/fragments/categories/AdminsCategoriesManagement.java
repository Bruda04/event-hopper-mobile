package com.ftn.eventhopper.fragments.categories;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ftn.eventhopper.R;
import com.ftn.eventhopper.adapters.AdminsCategoriesAdapter;
import com.ftn.eventhopper.adapters.AdminsSuggestionsAdapter;
import com.ftn.eventhopper.adapters.PupServicesAdapter;
import com.ftn.eventhopper.models.Category;
import com.ftn.eventhopper.models.CategorySuggestion;
import com.ftn.eventhopper.models.Service;

import java.util.ArrayList;

public class AdminsCategoriesManagement extends Fragment {


    public AdminsCategoriesManagement() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_admin_categories_management, container, false);

        // Set up the RecyclerView with an adapter
        RecyclerView recyclerView = view.findViewById(R.id.recycler_view_categories);

        ArrayList<Category> categories = new ArrayList<>();
        categories.add(new Category("Music", "Enjoy live performances, concerts, and musical shows", true));
        categories.add(new Category("Food", "Discover culinary delights, tastings, and food festivals", false));
        categories.add(new Category("Art", "Explore exhibitions, creative workshops, and galleries", false));
        categories.add(new Category("Sport", "Participate in physical activities, games, and competitions", true));
        categories.add(new Category("Education", "Attend insightful learning sessions, workshops, and seminars", true));
        categories.add(new Category("Health", "Join wellness activities, fitness classes, and health events", false));


        AdminsCategoriesAdapter adapter = new AdminsCategoriesAdapter(getContext(), categories, this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        return view;
    }
}