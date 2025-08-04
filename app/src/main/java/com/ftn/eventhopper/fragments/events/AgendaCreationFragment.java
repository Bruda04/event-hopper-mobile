package com.ftn.eventhopper.fragments.events;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ftn.eventhopper.R;
import com.ftn.eventhopper.fragments.events.viewmodels.EventCreationViewModel;
import com.ftn.eventhopper.shared.dtos.events.CreateAgendaActivityDTO;
import com.google.android.material.textfield.TextInputLayout;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Locale;
import java.util.Objects;

public class AgendaCreationFragment extends Fragment {
    private NavController navController;
    private EventCreationViewModel viewModel;
    private RecyclerView agendaRecyclerView;
    private AgendaAdapter adapter;
    private Button addActivityButton;

    private Button createButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requireActivity().getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                navController.popBackStack(R.id.create_agenda, false);
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_agenda_creation, container, false);
        navController = NavHostFragment.findNavController(this);
        viewModel = new ViewModelProvider(requireActivity()).get(EventCreationViewModel.class);

        agendaRecyclerView = view.findViewById(R.id.agenda_recycler_view);
        addActivityButton = view.findViewById(R.id.add_activity_button);
        createButton = view.findViewById(R.id.next_button);


        adapter = new AgendaAdapter();
        agendaRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        agendaRecyclerView.setAdapter(adapter);

        viewModel.getAgendas().observe(getViewLifecycleOwner(), activities -> {
            Collections.sort(activities, Comparator.comparing(CreateAgendaActivityDTO::getStartTime));
            adapter.setActivities(activities);
        });

        createButton.setOnClickListener(v -> {
            try{
                if(Objects.requireNonNull(viewModel.getAgendas().getValue()).isEmpty()){
                    Toast.makeText(getContext(), "You must add activities first", Toast.LENGTH_SHORT).show();
                    return;
                }

            }catch (Exception e){
                Toast.makeText(getContext(), "You must add activities first", Toast.LENGTH_SHORT).show();
                return;
            }
            viewModel.createEvent();

        });

        viewModel.getCreated().observe(getViewLifecycleOwner(), created -> {
            if (created) {
                viewModel.setCreated(false);
                Toast.makeText(getContext(), "Event Created!", Toast.LENGTH_SHORT).show();
                navController.navigate(R.id.back_to_my_events_page);
            }
        });

        addActivityButton.setOnClickListener(v -> showAddDialog());

        return view;
    }

    private void showAddDialog() {
        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_add_agenda_activity, null);

        TextInputLayout nameLayout = dialogView.findViewById(R.id.activity_name);
        TextInputLayout descLayout = dialogView.findViewById(R.id.activity_description);
        TextInputLayout locationLayout = dialogView.findViewById(R.id.activity_location);
        EditText name = (EditText) nameLayout.getEditText();
        EditText desc = (EditText) descLayout.getEditText();
        EditText location = (EditText) locationLayout.getEditText();
        TextView startTime = dialogView.findViewById(R.id.start_time);
        TextView endTime = dialogView.findViewById(R.id.end_time);
        TextView timeError = dialogView.findViewById(R.id.time_error); // Add this TextView to XML if not already

        final int[] startHour = { -1 };
        final int[] startMinute = { -1 };
        final int[] endHour = { -1 };
        final int[] endMinute = { -1 };

        startTime.setOnClickListener(v -> new TimePickerDialog(getContext(), (view, hourOfDay, minute) -> {
            startHour[0] = hourOfDay;
            startMinute[0] = minute;
            startTime.setText(String.format(Locale.getDefault(), "%02d:%02d", hourOfDay, minute));
        }, 12, 0, true).show());

        endTime.setOnClickListener(v -> new TimePickerDialog(getContext(), (view, hourOfDay, minute) -> {
            endHour[0] = hourOfDay;
            endMinute[0] = minute;
            endTime.setText(String.format(Locale.getDefault(), "%02d:%02d", hourOfDay, minute));
        }, 12, 0, true).show());

        AlertDialog dialog = new AlertDialog.Builder(requireContext())
                .setTitle("Add Activity")
                .setView(dialogView)
                .setPositiveButton("Create", null)
                .setNegativeButton("Cancel", null)
                .create();

        dialog.setOnShowListener(dialogInterface -> {
            Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            positiveButton.setOnClickListener(v -> {
                boolean hasError = false;

                String n = name.getText().toString().trim();
                String d = desc.getText().toString().trim();
                String l = location.getText().toString().trim();
                String s = startTime.getText().toString().trim();
                String e = endTime.getText().toString().trim();

                // Clear previous errors
                nameLayout.setError(null);
                descLayout.setError(null);
                locationLayout.setError(null);
                startTime.setError(null);
                endTime.setError(null);
                timeError.setVisibility(View.GONE);

                if (TextUtils.isEmpty(n)) {
                    nameLayout.setError("Name is required");
                    hasError = true;
                }
                if (TextUtils.isEmpty(d)) {
                    descLayout.setError("Description is required");
                    hasError = true;
                }
                if (TextUtils.isEmpty(l)) {
                    locationLayout.setError("Location is required");
                    hasError = true;
                }
                if (startHour[0] == -1) {
                    startTime.setError("Start time is required");
                    hasError = true;
                }
                if (endHour[0] == -1) {
                    endTime.setError("End time is required");
                    hasError = true;
                }

                if (!TextUtils.isEmpty(s) && !TextUtils.isEmpty(e) && !hasError) {
                    int startInMinutes = startHour[0] * 60 + startMinute[0];
                    int endInMinutes = endHour[0] * 60 + endMinute[0];
                    if (endInMinutes <= startInMinutes) {
                        timeError.setText("End time must be after start time");
                        timeError.setVisibility(View.VISIBLE);
                        hasError = true;
                    }
                }

                if (!hasError) {
                    LocalDateTime eventDate = viewModel.getEvent().getTime();
                    LocalDateTime startDateTime = LocalDateTime.of(
                            eventDate.getYear(),
                            eventDate.getMonth(),
                            eventDate.getDayOfMonth(),
                            startHour[0],
                            startMinute[0]
                    );
                    LocalDateTime endDateTime = LocalDateTime.of(
                            eventDate.getYear(),
                            eventDate.getMonth(),
                            eventDate.getDayOfMonth(),
                            endHour[0],
                            endMinute[0]
                    );

                    ArrayList<CreateAgendaActivityDTO> list = viewModel.getAgendas().getValue();
                    if (list != null) {
                        for (CreateAgendaActivityDTO existing : list) {
                            if (startDateTime.isBefore(existing.getEndTime()) && endDateTime.isAfter(existing.getStartTime())) {
                                timeError.setText("This time slot overlaps with another activity");
                                timeError.setVisibility(View.VISIBLE);
                                return;
                            }
                        }
                    }

                    CreateAgendaActivityDTO activity = new CreateAgendaActivityDTO();
                    activity.setName(n);
                    activity.setDescription(d);
                    activity.setLocationName(l);
                    activity.setStartTime(startDateTime);
                    activity.setEndTime(endDateTime);

                    if (list == null) list = new ArrayList<>();
                    list.add(activity);
                    viewModel.getAgendas().setValue(list);
                    dialog.dismiss();
                }
            });
        });

        dialog.show();
    }

    private class AgendaAdapter extends RecyclerView.Adapter<AgendaAdapter.ViewHolder> {

        private ArrayList<CreateAgendaActivityDTO> activities = new ArrayList<>();

        void setActivities(ArrayList<CreateAgendaActivityDTO> activities) {
            this.activities = activities;
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_agenda_card, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            CreateAgendaActivityDTO item = activities.get(position);
            holder.name.setText(item.getName());
            holder.description.setText(item.getDescription());
            holder.location.setText(item.getLocationName());
            int startMinutesI = item.getStartTime().getMinute();
            int endMinutesI = item.getStartTime().getMinute();
            String startMinutes, endMinutes;
            if(startMinutesI < 10){
                startMinutes = "0" + startMinutesI;
            }else{
                startMinutes = String.valueOf(startMinutesI);
            }

            if(endMinutesI < 10){
                endMinutes = "0" + endMinutesI;
            }else{
                endMinutes = String.valueOf(endMinutesI);
            }

            holder.time.setText(item.getStartTime().getHour() + ":" + startMinutes + " - " + item.getEndTime().getHour() + ":" + endMinutes);
            holder.remove.setOnClickListener(v -> {
                activities.remove(position);
                viewModel.getAgendas().setValue(activities);
            });
        }

        @Override
        public int getItemCount() {
            return activities.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            TextView name, description, location, time;
            ImageButton remove;

            ViewHolder(View itemView) {
                super(itemView);
                name = itemView.findViewById(R.id.agenda_name);
                description = itemView.findViewById(R.id.agenda_description);
                location = itemView.findViewById(R.id.agenda_location);
                time = itemView.findViewById(R.id.agenda_time);
                remove = itemView.findViewById(R.id.agenda_remove);
            }
        }
    }
}
