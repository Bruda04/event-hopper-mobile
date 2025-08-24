package com.ftn.eventhopper.fragments.events;

import static java.security.AccessController.getContext;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.Manifest;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.ftn.eventhopper.BuildConfig;
import com.ftn.eventhopper.R;
import com.ftn.eventhopper.clients.ClientUtils;
import com.ftn.eventhopper.clients.services.auth.UserService;
import com.ftn.eventhopper.fragments.events.viewmodels.EventPageViewModel;
import com.ftn.eventhopper.shared.dtos.events.SinglePageEventDTO;
import com.ftn.eventhopper.shared.dtos.location.LocationDTO;
import com.ftn.eventhopper.shared.models.events.EventPrivacyType;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class EventPageFragment extends Fragment{

    private TextInputEditText emailField;
    private LinearLayout emailsList;
    private EventPageViewModel viewModel;
    private NavController navController;
    private ImageView eventImage, favoriteIcon;
    private TextView eventTitle, eventDescription, eventLocation, eventTime, eventPrivacy;
    private Button inviteBtn;
    private FloatingActionButton exportPdfButton;
    private MaterialButton chatButton, generateGuestListBtn, viewStatsBtn, editBudgetBtn;

    private MapView mapView;
    private boolean favorited;


    public EventPageFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requireActivity().getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                navController.popBackStack(R.id.event_page, true);
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_event_page, container, false);
        viewModel = new ViewModelProvider(this).get(EventPageViewModel.class);
        navController = NavHostFragment.findNavController(this);

        Configuration.getInstance().setUserAgentValue(BuildConfig.APPLICATION_ID);

        Bundle arguments = getArguments();
        if (arguments != null) {
            String eventId = arguments.getString("event_id");
            if (eventId != null) {
                viewModel.loadEventById(eventId);
            }
        }
        eventImage = view.findViewById(R.id.event_image);
        eventTitle = view.findViewById(R.id.event_title);
        eventDescription = view.findViewById(R.id.event_description);
        eventLocation = view.findViewById(R.id.event_location);
        eventTime = view.findViewById(R.id.event_time);
        eventPrivacy = view.findViewById(R.id.event_privacy);
        favoriteIcon = view.findViewById(R.id.favorite_icon);
        exportPdfButton = view.findViewById(R.id.export_pdf_button);
        chatButton = view.findViewById(R.id.chat_with_us);
        viewStatsBtn = view.findViewById(R.id.view_stats_button);
        editBudgetBtn = view.findViewById(R.id.edit_budget_btn);
        mapView = view.findViewById(R.id.event_map);

        exportPdfButton.setOnClickListener(v -> viewModel.exportToPDF(getContext()));

        generateGuestListBtn = view.findViewById(R.id.generate_guest_list);
        inviteBtn = view.findViewById(R.id.invite_people_button);
