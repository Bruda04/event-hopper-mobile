package com.ftn.eventhopper.fragments.upgrades;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ftn.eventhopper.R;
import com.ftn.eventhopper.adapters.ImagePreviewAdapter;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.io.IOException;
import java.util.ArrayList;


public class CompanyData2Fragment extends Fragment {

    private ArrayList<ImagePreviewAdapter.ImagePreviewItem> uploadedProfilePicture = new ArrayList<>();
    private MaterialButton uploadImageButton;
    private ImagePreviewAdapter imagePreviewAdapter;
    private RecyclerView imagePreviewRecyclerView;
    private NavController navController;
    private TextInputEditText descriptionField;
    private TextInputLayout descriptionLayout;
    private String description;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_company_data2, container, false);
        navController = NavHostFragment.findNavController(this);
        uploadImageButton = view.findViewById(R.id.upload_profile_picture);
        uploadImageButton.setOnClickListener(v -> selectImages());
        imagePreviewAdapter= new ImagePreviewAdapter(this.uploadedProfilePicture, this::removeImage);
        imagePreviewRecyclerView = view.findViewById(R.id.image_preview);
        imagePreviewRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        imagePreviewRecyclerView.setAdapter(imagePreviewAdapter);


        Bundle receivedBundle = getArguments();

        retrieveFields(view);

        view.findViewById(R.id.next_btn).setOnClickListener(v -> {
            retrieveData();
            if(!validateFields()){
                receivedBundle.putString("description", description);
                if(!this.uploadedProfilePicture.isEmpty()){
                    receivedBundle.putSerializable("profilePicture", this.uploadedProfilePicture.get(0));
                }

                //action to pup image upload??????????????????????????????
                navController.navigate(R.id.action_to_success, receivedBundle);
            }
        });
        return view;
    }

    private void retrieveFields(View view){
        descriptionLayout = view.findViewById(R.id.register_description_layout);

        descriptionField = (TextInputEditText) descriptionLayout.getEditText();
    }


    private void retrieveData(){
        description = descriptionField != null ? descriptionField.getText().toString().trim() : "";
    }


    private boolean validateFields(){
        boolean hasError = false;

        if (description.isEmpty()) {
            descriptionLayout.setError("Description is required. ");
            descriptionLayout.setBoxStrokeColor(getResources().getColor(R.color.red));
            hasError = true;
        } else {
            descriptionLayout.setError(null);
            descriptionLayout.setBoxStrokeColor(getResources().getColor(R.color.white)); // Reset border color
        }


        return hasError;
    }


    private void selectImages() {
        //for profile picture there is only one that can be selected at a time
        if(!this.uploadedProfilePicture.isEmpty()){
            this.removeImage(0);
        }
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, false);
        startActivityForResult(intent, 101);
    }

    private void removeImage(int position) {
        this.uploadedProfilePicture.remove(position);
        imagePreviewAdapter.notifyItemRemoved(position);
        imagePreviewAdapter.notifyItemRangeChanged(position, this.uploadedProfilePicture.size());
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 101 && resultCode == RESULT_OK) {
            // Single image selected
            Uri imageUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), imageUri);
                this.uploadedProfilePicture.add(new ImagePreviewAdapter.ImagePreviewItem(bitmap));
            } catch (IOException e) {
                e.printStackTrace();
            }
            imagePreviewAdapter.notifyDataSetChanged();
        }
    }

}