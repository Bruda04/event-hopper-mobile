package com.ftn.eventhopper.fragments.events;

import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.ftn.eventhopper.R;
import com.ftn.eventhopper.clients.ClientUtils;
import com.ftn.eventhopper.fragments.events.viewmodels.EventPageViewModel;
import com.ftn.eventhopper.shared.dtos.events.SinglePageEventDTO;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;


public class EventPageFragment extends Fragment {

    private TextInputEditText emailField;
    private LinearLayout emailsList;
    private EventPageViewModel viewModel;
    private NavController navController;
    private ImageView eventImage;
    private TextView eventTitle;
    private TextView eventDescription;
    private TextView eventLocation;
    private TextView eventTime;
    private ImageView favoriteIcon;
    private Button exportPdfButton;
    private Button inviteBtn;


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
        favoriteIcon = view.findViewById(R.id.favorite_icon);
        exportPdfButton = view.findViewById(R.id.export_pdf_button);

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
                eventLocation.setText(event.getLocation().getAddress());
                eventTime.setText(event.getTime().toString());

                favoriteIcon.setImageResource(event.isFavorite() ? R.drawable.star : R.drawable.star);
                favoriteIcon.setOnClickListener(v -> viewModel.toggleFavorite());
            }
        });
        return view;
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
            if (invitePoeple(dialogView)) {
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

    private boolean invitePoeple(View dialogView){
        boolean isValid = true;

        //dodati logiku za poziv ljudi

        return isValid;
    }


}