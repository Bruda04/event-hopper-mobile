package com.ftn.eventhopper.fragments.solutions;

import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
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
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ftn.eventhopper.R;
import com.ftn.eventhopper.adapters.CommentAdapter;
import com.ftn.eventhopper.adapters.ImageSliderAdapter;
import com.ftn.eventhopper.clients.services.auth.UserService;
import com.ftn.eventhopper.filters.MinMaxInputFilter;
import com.ftn.eventhopper.fragments.solutions.viewmodel.SolutionPageViewModel;
import com.ftn.eventhopper.shared.dtos.eventTypes.SimpleEventTypeDTO;
import com.ftn.eventhopper.shared.dtos.events.SimpleEventDTO;
import com.ftn.eventhopper.shared.dtos.solutions.SolutionDetailsDTO;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;


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
    private MaterialButton buyButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requireActivity().getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                navController.popBackStack(R.id.solution_page, true);
            }
        });
    }

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
            Log.i("solutin","upao");
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
            buyButton = view.findViewById(R.id.solution_buy_button);
        }

        viewModel.getSolutionDetails().observe(getViewLifecycleOwner(), solution -> {
            if (solution != null) {
                statusMessage.setVisibility(View.GONE);
                this.setFieldValues(solution);
            }
        });

        viewModel.getErrorMessage().observe(getViewLifecycleOwner(), error -> {
            if (error != null) {
                Log.e("Solution Details", error);
                new MaterialAlertDialogBuilder(requireContext())
                        .setTitle("Error")
                        .setMessage(error)
                        .setPositiveButton("OK", (dialog, which) -> dialog.dismiss())
                        .show();
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

        if (solution.getApplicableEvents() != null && !solution.getApplicableEvents().isEmpty() && solution.isAvailable()) {
            buyButton.setVisibility(View.VISIBLE);
            buyButton.setOnClickListener(v -> {
                setupPurchaseFlow(solution);
            });
        } else {
            buyButton.setVisibility(View.GONE);
        }
    }

    private void setupPurchaseFlow(SolutionDetailsDTO solution) {
        if (solution.getApplicableEvents().size() == 1) {
            if (solution.isService()) {
                setupBookServiceDialog(solution, solution.getApplicableEvents().iterator().next());
            } else {
                setupBuyProductDialog(solution, solution.getApplicableEvents().iterator().next());
            }
        } else {
            setupChoseEventDialog(solution);
        }
    }

    private void setupBookServiceDialog(SolutionDetailsDTO solution, SimpleEventDTO event) {
        MaterialAlertDialogBuilder dialogBuilder = new MaterialAlertDialogBuilder(requireContext());
        dialogBuilder.setTitle("Book a service");

        LinearLayout layout = new LinearLayout(requireContext());
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(50, 20, 50, 20);

        Button chooseDateButton = new Button(requireContext());
        chooseDateButton.setText("Choose Date");

        LinearLayout.LayoutParams buttonParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        buttonParams.gravity = Gravity.START;
        chooseDateButton.setLayoutParams(buttonParams);

        final Calendar selectedDate = Calendar.getInstance();

        chooseDateButton.setOnClickListener(v -> {
            int year = selectedDate.get(Calendar.YEAR);
            int month = selectedDate.get(Calendar.MONTH);
            int day = selectedDate.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(requireContext(),
                    (view, selectedYear, selectedMonth, selectedDay) -> {

                        selectedDate.set(selectedYear, selectedMonth, selectedDay);

                        String formattedDate = String.format(Locale.getDefault(), "%02d-%02d-%04d", selectedDay, selectedMonth + 1, selectedYear);
                        chooseDateButton.setText(formattedDate);
                    }, year, month, day);

            datePickerDialog.show();
        });

        layout.addView(chooseDateButton);
        dialogBuilder.setView(layout);

        dialogBuilder.setPositiveButton("Book", (dialogInterface, i) -> {
            //viewModel.bookService(event.getId(), datePicker.getda);
        });
        dialogBuilder.setNegativeButton("Cancel", (dialogInterface, i) -> {
        });
        dialogBuilder.show();
    }

    private void setupBuyProductDialog(SolutionDetailsDTO solution, SimpleEventDTO event) {
        MaterialAlertDialogBuilder dialogBuilder = new MaterialAlertDialogBuilder(requireContext());
        dialogBuilder.setTitle("Buy Product");
        dialogBuilder.setMessage(String.format("Are you sure you want to buy %s from %s for your event %s?",
                solution.getName(), solution.getProvider().getCompanyName(), event.getName()));
        dialogBuilder.setPositiveButton("Yes", (dialogInterface, i) -> {
            viewModel.buyProduct(event.getId());
        });
        dialogBuilder.setNegativeButton("No", (dialogInterface, i) -> {
        });
        dialogBuilder.show();
    }

    private void setupChoseEventDialog(SolutionDetailsDTO solution) {
        View dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_buy_product_select_event, null);

        TextInputLayout select = dialogView.findViewById(R.id.event_select);
        AutoCompleteTextView selectEvent = dialogView.findViewById(R.id.event_select_autocomplete);

        ArrayList<String> eventNames = new ArrayList<>();
        for (SimpleEventDTO event : solution.getApplicableEvents()) {
            eventNames.add(event.getName());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_dropdown_item_1line, eventNames);
        selectEvent.setAdapter(adapter);

        MaterialAlertDialogBuilder editDialogBuilder = new MaterialAlertDialogBuilder(requireContext());
        editDialogBuilder.setView(dialogView);
        editDialogBuilder.setTitle("Select Event");
        editDialogBuilder.setPositiveButton("Chose", (dialog, which) -> {
        });
        editDialogBuilder.setNegativeButton("Cancel", (dialog, which) -> {
        });

        AlertDialog choseEventDialog = editDialogBuilder.create();
        choseEventDialog.show();
        choseEventDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v -> {
            String selectedEvent = selectEvent.getText().toString();
            if (selectedEvent.isEmpty()) {
                select.setError("Please select an event");
            } else {
                select.setError(null);
                int index = eventNames.indexOf(selectedEvent);
                if (index == -1) {
                    select.setError("Invalid event");
                } else {
                    select.setError(null);
                }
                ArrayList<SimpleEventDTO> applicableEvents = new ArrayList<>(solution.getApplicableEvents());
                choseEventDialog.dismiss();
                SimpleEventDTO event = applicableEvents.get(index);
                if (event == null){
                    return;
                }
                if (solution.isService()) {
                    setupBookServiceDialog(solution, event);
                } else {
                    setupBuyProductDialog(solution, event);
                }
            }
        });
    }

    private void setupReviewDialog() {
        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_product_review, null);

        TextInputLayout ratingInput = dialogView.findViewById(R.id.rating_layout);
        TextInputLayout commentInput = dialogView.findViewById(R.id.comment_layout);

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

            if (commentInput.getEditText().getText().toString().trim().length() > 1000) {
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