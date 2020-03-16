package com.jordanmadrigal.foodrecipes.requests.responses;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import com.jordanmadrigal.foodrecipes.models.Recipe;

import java.util.List;

public class RecipeApiClient {

    private static RecipeApiClient instance;
    private MutableLiveData<List<Recipe>> recipes;

    public static RecipeApiClient getInstance(){
        if(instance == null){
            instance = new RecipeApiClient();
        }

        return instance;
    }

    private RecipeApiClient() {
        recipes = new MutableLiveData<>();
    }

    public LiveData<List<Recipe>> getRecipes(){
        return recipes;
    }

}
