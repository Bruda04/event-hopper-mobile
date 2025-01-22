package com.ftn.eventhopper.fragments.profile;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.viewpager2.widget.ViewPager2;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ftn.eventhopper.R;
import com.ftn.eventhopper.adapters.ImageSliderAdapter;
import com.ftn.eventhopper.clients.services.auth.UserService;
import com.ftn.eventhopper.fragments.profile.viewmodels.ProfileViewModel;
import com.ftn.eventhopper.shared.models.users.PersonType;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.List;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CompanyFragment} factory method to
 * create an instance of this fragment.
 */
public class CompanyFragment extends Fragment {

    private ProfileViewModel viewModel;

    private ViewPager2 companyImagesSlider;

    private TextView companyName, companyAddress, companyDescription, companyEmail, companyPhoneNumber;

    private String address, city, phoneNumber;
    private boolean refreshCompany = false;


    public CompanyFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_company, container, false);
        viewModel = new ViewModelProvider(requireActivity()).get(ProfileViewModel.class);

        NavController navController = NavHostFragment.findNavController(this);

        this.companyName = view.findViewById(R.id.CompanyName);
        this.companyAddress = view.findViewById(R.id.companyAddress);
        this.companyDescription = view.findViewById(R.id.companyDescription);
        this.companyEmail = view.findViewById(R.id.companyEmail);
        this.companyPhoneNumber = view.findViewById(R.id.companyPhoneNumber);
        this.companyImagesSlider = view.findViewById(R.id.provider_company_image_slider);

        ImageView editPersonIcon = view.findViewById(R.id.editCompanyIcon);
        editPersonIcon.setOnClickListener(v -> openEditCompanyDialog());

        this.fillCompanyData();
        viewModel.fetchProfile();

        return view;
    }

    private void fillCompanyData(){
        viewModel.getProfileData().observe(getViewLifecycleOwner(), profile -> {
            if (profile != null && !refreshCompany) {
                companyName.setText(profile.getCompanyName());
                companyAddress.setText(profile.getCompanyLocation() != null ? profile.getCompanyLocation().getAddress() + ", " + profile.getCompanyLocation().getCity() : "Not found");
                companyDescription.setText(profile.getCompanyDescription());
                companyEmail.setText(profile.getCompanyEmail());
                companyPhoneNumber.setText(String.format("+%s", profile.getCompanyPhoneNumber()));

                this.address = profile.getCompanyLocation().getAddress();
                this.city = profile.getCompanyLocation().getCity();
                this.phoneNumber = profile.getCompanyPhoneNumber();

                String[] imageUrls = profile.getCompanyPhotos().toArray(String[]::new);
                ImageSliderAdapter imageSliderAdapter = new ImageSliderAdapter(List.of(imageUrls));
                companyImagesSlider.setAdapter(imageSliderAdapter);
            }
            refreshCompany = false;
        });

    }


    private void openEditCompanyDialog(){
        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_edit_company, null);
        TextInputLayout descriptionInput = dialogView.findViewById(R.id.description_layout);
        TextInputLayout phoneNumberInput = dialogView.findViewById(R.id.phone_number_layout);
        TextInputLayout addressInput = dialogView.findViewById(R.id.address_layout);
        TextInputLayout cityInput = dialogView.findViewById(R.id.city_layout);


        TextInputEditText descriptionField = dialogView.findViewById(R.id.description_field);
        TextInputEditText phoneNumberField = dialogView.findViewById(R.id.phone_number_field);
        TextInputEditText addressField = dialogView.findViewById(R.id.address_field);
        TextInputEditText cityField = dialogView.findViewById(R.id.city_field);

        //they have a custom format to display, that's why they can't be simply extracted from the layout fields
        phoneNumberField.setText(this.phoneNumber);
        addressField.setText(this.address);
        cityField.setText(this.city);
        descriptionField.setText(this.companyDescription.getText());


        Objects.requireNonNull(descriptionInput.getEditText());
        Objects.requireNonNull(phoneNumberInput.getEditText());
        Objects.requireNonNull(addressInput.getEditText());
        Objects.requireNonNull(cityInput.getEditText());


        MaterialAlertDialogBuilder dialogBuilder = new MaterialAlertDialogBuilder(getContext());
        dialogBuilder.setTitle("Edit Company");
        dialogBuilder.setView(dialogView);
        dialogBuilder.setPositiveButton("Save", (dialogInterface, i) -> {
        });
        dialogBuilder.setNegativeButton("Cancel", (dialogInterface, i) -> {
        });
        androidx.appcompat.app.AlertDialog changeDialog = dialogBuilder.create();
        changeDialog.show();

        viewModel = new ViewModelProvider(this).get(ProfileViewModel.class);


        changeDialog.getButton(androidx.appcompat.app.AlertDialog.BUTTON_POSITIVE).setOnClickListener(v1 -> {
            boolean isValid = true;

            if (descriptionInput.getEditText().getText().toString().trim().isEmpty()) {
                descriptionInput.setError("Description is required");
                isValid = false;
            } else {
                descriptionInput.setError(null);
            }

            if (addressInput.getEditText().getText().toString().trim().isEmpty()) {
                addressInput.setError("Address is required");
                isValid = false;
            } else {
                addressInput.setError(null);
            }
            if (cityInput.getEditText().getText().toString().trim().isEmpty()) {
                cityInput.setError("City is required");
                isValid = false;
            } else {
                cityInput.setError(null);
            }

            if (phoneNumberInput.getEditText().getText().toString().trim().isEmpty()) {
                phoneNumberInput.setError("Phone number is required");
                isValid = false;
            } else if (phoneNumberInput.getEditText().getText().toString().trim().length() < 8) {
                phoneNumberInput.setError("Phone number must be >8 digits");
                isValid = false;
            } else {
                phoneNumberInput.setError(null);
            }


            // Proceed if all inputs are valid
            Log.d("VALID", String.valueOf(isValid));
            if (isValid) {
                String description = descriptionInput.getEditText().getText().toString().trim();
                String phoneNumber = phoneNumberInput.getEditText().getText().toString().trim();
                String address = addressInput.getEditText().getText().toString().trim();
                String city = cityInput.getEditText().getText().toString().trim();

                // Call the ViewModel's changePassword method
                viewModel.editCompany(description, phoneNumber, address, city);
                viewModel.getEditCompanyProfileSuccess().observe(getViewLifecycleOwner(), success -> {
                    if(success){
                        this.phoneNumber = phoneNumber;
                        this.address = address;
                        this.city = city;
                        this.companyPhoneNumber.setText(String.format("+%s", phoneNumber));
                        this.companyAddress.setText(String.format("%s, %s", address, city));
                        this.companyDescription.setText(description);
                    }
                    this.refreshCompany = true;
                    changeDialog.dismiss();
                });
            }
        });
    }
}