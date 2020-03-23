package com.jordanmadrigal.foodrecipes.viewmodels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.jordanmadrigal.foodrecipes.models.Recipe;
import com.jordanmadrigal.foodrecipes.repositories.RecipeRepository;

public class SingleRecipeViewModel extends ViewModel {

    private RecipeRepository recipeRepository;
    private String recipeId;
    private boolean retrievedRecipe;

    public SingleRecipeViewModel() {
        recipeRepository = RecipeRepository.getInstance();
        retrievedRecipe = false;
    }

    public LiveData<Recipe> getSingleRecipe(){
        return recipeRepository.getSingleRecipe();
    }

    public LiveData<Boolean> isRecipeResponseTimedOut(){return recipeRepository.isRecipeResponseTimedOut();}

    public void searchRecipeById(String recipeId){
        this.recipeId = recipeId;
        recipeRepository.searchRecipeById(recipeId);
    }

    public String getRecipeId() {
        return recipeId;
    }

    public boolean hasRetrievedRecipe() {
        return retrievedRecipe;
    }

    public void setRetrievedRecipe(boolean retrievedRecipe) {
        this.retrievedRecipe = retrievedRecipe;
    }

}
