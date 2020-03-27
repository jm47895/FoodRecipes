package com.jordanmadrigal.foodrecipes.repositories;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.support.annotation.Nullable;

import com.jordanmadrigal.foodrecipes.models.Recipe;
import com.jordanmadrigal.foodrecipes.requests.responses.RecipeApiClient;

import java.util.List;

public class RecipeRepository {

    private static RecipeRepository instance;
    private RecipeApiClient recipeApiClient;
    private String query;
    private int pageNumber;
    private MutableLiveData<Boolean> isQueryExhausted = new MutableLiveData<>();
    private MediatorLiveData<List<Recipe>> recipes = new MediatorLiveData<>();

    public static RecipeRepository getInstance(){
        if(instance == null){
            instance = new RecipeRepository();
        }
        return instance;
    }

    private RecipeRepository() {
        recipeApiClient = RecipeApiClient.getInstance();
        initMediators();
    }

    public LiveData<List<Recipe>> getRecipes(){
        return recipes;
    }

    private void initMediators(){
        LiveData<List<Recipe>> recipeListApiSource = recipeApiClient.getRecipes();
        recipes.addSource(recipeListApiSource, new Observer<List<Recipe>>() {
            @Override
            public void onChanged(@Nullable List<Recipe> recipeList) {
                if(recipeList != null){
                    recipes.setValue(recipeList);
                    doneQuery(recipeList);
                }else{
                    //search database cache
                    doneQuery(null);
                }
            }
        });
    }

    private void doneQuery(List<Recipe> list){
        if(list != null){
            if(list.size() % 30 != 0){
                isQueryExhausted.setValue(true);
            }
        }else{
            isQueryExhausted.setValue(true);
        }
    }

    public LiveData<Boolean> hasQueryExhausted(){
        return isQueryExhausted;
    }

    public LiveData<Recipe> getSingleRecipe(){
        return recipeApiClient.getSingleRecipe();
    }

    public LiveData<Boolean> isRecipeResponseTimedOut(){return recipeApiClient.isRecipeResponseTimedOut();}

    public void searchRecipesApi(String query, int pageNumber){
        if(pageNumber == 0){
            pageNumber = 1;
        }
        this.query = query;
        this.pageNumber = pageNumber;
        isQueryExhausted.setValue(false);
        recipeApiClient.searchRecipesApi(query, pageNumber);
    }

    public void searchRecipeById(String recipeId){
        recipeApiClient.searchRecipeById(recipeId);
    }

    public void searchNextPage(){
        searchRecipesApi(query, pageNumber + 1);
    }

    public void cancelRequest(){
        recipeApiClient.cancelRequest();
    }

}
