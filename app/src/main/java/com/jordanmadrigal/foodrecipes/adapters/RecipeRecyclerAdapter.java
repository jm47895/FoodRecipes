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

import java.util.ArrayList;
import java.util.List;

public class RecipeRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private static final int RECIPE_TYPE = 1;
    private static final int LOADING_TYPE = 2;
    public static final String LOADING_KEY = "LOADING";
    private List<Recipe> recipes;
    private OnRecipeListener onRecipeListener;

    public RecipeRecyclerAdapter(OnRecipeListener onRecipeListener) {
        this.onRecipeListener = onRecipeListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = null;

        switch (i){
            case RECIPE_TYPE:
                view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_recipe_list_item, viewGroup, false);
                return new RecipeViewHolder(view, onRecipeListener);
            case LOADING_TYPE:
                view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_loading_list_item, viewGroup, false);
                return new LoadingViewHolder(view);
            default:
                view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_recipe_list_item, viewGroup, false);
                return new RecipeViewHolder(view, onRecipeListener);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {

        if(getItemViewType(position) == RECIPE_TYPE){
            RequestOptions requestOptions = new RequestOptions().placeholder(R.drawable.ic_launcher_background);

            Glide.with(viewHolder.itemView.getContext())
                    .setDefaultRequestOptions(requestOptions)
                    .load(recipes.get(position).getImage_url())
                    .into(((RecipeViewHolder)viewHolder).image);
            ((RecipeViewHolder)viewHolder).title.setText(recipes.get(position).getTitle());
            ((RecipeViewHolder)viewHolder).publisher.setText(recipes.get(position).getPublisher());
            ((RecipeViewHolder)viewHolder).socialScore.setText(String.valueOf(Math.round(recipes.get(position).getSocial_rank())));
        }
    }

    @Override
    public int getItemViewType(int position) {
        if(recipes.get(position).getTitle().equals(LOADING_KEY)){
            return LOADING_TYPE;
        }else{
            return RECIPE_TYPE;
        }
    }

    public void displayLoading (){
        if(!isLoading()){
            Recipe recipe = new Recipe();
            recipe.setTitle(LOADING_KEY);
            List<Recipe> loadingList = new ArrayList<>();
            loadingList.add(recipe);
            recipes = loadingList;
            notifyDataSetChanged();
        }
    }

    private boolean isLoading(){
        if(recipes != null){
            return recipes.size() > 0 && recipes.get(recipes.size() - 1).getTitle().equals(LOADING_KEY);
        }
        return false;
    }

    @Override
    public int getItemCount() {
        if(recipes != null){
            return recipes.size();
        }
        return 0;
    }

    public void setRecipes(List<Recipe> recipes){
        this.recipes = recipes;
        notifyDataSetChanged();
    }
}
