package com.jordanmadrigal.foodrecipes.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.jordanmadrigal.foodrecipes.R;
import com.jordanmadrigal.foodrecipes.models.Recipe;

import java.util.List;

public class RecipeRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private List<Recipe> recipes;
    private OnRecipeListener onRecipeListener;

    public RecipeRecyclerAdapter(List<Recipe> recipes, OnRecipeListener onRecipeListener) {
        this.recipes = recipes;
        this.onRecipeListener = onRecipeListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_recipe_list_item, viewGroup, false);
        return new RecipeViewHolder(view, onRecipeListener);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {

        RequestOptions requestOptions = new RequestOptions().placeholder(R.drawable.ic_launcher_background);

        Glide.with(viewHolder.itemView.getContext())
                .setDefaultRequestOptions(requestOptions)
                .load(recipes.get(i))
                .into(((RecipeViewHolder)viewHolder).image);
        ((RecipeViewHolder)viewHolder).title.setText(recipes.get(i).getTitle());
        ((RecipeViewHolder)viewHolder).publisher.setText(recipes.get(i).getPublisher());
        ((RecipeViewHolder)viewHolder).socialScore.setText(String.valueOf(recipes.get(i).getSocial_rank()));
    }

    @Override
    public int getItemCount() {
        return recipes.size();
    }

    public void setRecipes(List<Recipe> recipes){
        this.recipes = recipes;
        notifyDataSetChanged();
    }
}
