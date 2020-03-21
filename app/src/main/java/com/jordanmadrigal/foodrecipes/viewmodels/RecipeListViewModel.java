package com.jordanmadrigal.foodrecipes.viewmodels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.jordanmadrigal.foodrecipes.models.Recipe;
import com.jordanmadrigal.foodrecipes.repositories.RecipeRepository;

import java.util.List;

public class RecipeListViewModel extends ViewModel {

    private RecipeRepository recipeRepository;
    private boolean isViewingRecipes;
    private boolean isPerformingQuery;

    public RecipeListViewModel() {
        recipeRepository = RecipeRepository.getInstance();
        isPerformingQuery = false;
    }

    public LiveData<List<Recipe>> getRecipes(){
        return recipeRepository.getRecipes();
    }

    public void searchRecipesApi(String query, int pageNumber){
        isViewingRecipes = true;
        isPerformingQuery = true;
        recipeRepository.searchRecipesApi(query, pageNumber);
    }

    public void searchNextPage(){
        if(!isPerformingQuery && isViewingRecipes) {
            recipeRepository.searchNextPage();
        }
    }

    public boolean isViewingRecipes() {
        return isViewingRecipes;
    }

    public void setViewingRecipes(boolean viewingRecipes) {
        isViewingRecipes = viewingRecipes;
    }

    public void setIsPerformingQuery(boolean performingQuery) {
        isPerformingQuery = performingQuery;
    }

    public boolean isPerformingQuery() {
        return isPerformingQuery;
    }

    public boolean onBackPressed(){
        if(isPerformingQuery){
            recipeRepository.cancelRequest();
        }

        if(isViewingRecipes){
            isViewingRecipes = false;
            return false;
        }
        return true;
    }

}
