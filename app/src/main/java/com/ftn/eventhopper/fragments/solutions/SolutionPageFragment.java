package com.ftn.eventhopper.fragments.solutions;

import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ftn.eventhopper.R;
import com.ftn.eventhopper.adapters.CommentAdapter;
import com.ftn.eventhopper.adapters.ImageSliderAdapter;
import com.ftn.eventhopper.clients.services.auth.UserService;
import com.ftn.eventhopper.filters.MinMaxInputFilter;
import com.ftn.eventhopper.fragments.solutions.viewmodel.SolutionPageViewModel;
import com.ftn.eventhopper.shared.dtos.eventTypes.SimpleEventTypeDTO;
import com.ftn.eventhopper.shared.dtos.solutions.SolutionDetailsDTO;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;


public class SolutionPageFragment extends Fragment {
    private static final String ARG_ID = "id";

    private SolutionPageViewModel viewModel;
    private NavController navController;

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
    private TextView providerName;
    private TextView finalPrice;
    private TextView discount;
    private TextView oldPrice;
    private TextView rating;
    private TextView availability;

    private RecyclerView comments;
    private ImageView favoriteButton;
    private TextView statusMessage;
    private MaterialButton reviewButton;
    private MaterialButton chatButton;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState
    ) {
        View view = inflater.inflate(R.layout.fragment_solution_page, container, false);

        viewModel = new ViewModelProvider(this).get(SolutionPageViewModel.class);
        statusMessage = view.findViewById(R.id.status_message);
        statusMessage.setText(R.string.loading_product_service_details);
        statusMessage.setVisibility(View.VISIBLE);

        if (getArguments() != null) {
            id = UUID.fromString(getArguments().getString(ARG_ID));
            viewModel.fetchSolutionDetailsById(id);

            imageSlider = view.findViewById(R.id.provider_image_slider);
            name = view.findViewById(R.id.solution_name);
            description = view.findViewById(R.id.solution_description);
            category = view.findViewById(R.id.solution_category);
            eventTypes = view.findViewById(R.id.solution_event_types);
            duration = view.findViewById(R.id.solution_duration);
            reservationWindow = view.findViewById(R.id.solution_reservation_window);
            cancellationWindow = view.findViewById(R.id.solution_cancellation_window);
            provider = view.findViewById(R.id.solution_provider_label);
            providerName = view.findViewById(R.id.solution_provider_name);
            finalPrice = view.findViewById(R.id.solution_final_price);
            discount = view.findViewById(R.id.solution_discount);
            oldPrice = view.findViewById(R.id.solution_old_price);
            rating = view.findViewById(R.id.solution_rating);
            availability = view.findViewById(R.id.solution_availability);
            comments = view.findViewById(R.id.solution_comments_recyclerview);
            favoriteButton = view.findViewById(R.id.solution_favorite);
            reviewButton = view.findViewById(R.id.solution_review_button);
            chatButton = view.findViewById(R.id.solution_open_chat);
        }

        viewModel.getSolutionDetails().observe(getViewLifecycleOwner(), solution -> {
            if (solution != null) {
                statusMessage.setVisibility(View.GONE);
                this.setFieldValues(solution);
            }
        });

        viewModel.getErrorMessage().observe(getViewLifecycleOwner(), error -> {
            if (error != null) {
                Log.e("EventHopper", "Error fetching solution details: " + error);
                statusMessage.setText(R.string.oops_something_went_wrong_please_try_again_later);
                statusMessage.setVisibility(View.VISIBLE);
            }
        });

        navController = NavHostFragment.findNavController(this);

        return view;
    }

    private void setFieldValues(SolutionDetailsDTO solution) {
        name.setText(solution.getName());
        description.setText(solution.getDescription());
        category.setText(String.format("Category: %s", solution.getCategory().getName()));

        StringBuilder eventTypesText = new StringBuilder();
        int eventTypeIndex = 0;
        for (SimpleEventTypeDTO eventType : solution.getEventTypes()) {
            eventTypesText.append(eventType.getName());
            if (eventTypeIndex++ < solution.getEventTypes().size() - 1) {
                eventTypesText.append(", ");
            }
        }
        eventTypes.setText(String.format("Event types: %s", eventTypesText.toString()));

        if (solution.isService()) {
            duration.setText(String.format("Duration: %s minutes", solution.getDurationMinutes()));
            reservationWindow.setText(String.format("Reservation window: %d day(s)", solution.getReservationWindowDays()));
            cancellationWindow.setText(String.format("Cancellation window: %d day(s)", solution.getCancellationWindowDays()));
        } else {
            duration.setVisibility(View.GONE);
            reservationWindow.setVisibility(View.GONE);
            cancellationWindow.setVisibility(View.GONE);
        }

        if (solution.isService()) {
            provider.setText(R.string.service_provider);

        } else {
            provider.setText(R.string.product_provider);
        }

        providerName.setText(solution.getProvider().getCompanyName());


        finalPrice.setText(String.format("%.2f€", solution.getPrice().getFinalPrice()));
        if (solution.getPrice().getDiscount() == 0) {
            discount.setVisibility(View.GONE);
            oldPrice.setVisibility(View.GONE);
        }
        discount.setText(String.format("%.2f%%", solution.getPrice().getDiscount()));
        oldPrice.setText(String.format("%.2f€", solution.getPrice().getBasePrice()));

        if (!solution.isAvailable()) {
            availability.setText(R.string.unavailable);
        } else {
            availability.setVisibility(View.GONE);
        }

        int filledStars = (int) Math.floor(solution.getRating());
        int emptyStars = 5 - filledStars;
        StringBuilder ratingText = new StringBuilder();
        for (int i = 0; i < filledStars; i++) {
            ratingText.append("★");
        }
        for (int i = 0; i < emptyStars; i++) {
            ratingText.append("☆");
        }
        rating.setText(ratingText.toString());

        if (solution.getPictures().isEmpty()) {
            imageSlider.setVisibility(View.GONE);
        } else {
            String[] imageUrls = solution.getPictures().toArray(String[]::new);
            ImageSliderAdapter imageSliderAdapter = new ImageSliderAdapter(List.of(imageUrls));
            imageSlider.setAdapter(imageSliderAdapter);
            imageSlider.setVisibility(View.VISIBLE);
        }

        CommentAdapter commentsAdapter = new CommentAdapter(new ArrayList<>(solution.getComments()), getContext());
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        comments.setLayoutManager(layoutManager);
        comments.setItemAnimator(new DefaultItemAnimator());
        comments.setAdapter(commentsAdapter);

        if(UserService.isTokenValid()){
            favoriteButton.setImageDrawable(getResources().getDrawable(R.drawable.baseline_star_24));
            if (solution.isFavorite()) {
                favoriteButton.setColorFilter(getResources().getColor(R.color.md_theme_secondary));
            } else {
                favoriteButton.setColorFilter(getResources().getColor(R.color.grey));
            }
            favoriteButton.setVisibility(View.VISIBLE);

            favoriteButton.setOnClickListener(v -> {
                viewModel.toggleFavorite();
            });

            if (solution.isPendingRating() || solution.isPendingComment()) {
                reviewButton.setVisibility(View.VISIBLE);
                reviewButton.setOnClickListener(v -> {
                    setupReviewDialog();
                });
            } else {
                reviewButton.setVisibility(View.GONE);
            }

            if (solution.getConversationInitialization() != null) {
                chatButton.setVisibility(View.VISIBLE);
                chatButton.setOnClickListener(v -> {
                    Bundle bundle = new Bundle();
                    bundle.putString("username", solution.getConversationInitialization().getUsername());
                    bundle.putString("name", solution.getConversationInitialization().getName());
                    bundle.putString("surname", solution.getConversationInitialization().getSurname());
                    bundle.putString("profilePicture", solution.getConversationInitialization().getProfilePictureUrl());
                    navController.navigate(R.id.action_to_chat_with_provider, bundle);
                });
            } else {
                chatButton.setVisibility(View.GONE);
            }

        }

        providerName.setOnClickListener(v -> {
            viewModel.goToProviderPage(navController);
        });
    }

    private void setupReviewDialog() {
        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_product_review, null);

        TextInputLayout ratingInput = dialogView.findViewById(R.id.rating_layout);
        TextInputLayout commentInput = dialogView.findViewById(R.id.comment_layout);
