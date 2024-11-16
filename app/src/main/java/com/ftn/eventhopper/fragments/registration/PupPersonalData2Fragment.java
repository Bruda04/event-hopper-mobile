package com.ftn.eventhopper.fragments.registration;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.ftn.eventhopper.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

/**
     * A simple {@link Fragment} subclass.
     * Use the  factory method to
     * create an instance of this fragment.
     */
public class PupPersonalData2Fragment extends Fragment {

    private TextInputEditText descriptionField, cityField, addressField;
    private TextInputLayout descriptionLayout, cityLayout, addressLayout;
    private String description, city, address;

    private Button nextBtn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pup_personal_data2, container, false);
        Bundle bundle = getArguments();
        if (bundle != null) {
            Log.d("OrganizerPersonalData2", "User data: " + bundle.toString());
        }

        retrieveFields(view);
        nextBtn = view.findViewById(R.id.next_btn);

        nextBtn.setOnClickListener(v -> {
            retrieveData();
            if(!validateFields()){
                handleImageUploadRedirection(bundle);
            }
        });
        return view;
    }



    private void retrieveFields(View view){
        cityLayout = view.findViewById(R.id.register_city_layout);
        descriptionLayout = view.findViewById(R.id.register_description_layout);
        addressLayout = view.findViewById(R.id.register_address_layout);

        cityField = (TextInputEditText) cityLayout.getEditText();
        descriptionField = (TextInputEditText) descriptionLayout.getEditText();
        addressField = (TextInputEditText) addressLayout.getEditText();
    }


    private void retrieveData(){
        description = descriptionField != null ? descriptionField.getText().toString().trim() : "";
        city = cityField != null ? cityField.getText().toString().trim() : "";
        address = addressField != null ? addressField.getText().toString().trim() : "";
    }


    private boolean validateFields(){
        boolean hasError = false;

        if (city.isEmpty()) {
            cityLayout.setError("City is required"); // Show error message
            cityLayout.setBoxStrokeColor(getResources().getColor(R.color.red)); // Highlight in red
            hasError = true;
        } else {
            cityLayout.setError(null); // Clear error
            cityLayout.setBoxStrokeColor(getResources().getColor(R.color.white)); // Reset border color
        }

        if (description.isEmpty()) {
            descriptionLayout.setError("Description is required. ");
            descriptionLayout.setBoxStrokeColor(getResources().getColor(R.color.red));
            hasError = true;
        } else {
            descriptionLayout.setError(null);
            descriptionLayout.setBoxStrokeColor(getResources().getColor(R.color.white)); // Reset border color
        }

        if (address.isEmpty()) {
            addressLayout.setError("Address is required"); // Show error message
            addressLayout.setBoxStrokeColor(getResources().getColor(R.color.red)); // Highlight in red
            hasError = true;
        } else {
            addressLayout.setError(null); // Clear error
            addressLayout.setBoxStrokeColor(getResources().getColor(R.color.white)); // Reset border color
        }

        return hasError;
    }



    private void handleImageUploadRedirection(Bundle savedInstanceState) {
        savedInstanceState.putString("description", description);
        savedInstanceState.putString("city", city);
        savedInstanceState.putString("address", address);


        // You can now handle the registration logic with these values
        Log.d("Registration", "User: " + savedInstanceState.toString());

        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        Fragment fragment = new PupImageUploadFragment();
        fragment.setArguments(savedInstanceState);
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
