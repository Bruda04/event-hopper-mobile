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

/**
 * A simple {@link Fragment} subclass.
 * Use the  factory method to
 * create an instance of this fragment.
 */
public class PupImageUploadFragment extends Fragment {

    private Button nextBtn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pup_image_upload, container, false);



        nextBtn = view.findViewById(R.id.next_btn);

        nextBtn.setOnClickListener(v -> handleRegistration(savedInstanceState));

        return view;
    }

    private void handleRegistration(Bundle savedInstanceState) {



        // You can now handle the registration logic with these values
        //Log.d("Registration", "User: " + savedInstanceState.toString());

        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        Fragment fragment = new ConfirmEmailFragment();
        fragment.setArguments(savedInstanceState);
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
