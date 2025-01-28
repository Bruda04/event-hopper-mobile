package com.ftn.eventhopper.fragments.profile;

import static android.app.Activity.RESULT_OK;

import static com.ftn.eventhopper.R.*;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.ftn.eventhopper.R;
import com.ftn.eventhopper.activities.MainActivity;
import com.ftn.eventhopper.clients.ClientUtils;
import com.ftn.eventhopper.clients.services.auth.UserService;
import com.ftn.eventhopper.fragments.profile.viewmodels.ProfileViewModel;
import com.ftn.eventhopper.shared.dtos.profile.ProfileForPersonDTO;
import com.ftn.eventhopper.shared.models.users.PersonType;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.io.IOException;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {

    private ProfileViewModel viewModel;
    private PersonType role = UserService.getUserRole();
    private ImageView profileImage;
    private TextView userFullName, userAddress, userEmail, roleTitle, userPhoneNumber;

    private SwipeRefreshLayout swipeRefreshLayout;


    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        viewModel = new ViewModelProvider(requireActivity()).get(ProfileViewModel.class);

        NavController navController = NavHostFragment.findNavController(this);

        this.assignButtons(view, navController);

        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(() -> {
            fetchProfileInformation(true);
        });

        this.fetchProfileInformation(false);

        this.roleTitle = view.findViewById(R.id.role);
        this.userFullName = view.findViewById(id.userName);
        this.userAddress = view.findViewById(R.id.userAddress);
        this.userEmail = view.findViewById(R.id.userEmail);
        this.userPhoneNumber = view.findViewById(R.id.phoneNumber);

        this.handleCardVisibility(view);

        return view;
    }

    private void assignButtons(View view, NavController navController){
        view.findViewById(R.id.ListItemMyServices).setOnClickListener(v -> {
            navController.navigate(R.id.action_to_pup_services);
        });

        view.findViewById(R.id.ListItemCategories).setOnClickListener(v -> {
            navController.navigate(R.id.action_to_manage_categories);
        });

        view.findViewById(R.id.ListItemMyPrices).setOnClickListener(v -> {
            navController.navigate(R.id.action_to_manage_prices);
        });

        view.findViewById(R.id.ListItemManageComments).setOnClickListener( v -> {
            navController.navigate(R.id.action_to_manage_comments);
        });

        view.findViewById(R.id.ListItemDeactivateProfile).setOnClickListener(v -> {
            this.openDeactivateAccountDialog();
        });

        view.findViewById(R.id.ListItemChangePassword).setOnClickListener(v -> {
            this.openChangePasswordDialog();

        });

        view.findViewById(R.id.ListItemLogOut).setOnClickListener(v -> {
            viewModel.logout();
            ((MainActivity) requireActivity()).navigateToAuthGraph();
        });

        view.findViewById(R.id.ListItemUpgradeProfile).setOnClickListener(v -> {
            navController.navigate(R.id.action_to_choose_role);
        });

        this.profileImage = view.findViewById(R.id.profileImage);
        profileImage.setOnClickListener(v -> {
            showImageDialog(view);
        });

        ImageView editPersonIcon = view.findViewById(R.id.editPersonIcon);
        editPersonIcon.setOnClickListener(v -> openEditPersonDialog());

    }

    private void handleCardVisibility(View view){
        CardView changePasswordCard = view.findViewById(R.id.ListItemChangePassword);
        CardView myProductsCard = view.findViewById(R.id.ListItemMyProducts);
        CardView myServicesCard = view.findViewById(R.id.ListItemMyServices);
        CardView myPricesCard = view.findViewById(R.id.ListItemMyPrices);
        CardView categoriesCard = view.findViewById(R.id.ListItemCategories);
        CardView eventTypesCard = view.findViewById(R.id.ListItemEventTypes);
        CardView myEventsCard = view.findViewById(R.id.ListItemMyEvents);
        CardView logOutCard = view.findViewById(R.id.ListItemLogOut);
        CardView deactivateProfileCard = view.findViewById(R.id.ListItemDeactivateProfile);
        CardView upgradeProfileCard = view.findViewById(R.id.ListItemUpgradeProfile);
        CardView commentsCard = view.findViewById(R.id.ListItemManageComments);

        myProductsCard.setVisibility(View.GONE);
        myServicesCard.setVisibility(View.GONE);
        myPricesCard.setVisibility(View.GONE);
        categoriesCard.setVisibility(View.GONE);
        eventTypesCard.setVisibility(View.GONE);
        myEventsCard.setVisibility(View.GONE);
        upgradeProfileCard.setVisibility(View.GONE);
        commentsCard.setVisibility(View.GONE);

        changePasswordCard.setVisibility(View.VISIBLE);
        logOutCard.setVisibility(View.VISIBLE);
        deactivateProfileCard.setVisibility(View.VISIBLE);

        //set the title
        switch (role.toString()) {
            case "SERVICE_PROVIDER":
                myProductsCard.setVisibility(View.VISIBLE);
                myServicesCard.setVisibility(View.VISIBLE);
                myPricesCard.setVisibility(View.VISIBLE);
                break;

            case "ADMIN":
                categoriesCard.setVisibility(View.VISIBLE);
                eventTypesCard.setVisibility(View.VISIBLE);
                commentsCard.setVisibility(View.VISIBLE);
                break;

            case "EVENT_ORGANIZER":
                myEventsCard.setVisibility(View.VISIBLE);
                break;

            default:    //authenticated user
                upgradeProfileCard.setVisibility(View.VISIBLE);
                break;
        }
    }

    private void setRoleTitle(){
        if(this.role == PersonType.SERVICE_PROVIDER){
            roleTitle.setText(R.string.provider);
        }
        if(this.role == PersonType.EVENT_ORGANIZER){
            roleTitle.setText(R.string.organizer);
        }
        if(this.role == PersonType.ADMIN){
            roleTitle.setText(R.string.admin);
        }
        if(this.role == PersonType.AUTHENTICATED_USER){
            roleTitle.setText(R.string.user);
        }
    }

    private void fetchProfileInformation(boolean refresh){
        //if you're told to refresh, or this is your first time and you have to
        if(refresh || viewModel.getProfile() == null){
            viewModel.fetchProfile();
        }
        viewModel.getProfileChanged().observe(getViewLifecycleOwner(), changed -> {
            if (changed) {
                this.setRoleTitle();
                ProfileForPersonDTO profile = viewModel.getProfile();
                // Populate User Info
                userFullName.setText(String.format("%s %s", profile.getName(), profile.getSurname()));
                userAddress.setText(String.format("%s, %s", profile.getLocation().getAddress(), profile.getLocation().getCity()));
                userEmail.setText(profile.getEmail());
                userPhoneNumber.setText(String.format("+%s", profile.getPhoneNumber()));

                String profilePicUrl = profile.getProfilePicture();
                String fullProfileImageUrl = (profilePicUrl == null || profilePicUrl.isEmpty())
                        ? String.format("%s/%s", ClientUtils.SERVICE_API_IMAGE_PATH, "profile.png")
                        : String.format("%s/%s", ClientUtils.SERVICE_API_IMAGE_PATH, profilePicUrl);

                Glide.with(requireContext())
                        .load(fullProfileImageUrl)
                        .circleCrop()
                        .into(profileImage);
            }
            swipeRefreshLayout.setRefreshing(false); // Stop the refresh animation
        });
    }

    private void showImageDialog(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_profile_picture, null);
        builder.setView(dialogView);

        AlertDialog dialog = builder.create();
        TextView uploadOption = dialogView.findViewById(R.id.uploadOption);
        TextView removeOption = dialogView.findViewById(R.id.removeOption);

        uploadOption.setOnClickListener(v -> {
            changeProfilePicture();
            dialog.dismiss();
        });

        removeOption.setOnClickListener(v -> {
            removeProfilePicture(view);
            dialog.dismiss();
        });

        dialog.show();
    }

    private void openDeactivateAccountDialog() {
        MaterialAlertDialogBuilder confirmDialog = new MaterialAlertDialogBuilder(requireContext());
        confirmDialog.setTitle("Are you sure you want to deactivate your account?");
        confirmDialog.setMessage("This action can't be undone.");
        confirmDialog.setPositiveButton("Yes", (dialog, which) -> {
            viewModel.deactivateAccount();
            viewModel.getDeactivateAccountSuccess().observe(getViewLifecycleOwner(), success -> {
                if (success) {
                    ((MainActivity) requireActivity()).navigateToAuthGraph();
                }else{
                    String failureMessage;
                    if(this.role == PersonType.SERVICE_PROVIDER){
                        failureMessage = "Failed: You have upcoming bookings.";
                    }else if(this.role == PersonType.EVENT_ORGANIZER){
                        failureMessage = "Failed: You have upcoming events.";
                    }else{
                        failureMessage = "Account deactivation failed, try again later.";
                    }
                    Toast.makeText(requireContext(), failureMessage, Toast.LENGTH_LONG).show();
                }
            });
        });
        confirmDialog.setNegativeButton("No", (dialog, which) -> {
        });
        confirmDialog.show();
    }

    private void openChangePasswordDialog(){
        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_change_password, null);

        TextInputLayout oldPasswordInput = dialogView.findViewById(R.id.old_password_layout);
        TextInputLayout newPasswordInput = dialogView.findViewById(R.id.new_password_layout);
        TextInputLayout newPasswordAgainInput = dialogView.findViewById(R.id.new_password_again_layout);
        TextView message_label = dialogView.findViewById(R.id.message_label);

        Objects.requireNonNull(oldPasswordInput.getEditText());
        Objects.requireNonNull(newPasswordInput.getEditText());
        Objects.requireNonNull(newPasswordAgainInput.getEditText());


        MaterialAlertDialogBuilder dialogBuilder = new MaterialAlertDialogBuilder(getContext());
        dialogBuilder.setTitle("Change Password");
        dialogBuilder.setView(dialogView);
        dialogBuilder.setPositiveButton("Save", (dialogInterface, i) -> {
        });
        dialogBuilder.setNegativeButton("Cancel", (dialogInterface, i) -> {
        });
        androidx.appcompat.app.AlertDialog changeDialog = dialogBuilder.create();
        changeDialog.show();

        viewModel = new ViewModelProvider(this).get(ProfileViewModel.class);

        // Observe the LiveData for message updates
        viewModel.getErrorMessagePassword().observe(getViewLifecycleOwner(), message -> {
            if (message != null) {
                message_label.setText(message);
                message_label.setVisibility(View.VISIBLE);
            } else {
                message_label.setVisibility(View.GONE);
            }
        });

        changeDialog.getButton(androidx.appcompat.app.AlertDialog.BUTTON_POSITIVE).setOnClickListener(v1 -> {
            boolean isValid = true;

            if (oldPasswordInput.getEditText().getText().toString().trim().isEmpty()) {
                oldPasswordInput.setError("Old password is required");
                isValid = false;
            } else {
                oldPasswordInput.setError(null);
            }

            String newPassword = newPasswordInput.getEditText().getText().toString().trim();
            if (newPassword.isEmpty()) {
                newPasswordInput.setError("New password is required");
                isValid = false;
            } else if (newPassword.length() < 8) {
                newPasswordInput.setError("Must be at least 8 characters long");
                isValid = false;
            } else if (!newPassword.matches(".*[A-Z].*")) {
                newPasswordInput.setError("Must contain at least one uppercase letter");
                isValid = false;
            } else if (!newPassword.matches(".*[0-9].*")) {
                newPasswordInput.setError("Must contain at least one number");
                isValid = false;
            } else {
                newPasswordInput.setError(null);
            }

            if (newPasswordAgainInput.getEditText().getText().toString().trim().isEmpty()) {
                newPasswordAgainInput.setError("This field is required");
                isValid = false;
            } else if (!newPasswordInput.getEditText().getText().toString().trim()
                    .equals(newPasswordAgainInput.getEditText().getText().toString().trim())) {
                newPasswordAgainInput.setError("Passwords don't match.");
                isValid = false;
            } else {
                newPasswordAgainInput.setError(null);
            }

            // Proceed if all inputs are valid
            Log.d("VALID", String.valueOf(isValid));
            if (isValid) {
                String oldPasswordStr = oldPasswordInput.getEditText().getText().toString().trim();
                String newPasswordStr = newPasswordInput.getEditText().getText().toString().trim();

                // Call the ViewModel's changePassword method
                viewModel.changePassword(oldPasswordStr, newPasswordStr);

                // Observe changes to the error message
                viewModel.getErrorMessagePassword().observe(getViewLifecycleOwner(), message -> {
                    if (message != null) {
                        message_label.setText(message);
                        message_label.setVisibility(View.VISIBLE);
                        if (!message.equals("Password changed successfully!")) {
                            message_label.setTextColor(ContextCompat.getColor(requireContext(), R.color.md_theme_error));
                        }
                        if (message.equals("Password changed successfully!")) {
                            message_label.setTextColor(ContextCompat.getColor(requireContext(), color.text_dark_blue));
                            viewModel.getErrorMessagePassword().removeObservers(getViewLifecycleOwner());
                            // Dismiss the dialog after 2 seconds so the user can see the message
                            new Handler(Looper.getMainLooper()).postDelayed(changeDialog::dismiss, 1500);
                        }
                    }
                });
            }
        });
    }

    private void openEditPersonDialog(){
        View dialogView = LayoutInflater.from(getContext()).inflate(layout.dialog_edit_person, null);
        TextInputLayout nameInput = dialogView.findViewById(R.id.name_layout);
        TextInputLayout surnameInput = dialogView.findViewById(R.id.surname_layout);
        TextInputLayout phoneNumberInput = dialogView.findViewById(R.id.phone_number_layout);
        TextInputLayout addressInput = dialogView.findViewById(R.id.address_layout);
        TextInputLayout cityInput = dialogView.findViewById(R.id.city_layout);


        TextInputEditText nameField = dialogView.findViewById(R.id.name_field);
        TextInputEditText surnameField = dialogView.findViewById(R.id.surname_field);
        TextInputEditText phoneNumberField = dialogView.findViewById(R.id.phone_number_field);
        TextInputEditText addressField = dialogView.findViewById(R.id.address_field);
        TextInputEditText cityField = dialogView.findViewById(R.id.city_field);

        nameField.setText(viewModel.getProfile().getName());
        surnameField.setText(viewModel.getProfile().getSurname());
        phoneNumberField.setText(viewModel.getProfile().getPhoneNumber());
        addressField.setText(viewModel.getProfile().getLocation().getAddress());
        cityField.setText(viewModel.getProfile().getLocation().getCity());


        Objects.requireNonNull(nameInput.getEditText());
        Objects.requireNonNull(surnameInput.getEditText());
        Objects.requireNonNull(phoneNumberInput.getEditText());
        Objects.requireNonNull(addressInput.getEditText());
        Objects.requireNonNull(cityInput.getEditText());


        MaterialAlertDialogBuilder dialogBuilder = new MaterialAlertDialogBuilder(getContext());
        dialogBuilder.setTitle("Edit Profile");
        dialogBuilder.setView(dialogView);
        dialogBuilder.setPositiveButton("Save", (dialogInterface, i) -> {
        });
        dialogBuilder.setNegativeButton("Cancel", (dialogInterface, i) -> {
        });
        androidx.appcompat.app.AlertDialog changeDialog = dialogBuilder.create();
        changeDialog.show();

        changeDialog.getButton(androidx.appcompat.app.AlertDialog.BUTTON_POSITIVE).setOnClickListener(v1 -> {
            boolean isValid = true;

            if (nameInput.getEditText().getText().toString().trim().isEmpty()) {
                nameInput.setError("Name is required");
                isValid = false;
            } else {
                nameInput.setError(null);
            }

            if (surnameInput.getEditText().getText().toString().trim().isEmpty()) {
                surnameInput.setError("Surname is required");
                isValid = false;
            } else {
                surnameInput.setError(null);
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
                String name = nameInput.getEditText().getText().toString().trim();
                String surname = surnameInput.getEditText().getText().toString().trim();
                String phoneNumber = phoneNumberInput.getEditText().getText().toString().trim();
                String address = addressInput.getEditText().getText().toString().trim();
                String city = cityInput.getEditText().getText().toString().trim();


                // Call the ViewModel's changePassword method
                viewModel.editPerson(name, surname, phoneNumber, address, city);
                viewModel.getEditPersonProfileSuccess().observe(getViewLifecycleOwner(), success -> {
                    if(success){
                        this.userPhoneNumber.setText(String.format("+%s", phoneNumber));
                        this.userFullName.setText(String.format("%s %s", name, surname));
                        this.userAddress.setText(String.format("%s, %s", address, city));
                    }
                    changeDialog.dismiss();
                });
            }
        });
    }

    private void removeProfilePicture(View view){
        ImageView profileImage = view.findViewById(R.id.profileImage);
        Glide.with(requireContext())
                .load(String.format("%s/%s", ClientUtils.SERVICE_API_IMAGE_PATH, "profile.png"))
                .circleCrop()
                .into(profileImage);
        viewModel.removeProfilePicture();
    }

    private void changeProfilePicture() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*"); // Restrict to image files
        startActivityForResult(intent, 1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri selectedImageUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedImageUri);

                viewModel.changeProfilePicture(bitmap);

                viewModel.getImageUrlLiveData().observe(getViewLifecycleOwner(), newImageUrl -> {
                    if (newImageUrl != null) {
                        // Update the UI with the new profile picture
                        Glide.with(requireContext())
                                .load(String.format("%s/%s", ClientUtils.SERVICE_API_IMAGE_PATH, newImageUrl))
                                .circleCrop()
                                .into(profileImage);
                    }
                });

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}