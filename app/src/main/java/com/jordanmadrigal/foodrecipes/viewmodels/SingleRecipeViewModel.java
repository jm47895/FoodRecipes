package com.jordanmadrigal.foodrecipes.viewmodels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.jordanmadrigal.foodrecipes.models.Recipe;
import com.jordanmadrigal.foodrecipes.repositories.RecipeRepository;

public class SingleRecipeViewModel extends ViewModel {

    private RecipeRepository recipeRepository;

    public SingleRecipeViewModel() {
        recipeRepository = RecipeRepository.getInstance();
    }

    public LiveData<Recipe> getSingleRecipe(){
        return recipeRepository.getSingleRecipe();
    }

    public void searchRecipeById(String recipeId){
        recipeRepository.searchRecipeById(recipeId);
    }
}
