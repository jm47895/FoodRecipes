package com.jordanmadrigal.foodrecipes.repositories;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import com.jordanmadrigal.foodrecipes.models.Recipe;
import com.jordanmadrigal.foodrecipes.requests.responses.RecipeApiClient;

import java.util.List;

public class RecipeRepository {

    private static RecipeRepository instance;
    private RecipeApiClient recipeApiClient;

    public static RecipeRepository getInstance(){
        if(instance == null){
            instance = new RecipeRepository();
        }
        return instance;
    }

    private RecipeRepository() {
        recipeApiClient = RecipeApiClient.getInstance();
    }

    public LiveData<List<Recipe>> getRecipes(){
        return recipeApiClient.getRecipes();
    }

}
