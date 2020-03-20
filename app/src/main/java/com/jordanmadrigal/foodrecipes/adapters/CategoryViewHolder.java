package com.jordanmadrigal.foodrecipes.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.jordanmadrigal.foodrecipes.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class CategoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public CircleImageView categoryImage;
    public TextView categoryTitle;
    private OnRecipeListener categoryListener;

    public CategoryViewHolder(@NonNull View itemView, OnRecipeListener categoryListener) {
        super(itemView);

        this.categoryListener = categoryListener;
        categoryImage = itemView.findViewById(R.id.category_image);
        categoryTitle = itemView.findViewById(R.id.category_title);

        itemView.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        categoryListener.onCategoryClick(categoryTitle.getText().toString());
    }
}