//        emailField = view.findViewById(R.id.invite_email_field);
//        emailsList = view.findViewById(R.id.emails_list);

        inviteBtn.setOnClickListener(v -> showInviteDialog());


        viewModel.getEvent().observe(getViewLifecycleOwner(), new Observer<SinglePageEventDTO>() {
            @Override
            public void onChanged(SinglePageEventDTO event) {
                // Populate data dynamically
                Glide.with(requireContext())
                        .load(String.format("%s/%s", ClientUtils.SERVICE_API_IMAGE_PATH, event.getPicture()))
                        .placeholder(R.drawable.baseline_image_placeholder_24)
                        .error(R.drawable.baseline_image_not_supported_24)
                        .into(eventImage);

                eventTitle.setText(event.getName());
                eventDescription.setText(event.getDescription());
                eventLocation.setText(String.format("%s, %s", event.getLocation().getAddress(), event.getLocation().getCity()));
                eventTime.setText(event.getTime().toString());
                if(event.getPrivacy().equals(EventPrivacyType.PRIVATE)){
                    eventPrivacy.setText("Private");
                }else{
                    eventPrivacy.setText("Public");
                }

                LocalDateTime eventTimeValue = event.getTime();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
                eventTime.setText(eventTimeValue.format(formatter));

                mapView.setTileSource(TileSourceFactory.MAPNIK);
                mapView.setMultiTouchControls(true);

                viewModel.getLocationById(event.getLocation().getId()).observe(getViewLifecycleOwner(), location -> {
                    if (location != null) {
                        GeoPoint point = new GeoPoint(location.getLatitude(), location.getLongitude());
                        mapView.getController().setZoom(15.0);
                        mapView.getController().setCenter(point);

                        Marker marker = new Marker(mapView);
                        marker.setPosition(point);
                        marker.setTitle(event.getName());
                        mapView.getOverlays().clear();
                        mapView.getOverlays().add(marker);
                        mapView.invalidate();
                    }
                });




                if(event.isEventOrganizerLoggedIn()){
                    inviteBtn.setVisibility(View.VISIBLE);
                    if(event.getPrivacy().equals(EventPrivacyType.PRIVATE)){
                        generateGuestListBtn.setVisibility(View.VISIBLE);
                        generateGuestListBtn.setOnClickListener(v -> {
                            viewModel.generateGuestList(getContext());
                        });
                    }
                }

                if(event.isGraphAuthorized()){
                    viewStatsBtn.setVisibility(View.VISIBLE);
                    viewStatsBtn.setOnClickListener(v->{
                        Bundle bundle = new Bundle();
                        bundle.putString("eventId", String.valueOf(event.getId()));
                        navController.navigate(R.id.action_to_view_stats, bundle);

                    });
                }

                if(event.isEventOrganizerLoggedIn()){
                    editBudgetBtn.setVisibility(View.VISIBLE);
                    editBudgetBtn.setOnClickListener(v->{
                        Bundle bundle = new Bundle();
                        bundle.putString("eventId", String.valueOf(event.getId()));
                        navController.navigate(R.id.action_to_budgeting, bundle);

                    });
                }

                if (event.getConversationInitialization() != null) {
                    chatButton.setVisibility(View.VISIBLE);
                    chatButton.setOnClickListener(v -> {
                        Bundle bundle = new Bundle();
                        bundle.putString("username", event.getConversationInitialization().getUsername());
                        bundle.putString("name", event.getConversationInitialization().getName());
                        bundle.putString("surname", event.getConversationInitialization().getSurname());
                        bundle.putString("profilePicture", event.getConversationInitialization().getProfilePictureUrl());
                        navController.navigate(R.id.action_to_chat_with_provider, bundle);
                    });
                } else {
                    chatButton.setVisibility(View.GONE);
                }

                favorited = event.isFavorite();
                favoriteIcon.setImageResource(event.isFavorite() ? R.drawable.baseline_star_24 : R.drawable.star);
                favoriteIcon.setOnClickListener(v -> toggleFavorite());

                //no one is logged in
                if(!UserService.isTokenValid()){
                    favoriteIcon.setVisibility(View.GONE);
                }
            }
        });

        Configuration.getInstance().setUserAgentValue(BuildConfig.APPLICATION_ID);



        return view;
    }


    private void toggleFavorite(){
        this.viewModel.toggleFavorite();
        if(this.viewModel.isFavorited()){
            Toast.makeText(requireContext(), "Added to favorites!", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(requireContext(), "Removed from favorites.", Toast.LENGTH_SHORT).show();
        }

        favoriteIcon.setImageResource(this.viewModel.isFavorited() ? R.drawable.baseline_star_24 : R.drawable.star);
    }


    private void showInviteDialog() {
        View dialogView = getLayoutInflater().inflate(R.layout.invite_dialog, null);
        MaterialAlertDialogBuilder dialog = new MaterialAlertDialogBuilder(getContext());

        TextInputEditText emailField = dialogView.findViewById(R.id.invite_email_field);
        LinearLayout emailsList = dialogView.findViewById(R.id.emails_list);
        Button addButton = dialogView.findViewById(R.id.invite_button);

        addButton.setOnClickListener(v -> {
            String email = emailField.getText().toString().trim();
            if (TextUtils.isEmpty(email)) {     //dodatne provere za email
                Toast.makeText(getContext(), "Please enter a valid email", Toast.LENGTH_SHORT).show();
            } else {
                addEmailToDialogList(email, emailsList);
                emailField.setText("");
            }
        });

        dialog.setTitle("Invite people");
        dialog.setView(dialogView);
        dialog.setPositiveButton("Invite", (dialogInterface, i) -> {
        });
        dialog.setNegativeButton("Cancel", (dialogInterface, i) -> {
            dialogInterface.dismiss();
        });
        AlertDialog alertDialog = dialog.create();
        alertDialog.show();
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v1 -> {
            if (invitePeople(dialogView)) {
                alertDialog.dismiss();
            }
        });
    }

    private void addEmailToDialogList(String email, LinearLayout emailsList) {
        View emailView = LayoutInflater.from(getContext()).inflate(R.layout.invited_email_item, emailsList, false);

        TextView emailLabel = emailView.findViewById(R.id.email_label);
        Button deleteButton = emailView.findViewById(R.id.delete_button);

        emailLabel.setText(email);
        deleteButton.setOnClickListener(view -> emailsList.removeView(emailView));

        emailsList.addView(emailView);
    }

    private boolean invitePeople(View dialogView){
        boolean isValid = true;

        //dodati logiku za poziv ljudi

        return isValid;
    }


}