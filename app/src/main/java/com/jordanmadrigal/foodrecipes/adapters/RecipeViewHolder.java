package com.jordanmadrigal.foodrecipes.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jordanmadrigal.foodrecipes.R;

public class RecipeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView title, publisher, socialScore;
    public ImageView image;
    public OnRecipeListener onRecipeListener;

    public RecipeViewHolder(@NonNull View itemView, OnRecipeListener onRecipeListener) {
        super(itemView);
        title = itemView.findViewById(R.id.recipe_title);
        publisher = itemView.findViewById(R.id.recipe_publisher);
        socialScore = itemView.findViewById(R.id.recipe_social_score);
        image = itemView.findViewById(R.id.recipe_image);
        this.onRecipeListener = onRecipeListener;

        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        onRecipeListener.onRecipeClick(getAdapterPosition());
    }
}
