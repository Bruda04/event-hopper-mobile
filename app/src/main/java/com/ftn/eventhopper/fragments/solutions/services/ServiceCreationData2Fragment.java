package com.ftn.eventhopper.fragments.solutions.services;

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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.ftn.eventhopper.R;
import com.ftn.eventhopper.adapters.ImagePreviewAdapter;
import com.ftn.eventhopper.filters.MinMaxInputFilter;
import com.ftn.eventhopper.fragments.solutions.services.viewmodels.ServiceCreationViewModel;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public class ServiceCreationData2Fragment extends Fragment {
    private NavController navController;
    private ServiceCreationViewModel viewModel;
    private Button createButton;
    private Button backButton;

    private MaterialButton uploadImagesButton;
    private RecyclerView imagePreviewRecyclerView;
    private ImagePreviewAdapter imagePreviewAdapter;
    private TextView imagesError;


    private TextInputLayout reservationWindowInput, durationInput, cancellationWindowInput, priceInput, discountInput;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requireActivity().getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                viewModel.reset();
                navController.popBackStack(R.id.pup_services, false);
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_service_creation_data2, container, false);
        navController = NavHostFragment.findNavController(this);
        viewModel = new ViewModelProvider(requireActivity(),
                new ViewModelProvider.NewInstanceFactory()).get(ServiceCreationViewModel.class);

        createButton = view.findViewById(R.id.next_button);
        backButton = view.findViewById(R.id.back_button);
        reservationWindowInput = view.findViewById(R.id.reservation_window);
        durationInput = view.findViewById(R.id.duration);
        cancellationWindowInput = view.findViewById(R.id.cancellation_window);
        priceInput = view.findViewById(R.id.service_price);
        discountInput = view.findViewById(R.id.service_discount);
        uploadImagesButton = view.findViewById(R.id.upload_images_btn);
        imagesError = view.findViewById(R.id.imagesError);
        imagePreviewRecyclerView = view.findViewById(R.id.image_preview_recycler_view);

        Objects.requireNonNull(priceInput.getEditText())
                .setFilters(new InputFilter[]{new MinMaxInputFilter(0.0, 10000.0)});
        Objects.requireNonNull(discountInput.getEditText())
                .setFilters(new InputFilter[]{new MinMaxInputFilter(0.0, 100.0)});
        Objects.requireNonNull(reservationWindowInput.getEditText())
                .setFilters(new InputFilter[]{new MinMaxInputFilter(0, 365)});
        Objects.requireNonNull(cancellationWindowInput.getEditText())
                .setFilters(new InputFilter[]{new MinMaxInputFilter(0, 365)});
        Objects.requireNonNull(durationInput.getEditText())
                .setFilters(new InputFilter[]{new MinMaxInputFilter(0, 1440)});

        setFields();

        // Set up button actions
        createButton.setOnClickListener(v -> {
            if (validate()) {
                patchService();
                viewModel.createService();
            }
        });
        backButton.setOnClickListener(v -> {
            patchService();
            navController.navigate(R.id.back_to_service_creation1);
        });

        viewModel.getCreated().observe(getViewLifecycleOwner(), created -> {
            if (created) {
                viewModel.setCreated(false);
                Toast.makeText(getContext(), "Service Created!", Toast.LENGTH_SHORT).show();
                navController.navigate(R.id.back_to_pup_services);
            }
        });


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
        if (priceInput.getEditText().getText() == null || priceInput.getEditText().getText().toString().trim().isEmpty()) {
            priceInput.setError("Price is required");
            return false;
        } else {
            priceInput.setError(null);
        }

        if (discountInput.getEditText().getText() == null || discountInput.getEditText().getText().toString().trim().isEmpty()) {
            discountInput.setError("Discount is required");
            return false;
        } else {
            discountInput.setError(null);
        }

        if (durationInput.getEditText().getText() == null || durationInput.getEditText().getText().toString().trim().isEmpty()) {
            durationInput.setError("Duration is required");
            return false;
        } else {
            durationInput.setError(null);
        }

        if (reservationWindowInput.getEditText().getText() == null || reservationWindowInput.getEditText().getText().toString().trim().isEmpty()) {
            reservationWindowInput.setError("Reservation window is required");
            return false;
        } else {
            reservationWindowInput.setError(null);
        }

        if (cancellationWindowInput.getEditText().getText() == null || cancellationWindowInput.getEditText().getText().toString().trim().isEmpty()) {
            cancellationWindowInput.setError("Cancellation window is required");
            return false;
        } else {
            cancellationWindowInput.setError(null);
        }

        if (viewModel.getUploadedImages().isEmpty()) {
            imagesError.setVisibility(View.VISIBLE);
            return false;
        } else {
            imagesError.setVisibility(View.GONE);
        }

        return true;
    }

    private void patchService() {
        String price = priceInput.getEditText().getText().toString().trim();
        String discount = discountInput.getEditText().getText().toString().trim();
        String duration = durationInput.getEditText().getText().toString().trim();
        String reservationWindow = reservationWindowInput.getEditText().getText().toString().trim();
        String cancellationWindow = cancellationWindowInput.getEditText().getText().toString().trim();

        if (!price.isEmpty()) {
            viewModel.getService().setBasePrice(parseDouble(price));
        }
        if (!discount.isEmpty()) {
            viewModel.getService().setDiscount(parseDouble(discount));
        }
        if (!duration.isEmpty()) {
            viewModel.getService().setDurationMinutes(Integer.parseInt(durationInput.getEditText().getText().toString().trim()));
        }
        if (!reservationWindow.isEmpty()) {
            viewModel.getService().setReservationWindowDays(Integer.parseInt(reservationWindowInput.getEditText().getText().toString().trim()));
        }
        if (!cancellationWindow.isEmpty()) {
            viewModel.getService().setCancellationWindowDays(Integer.parseInt(cancellationWindowInput.getEditText().getText().toString().trim()));
        }
    }

    private void setFields() {
        reservationWindowInput.getEditText().setText(String.valueOf(viewModel.getService().getReservationWindowDays()));
        durationInput.getEditText().setText(String.valueOf(viewModel.getService().getDurationMinutes()));
        cancellationWindowInput.getEditText().setText(String.valueOf(viewModel.getService().getCancellationWindowDays()));
        priceInput.getEditText().setText(String.valueOf(viewModel.getService().getBasePrice()));
        discountInput.getEditText().setText(String.valueOf(viewModel.getService().getDiscount()));

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
