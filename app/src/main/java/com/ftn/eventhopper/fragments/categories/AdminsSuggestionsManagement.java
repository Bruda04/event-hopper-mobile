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
import com.ftn.eventhopper.models.Category;
import com.ftn.eventhopper.models.CategorySuggestion;

import java.util.ArrayList;

public class AdminsSuggestionsManagement extends Fragment {

    public AdminsSuggestionsManagement() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_admin_suggestions_management, container, false);

        // Set up the RecyclerView with an adapter
        RecyclerView recyclerView = view.findViewById(R.id.recycler_view_suggestions);

        ArrayList<CategorySuggestion> suggestions = new ArrayList<>();
        suggestions.add(new CategorySuggestion("Music", "Event"));
        suggestions.add(new CategorySuggestion("Food", "Event"));
        suggestions.add(new CategorySuggestion("Art", "Event"));
        suggestions.add(new CategorySuggestion("Sport", "Event"));
        suggestions.add(new CategorySuggestion("Education", "Event"));
        suggestions.add(new CategorySuggestion("Health", "Event"));


        AdminsSuggestionsAdapter adapter = new AdminsSuggestionsAdapter(getContext(), suggestions, this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        return view;
    }
}