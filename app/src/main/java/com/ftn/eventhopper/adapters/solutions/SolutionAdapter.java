package com.ftn.eventhopper.adapters.solutions;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ftn.eventhopper.R;
import com.ftn.eventhopper.clients.ClientUtils;
import com.ftn.eventhopper.shared.dtos.solutions.SimpleProductDTO;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;

public class SolutionAdapter extends RecyclerView.Adapter<SolutionAdapter.SolutionViewHolder> {

    ArrayList<SimpleProductDTO> solutions;
    Context context;

    private final Fragment fragment;

    public SolutionAdapter(Context context, ArrayList<SimpleProductDTO> solutions, Fragment fragment){
        this.context = context;
        this.solutions = solutions;
        this.fragment = fragment;
    }

    @NonNull
    @Override
    public SolutionAdapter.SolutionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        return new SolutionAdapter.SolutionViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.card_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull SolutionAdapter.SolutionViewHolder holder, int position) {

        holder.titleView.setText(solutions.get(position).getName());
        holder.descriptionView.setText(solutions.get(position).getDescription());
        holder.secondaryView.setText(solutions.get(position).getCategory().getName() );


        Glide.with(holder.imageView.getContext())
                .load(String.format("%s/%s", ClientUtils.SERVICE_API_IMAGE_PATH, solutions.get(position).getPictures().get(0)))
                .placeholder(R.drawable.baseline_image_placeholder_24)
                .error(R.drawable.baseline_image_not_supported_24)
                .into(holder.imageView);

        holder.viewMoreButton.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putString("id", solutions.get(position).getId().toString());
            NavController navController = NavHostFragment.findNavController(this.fragment);
            navController.navigate(R.id.action_to_solution_page, bundle);
        });
    }

    @Override
    public int getItemCount() {
        return solutions.size();
    }

    public class SolutionViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView titleView;
        TextView secondaryView;
        TextView descriptionView;
        public MaterialButton viewMoreButton;

        public SolutionViewHolder(View view) {
            super(view);
            this.viewMoreButton = view.findViewById(R.id.card_button);

            imageView = view.findViewById(R.id.card_image);
            titleView = view.findViewById(R.id.card_title);
            secondaryView = view.findViewById(R.id.card_secondary);
            descriptionView = view.findViewById(R.id.card_description);
        }
    }


}
