package com.jordanmadrigal.foodrecipes.viewmodels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.jordanmadrigal.foodrecipes.models.Recipe;
import com.jordanmadrigal.foodrecipes.repositories.RecipeRepository;

import java.util.List;

public class RecipeListViewModel extends ViewModel {

    private RecipeRepository recipeRepository;

    public RecipeListViewModel() {
        recipeRepository = RecipeRepository.getInstance();
    }

    public LiveData<List<Recipe>> getRecipes(){
        return recipeRepository.getRecipes();
    }
}
