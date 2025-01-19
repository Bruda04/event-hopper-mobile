package com.ftn.eventhopper.fragments.profile;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ftn.eventhopper.R;
import com.ftn.eventhopper.adapters.solutions.SolutionAdapter;
import com.ftn.eventhopper.fragments.profile.viewmodels.ProfileViewModel;
import com.ftn.eventhopper.shared.dtos.solutions.SimpleProductDTO;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FavoriteSolutionsFragment#} factory method to
 * create an instance of this fragment.
 */
public class FavoriteSolutionsFragment extends Fragment {

    private ProfileViewModel viewModel;
    private RecyclerView allSolutionsRecyclerView;

    public FavoriteSolutionsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorite_solutions, container, false);
        viewModel = new ViewModelProvider(requireActivity()).get(ProfileViewModel.class);

        this.allSolutionsRecyclerView = view.findViewById(R.id.recyclerView_allsolutions);
        if (allSolutionsRecyclerView.getParent() instanceof ViewGroup) {
            ((ViewGroup) allSolutionsRecyclerView.getParent()).setLayoutTransition(null);
        }
        allSolutionsRecyclerView.setLayoutTransition(null);


        viewModel.getProfileData().observe(getViewLifecycleOwner(), profile -> {
            if (profile != null) {
                allSolutionsRecyclerView.setVisibility(View.VISIBLE);
                this.setAll(new ArrayList<>(profile.getFavoriteProducts()));
            }
        });
        viewModel.fetchProfile();

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

    private void setAll(ArrayList<SimpleProductDTO> solutions){
        SolutionAdapter adapter = new SolutionAdapter(getContext(),solutions,this);
        RecyclerView.LayoutManager layoutManager= new LinearLayoutManager(getContext());
        this.allSolutionsRecyclerView.setLayoutManager(layoutManager);
        this.allSolutionsRecyclerView.setItemAnimator(new DefaultItemAnimator());
        this.allSolutionsRecyclerView.setAdapter(adapter);
    }
}