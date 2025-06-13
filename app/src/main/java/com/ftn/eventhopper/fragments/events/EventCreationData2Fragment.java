package com.ftn.eventhopper.fragments.events;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.MediaStore;
import android.text.InputFilter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.ftn.eventhopper.R;
import com.ftn.eventhopper.adapters.ImagePreviewAdapter;
import com.ftn.eventhopper.filters.MinMaxInputFilter;
import com.ftn.eventhopper.fragments.events.viewmodels.EventCreationViewModel;
import com.ftn.eventhopper.fragments.solutions.services.viewmodels.ServiceCreationViewModel;
import com.ftn.eventhopper.shared.dtos.location.CreateLocationDTO;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;

import java.io.IOException;
import java.util.Objects;


public class EventCreationData2Fragment extends Fragment {
    private NavController navController;
    private EventCreationViewModel viewModel;
    private Button createButton;
    private Button backButton;
    private MaterialButton uploadImagesButton;
    private RecyclerView imagePreviewRecyclerView;
    private ImagePreviewAdapter imagePreviewAdapter;
    private TextView imagesError;
    private TextInputLayout numParticipantsInput, cityInput, addressInput;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requireActivity().getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                navController.popBackStack(R.id.event_creation_2, true);
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_event_creation_data2, container, false);
        navController = NavHostFragment.findNavController(this);
        viewModel = new ViewModelProvider(requireActivity(),
                new ViewModelProvider.NewInstanceFactory()).get(EventCreationViewModel.class);

        createButton = view.findViewById(R.id.next_button);
        backButton = view.findViewById(R.id.back_button);
        numParticipantsInput = view.findViewById(R.id.max_participants);
        cityInput = view.findViewById(R.id.city);
        addressInput = view.findViewById(R.id.address);


        uploadImagesButton = view.findViewById(R.id.upload_images_btn);
        imagesError = view.findViewById(R.id.imagesError);
        imagePreviewRecyclerView = view.findViewById(R.id.image_preview_recycler_view);

        Objects.requireNonNull(numParticipantsInput.getEditText())
                .setFilters(new InputFilter[]{new MinMaxInputFilter(1.0, 1000000.0)});

        setFields();

        // Set up button actions
        createButton.setOnClickListener(v -> {
            if (validate()) {
                patchService();
                navController.navigate(R.id.create_agenda);
            }
        });
        backButton.setOnClickListener(v -> {
            patchService();
            navController.popBackStack(R.id.event_creation_2, true);
        });

        /*viewModel.getCreated().observe(getViewLifecycleOwner(), created -> {
            if (created) {
                viewModel.setCreated(false);

                navController.navigate(R.id.action_to_add_agenda);
            }
        });**/


        return view;
    }

    private void selectImages() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        startActivityForResult(intent, 101);
    }

    private void removeImage(int position) {
        viewModel.getUploadedImages().remove(position);
        imagePreviewAdapter.notifyItemRemoved(position);
        imagePreviewAdapter.notifyItemRangeChanged(position, viewModel.getUploadedImages().size());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 101 && resultCode == RESULT_OK) {
            if (data.getClipData() != null) {
                // Multiple images selected
                for (int i = 0; i < data.getClipData().getItemCount(); i++) {
                    Uri imageUri = data.getClipData().getItemAt(i).getUri();
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), imageUri);
                        viewModel.getUploadedImages().add(new ImagePreviewAdapter.ImagePreviewItem(bitmap));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } else if (data.getData() != null) {
                // Single image selected
                Uri imageUri = data.getData();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), imageUri);
                    viewModel.getUploadedImages().add(new ImagePreviewAdapter.ImagePreviewItem(bitmap));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            imagePreviewAdapter.notifyDataSetChanged();
        }
    }

    private boolean validate() {
        if (numParticipantsInput.getEditText().getText() == null || numParticipantsInput.getEditText().getText().toString().trim().isEmpty()) {
            numParticipantsInput.setError("Number of participants is required");
            return false;
        } else {
            numParticipantsInput.setError(null);
        }

        if (cityInput.getEditText().getText() == null || cityInput.getEditText().getText().toString().trim().isEmpty()) {
            cityInput.setError("City is required");
            return false;
        } else {
            cityInput.setError(null);
        }

        if (addressInput.getEditText().getText() == null || addressInput.getEditText().getText().toString().trim().isEmpty()) {
            addressInput.setError("Address is required");
            return false;
        } else {
            addressInput.setError(null);
        }


        if (viewModel.getUploadedImages().isEmpty()) {
            imagesError.setVisibility(View.VISIBLE);
            return false;
        } else {
            imagesError.setVisibility(View.GONE);
        }

        if(viewModel.getUploadedImages().size() > 1){
            imagesError.setVisibility(View.VISIBLE);
            return false;
        } else {
            imagesError.setVisibility(View.GONE);
        }

        return true;
    }

    private void patchService() {
        String numberOfParticipants = numParticipantsInput.getEditText().getText().toString().trim();
        String city = cityInput.getEditText().getText().toString().trim();
        String address = addressInput.getEditText().getText().toString().trim();


        if (!numberOfParticipants.isEmpty()) {
            viewModel.getEvent().setMaxAttendance((int) parseDouble(numberOfParticipants));
        }

        CreateLocationDTO location = new CreateLocationDTO();
        if (!city.isEmpty()) {
            if (!address.isEmpty()) {
                location.setCity(city);
                location.setAddress(address);
                viewModel.getEvent().setLocation(location);
            }
        }

        viewModel.uploadImages();

    }

    private void setFields() {
        // Initialize RecyclerView
        imagePreviewAdapter= new ImagePreviewAdapter(viewModel.getUploadedImages(), this::removeImage);
        imagePreviewRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        imagePreviewRecyclerView.setAdapter(imagePreviewAdapter);

        // Upload Images Button
        uploadImagesButton.setOnClickListener(v -> selectImages());

    }


    // Function to handle the parsing logic
    private double parseDouble(String input) {
        if (input.isEmpty()) {
            return 0;
        }

        // Normalize input by replacing ',' with '.'
        input = input.replace(',', '.');

        // Handle edge cases like input being only '.' or ',' or invalid numbers
        try {
            return Double.parseDouble(input);
        } catch (NumberFormatException e) {
            return 0; // Return 0 if input cannot be parsed
        }
    }
}