package com.ftn.eventhopper.fragments.home;

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
import android.widget.TextView;

import com.ftn.eventhopper.R;
import com.ftn.eventhopper.adapters.solutions.SolutionAdapter;
import com.ftn.eventhopper.adapters.solutions.TopSolutionAdapter;
import com.ftn.eventhopper.clients.services.auth.UserService;
import com.ftn.eventhopper.fragments.home.viewmodels.HomeViewModel;
import com.ftn.eventhopper.shared.dtos.solutions.SimpleProductDTO;
import com.github.islamkhsh.CardSliderViewPager;
import com.google.android.material.search.SearchBar;
import com.google.android.material.search.SearchView;

import java.util.ArrayList;
import java.util.UUID;

public class HomeSolutionsFragment extends Fragment {

    private HomeViewModel viewModel;
    private CardSliderViewPager topSolutionsRecyclerView;
    private RecyclerView allSolutionsRecycleView;
    private Button filterButton;
    private Button searchButton;
    private SearchView searchView;
    private SearchBar searchBar;
    private String searchText = "";
    private String sortDirection = "asc";
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

    public HomeSolutionsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home_solutions, container, false);
        viewModel = new ViewModelProvider(requireActivity()).get(HomeViewModel.class);

        this.searchView = view.findViewById(R.id.search_view_solutions);
        this.searchBar = view.findViewById(R.id.search_bar_solutions);
        this.searchButton = view.findViewById(R.id.search_button_solutions);

        this.topSolutionsRecyclerView = view.findViewById(R.id.viewPagerSolutions);
        this.allSolutionsRecycleView = view.findViewById(R.id.recyclerView_allsolutions);

        this.nextPage = view.findViewById(R.id.forward_arrow_button_solutions);
        this.previousPage = view.findViewById(R.id.back_arrow_button_solutions);
        this.pager = view.findViewById(R.id.pager_solutions);

        //viewModel.fetchAllSolutions();
        UUID usersId = UUID.fromString(UserService.getJwtClaim(
                UserService.getJwtToken(),"id"
        ));
        viewModel.fetchTopSolutions(usersId);

        viewModel.getTop5Products().observe(getViewLifecycleOwner(), topProducts ->
        {
            if (topProducts != null){
                topSolutionsRecyclerView.setVisibility(View.VISIBLE);
                this.setTop(topProducts);
            }
        });

        this.fetchProducts();

        viewModel.getProductsPage().observe(getViewLifecycleOwner(), pagedResponse -> {
            if (pagedResponse != null && pagedResponse.getContent() != null) {
                allSolutionsRecycleView.setVisibility(View.VISIBLE);
                this.setAll(new ArrayList<>(pagedResponse.getContent()));
                totalCount = (int) viewModel.getProductsPage().getValue().getTotalElements();
                totalPages = (int) viewModel.getProductsPage().getValue().getTotalPages();
                setPager();
            }
        });

        viewModel.fetchCategories();
        //viewModel.fetchEventTypes();


        filterButton = view.findViewById(R.id.filterButtonSolution);
        filterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                SolutionsFilterSort bottomSheet = new SolutionsFilterSort();
                bottomSheet.show(fragmentManager, "FilterSortBottomSheet");
            }
        });

        searchButton.setOnClickListener(v -> {
            searchText = searchView.getText().toString();
            searchBar.setText(searchText);
            searchView.hide();
            viewModel.setSearchTextProducts(searchText);
            this.fetchProducts();

        });

        previousPage.setOnClickListener(v ->
        {
            currentPage--;
            this.fetchProducts();
            setPager();
        });

        nextPage.setOnClickListener(v ->{
            currentPage++;
            this.fetchProducts();
            setPager();
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

    private void setAll(ArrayList<SimpleProductDTO> products){

        SolutionAdapter adapter = new SolutionAdapter(getContext(),products,this);
        RecyclerView.LayoutManager layoutManager= new LinearLayoutManager(getContext());
        this.allSolutionsRecycleView.setLayoutManager(layoutManager);
        this.allSolutionsRecycleView.setItemAnimator(new DefaultItemAnimator());
        this.allSolutionsRecycleView.setAdapter(adapter);
    }

    private void setTop(ArrayList<SimpleProductDTO> top5Products){
        this.topSolutionsRecyclerView.setAdapter(new TopSolutionAdapter(top5Products));
    }

    private void fetchProducts() {
        UUID category = viewModel.getSelectedCategory().getValue();
        ArrayList<UUID> eventTypeIds = viewModel.getSelectedEventTypesProducts().getValue();
        Boolean isProduct = viewModel.getIsProduct().getValue();
        Boolean isService = viewModel.getIsService().getValue();
        Boolean availability = viewModel.getAvailability().getValue();
        Double minPrice = viewModel.getMinPrice().getValue();
        Double maxPrice = viewModel.getMaxPrice().getValue();
        String searchText = viewModel.getSearchTextProducts().getValue();
        String sortField = viewModel.getSortFieldProducts().getValue();

        viewModel.fetchAllSolutionsPage(isProduct,isService,category, eventTypeIds, minPrice,maxPrice,availability, searchText, sortField,sortDirection,  currentPage, pageSize);
    }
}