package com.ftn.eventhopper.adapters.solutions;

import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ftn.eventhopper.R;
import com.ftn.eventhopper.shared.dtos.solutions.SimpleProductDTO;
import com.github.islamkhsh.CardSliderAdapter;

import java.util.ArrayList;

public class TopSolutionAdapter extends CardSliderAdapter<TopSolutionAdapter.SolutionViewHolder>
{

    private ArrayList<SimpleProductDTO> solutions;

    public TopSolutionAdapter(ArrayList<SimpleProductDTO> solutions){
        this.solutions = solutions;
    }

    @Override
    public int getItemCount(){
        return solutions.size();
    }

    @NonNull
    @Override
    public TopSolutionAdapter.SolutionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_list_item, parent, false);
        view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        return new TopSolutionAdapter.SolutionViewHolder(view);
    }
    public void onBindViewHolder(int position, TopSolutionAdapter.SolutionViewHolder holder) {
        SimpleProductDTO event = solutions.get(position);


        ImageView imageView = holder.itemView.findViewById(R.id.card_image);
        TextView titleView = holder.itemView.findViewById(R.id.card_title);
        TextView secondayView = holder.itemView.findViewById(R.id.card_secondary);
        TextView descriptionView = holder.itemView.findViewById(R.id.card_description);

    }


    @Override
    public void bindVH(TopSolutionAdapter.SolutionViewHolder holder, int position) {
        SimpleProductDTO solution = solutions.get(position);

        ImageView imageView = holder.itemView.findViewById(R.id.card_image);
        TextView titleView = holder.itemView.findViewById(R.id.card_title);
        TextView secondaryView = holder.itemView.findViewById(R.id.card_secondary);
        TextView descriptionView = holder.itemView.findViewById(R.id.card_description);

        titleView.setText(solution.getName());
        secondaryView.setText(solution.getCategory().getName() );
        imageView.setImageDrawable(Drawable.createFromPath(solutions.get(position).getPictures().get(0)));

        String imageUrl = solutions.get(position).getPictures().get(0);
        Glide.with(holder.imageView.getContext())
                .load(imageUrl)
//                .placeholder(R.drawable.placeholder_image)
//                .error(R.drawable.error_image)
                .into(holder.imageView);
        descriptionView.setText(solution.getDescription());
    }


    public static class SolutionViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView titleView;
        TextView secondaryView;
        TextView descriptionView;

        public SolutionViewHolder(View view) {
            super(view);
            imageView = view.findViewById(R.id.card_image);
            titleView = view.findViewById(R.id.card_title);
            secondaryView = view.findViewById(R.id.card_secondary);
            descriptionView = view.findViewById(R.id.card_description);
        }
    }

}
