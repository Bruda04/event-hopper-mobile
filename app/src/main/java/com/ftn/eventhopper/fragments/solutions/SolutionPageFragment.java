package com.ftn.eventhopper.fragments.solutions;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.ftn.eventhopper.R;
import com.ftn.eventhopper.adapters.CommentAdapter;
import com.ftn.eventhopper.adapters.ImageSliderAdapter;
import com.ftn.eventhopper.clients.ClientUtils;
import com.ftn.eventhopper.fragments.solutions.viewmodel.SolutionPageViewModel;
import com.ftn.eventhopper.shared.dtos.eventTypes.SimpleEventTypeDTO;
import com.ftn.eventhopper.shared.dtos.solutions.SolutionDetailsDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


public class SolutionPageFragment extends Fragment {
    private static final String ARG_ID = "id";

    private SolutionPageViewModel viewModel;

    private UUID id;

    private ViewPager2 imageSlider;
    private TextView name;
    private TextView description;
    private TextView category;
    private TextView eventTypes;
    private TextView duration;
    private TextView reservationWindow;
    private TextView cancellationWindow;
    private TextView provider;
    private TextView finalPrice;
    private TextView discount;
    private TextView oldPrice;
    private TextView rating;
    private TextView availability;

    private RecyclerView comments;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState
    ) {
        View view = inflater.inflate(R.layout.fragment_solution_page, container, false);
        name = view.findViewById(R.id.solution_name);


        viewModel = new ViewModelProvider(this).get(SolutionPageViewModel.class);

        if (getArguments() != null) {
            UUID productId = UUID.fromString(getArguments().getString(ARG_ID));
            viewModel.fetchSolutionDetailsById(productId);

            imageSlider = view.findViewById(R.id.solution_image_slider);
            name = view.findViewById(R.id.solution_name);
            description = view.findViewById(R.id.solution_description);
            category = view.findViewById(R.id.solution_category);
            eventTypes = view.findViewById(R.id.solution_event_types);
            duration = view.findViewById(R.id.solution_duration);
            reservationWindow = view.findViewById(R.id.solution_reservation_window);
            cancellationWindow = view.findViewById(R.id.solution_cancellation_window);
            provider = view.findViewById(R.id.solution_provider_name);
            finalPrice = view.findViewById(R.id.solution_final_price);
            discount = view.findViewById(R.id.solution_discount);
            oldPrice = view.findViewById(R.id.solution_old_price);
            rating = view.findViewById(R.id.solution_rating);
            availability = view.findViewById(R.id.solution_availability);
            comments = view.findViewById(R.id.solution_comments_recyclerview);
        }

        viewModel.getSolutionDetails().observe(getViewLifecycleOwner(), product -> {
            if (product != null) {
                this.setFiledValues(product);
            }
        });

        viewModel.getErrorMessage().observe(getViewLifecycleOwner(), error -> {
            if (error != null) {
                Log.e("EventHopper", "Error fetching solution details: " + error);
                Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    private void setFiledValues(SolutionDetailsDTO product) {
        name.setText(product.getName());
        description.setText(product.getDescription());
        category.setText(String.format("Category: %s", product.getCategory().getName()));

        StringBuilder eventTypesText = new StringBuilder();
        int eventTypeIndex = 0;
        for (SimpleEventTypeDTO eventType : product.getEventTypes()) {
            eventTypesText.append(eventType.getName());
            if (eventTypeIndex++ < product.getEventTypes().size() - 1) {
                eventTypesText.append(", ");
            }
        }
        eventTypes.setText(String.format("Event types: %s", eventTypesText.toString()));

        if (product.isService()) {
            duration.setText(String.format("Duration: %s minutes",product.getDurationMinutes()));
            reservationWindow.setText(String.format("Reservation window: %d day(s)", product.getReservationWindowDays()));
            cancellationWindow.setText(String.format("Cancellation window: %d day(s)", product.getCancellationWindowDays()));
        } else {
            duration.setVisibility(View.GONE);
            reservationWindow.setVisibility(View.GONE);
            cancellationWindow.setVisibility(View.GONE);
        }
        provider.setText(product.getProvider().getName());

        finalPrice.setText(String.format("%.2f€", product.getPrice().getFinalPrice()));
        discount.setText(String.format("%.2f%%", product.getPrice().getDiscount()));
        oldPrice.setText(String.format("%.2f€", product.getPrice().getBasePrice()));

        if (!product.isAvailable()) {
            availability.setText(R.string.unavailable);
        }

        int filledStars = (int) Math.floor(product.getRating());
        int emptyStars = 5 - filledStars;
        StringBuilder ratingText = new StringBuilder();
        for (int i = 0; i < filledStars; i++) {
            ratingText.append("★");
        }
        for (int i = 0; i < emptyStars; i++) {
            ratingText.append("☆");
        }
        rating.setText(ratingText.toString());

        String[] imageUrls = product.getPictures().toArray(String[]::new);
        ImageSliderAdapter imageSliderAdapter = new ImageSliderAdapter(List.of(imageUrls));
        imageSlider.setAdapter(imageSliderAdapter);

        CommentAdapter commentsAdapter = new CommentAdapter(new ArrayList<>(product.getComments()));
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        comments.setLayoutManager(layoutManager);
        comments.setItemAnimator(new DefaultItemAnimator());
        comments.setAdapter(commentsAdapter);
    }

}