//
        Objects.requireNonNull(ratingInput.getEditText())
                .setFilters(new InputFilter[]{new MinMaxInputFilter(1, 5)});

        if (!viewModel.getSolutionDetails().getValue().isPendingRating()) {
            ratingInput.setEnabled(false);
        }
        if (!viewModel.getSolutionDetails().getValue().isPendingComment()) {
            commentInput.setEnabled(false);
        }

        MaterialAlertDialogBuilder dialogBuilder = new MaterialAlertDialogBuilder(getContext());
        dialogBuilder.setTitle("Review " + viewModel.getSolutionDetails().getValue().getName());
        dialogBuilder.setView(dialogView);
        dialogBuilder.setPositiveButton("Save", (dialogInterface, i) -> {
        });
        dialogBuilder.setNegativeButton("Cancel", (dialogInterface, i) -> {
        });
        AlertDialog editDialog = dialogBuilder.create();
        editDialog.show();
        editDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v1 -> {
            if (!ratingInput.getEditText().getText().toString().trim().isEmpty()) {
                if (Integer.valueOf(ratingInput.getEditText().getText().toString().trim()) < 1 || Integer.valueOf(ratingInput.getEditText().getText().toString().trim()) > 5) {
                    ratingInput.setError("Rating must be between 1 and 5");
                } else {
                    ratingInput.setError(null);
                }
            }

            if (commentInput.getEditText().getText().toString().trim().length() > 255) {
                commentInput.setError("Comment is too long");
            } else {
                commentInput.setError(null);
            }

            if (ratingInput.getError() == null && commentInput.getError() == null) {
                Integer rating;
                String comment;
                if (ratingInput.getEditText().getText().toString().trim().isEmpty()) {
                    rating = null;
                } else {
                    rating = Integer.valueOf(ratingInput.getEditText().getText().toString().trim());
                }
                if (commentInput.getEditText().getText().toString().trim().isEmpty()) {
                    comment = null;
                } else {
                    comment = commentInput.getEditText().getText().toString().trim();
                }


                viewModel.makeReview(
                        rating,
                        comment
                );

                editDialog.dismiss();
            }
        });
    }

}