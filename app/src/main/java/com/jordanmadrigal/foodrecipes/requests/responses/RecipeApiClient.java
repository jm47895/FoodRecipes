package com.jordanmadrigal.foodrecipes.requests.responses;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.util.Log;

import com.jordanmadrigal.foodrecipes.AppExecutors;
import com.jordanmadrigal.foodrecipes.models.Recipe;
import com.jordanmadrigal.foodrecipes.requests.ServiceGenerator;
import com.jordanmadrigal.foodrecipes.utils.Constants;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Response;

import static com.jordanmadrigal.foodrecipes.utils.Constants.NETWORK_TIMEOUT;

public class RecipeApiClient {

    private static final String TAG = "RecipeApiClient";
    private static RecipeApiClient instance;
    private MutableLiveData<List<Recipe>> recipes;
    private RetrieveRecipesRunnable retrieveRecipesRunnable;
    private RetrieveRecipeRunnable retrieveRecipeRunnable;
    private MutableLiveData<Recipe> singleRecipe;
    private MutableLiveData<Boolean> recipeRequestTimeout;

    public static RecipeApiClient getInstance(){
        if(instance == null){
            instance = new RecipeApiClient();
        }

        return instance;
    }

    private RecipeApiClient() {
        recipes = new MutableLiveData<>();
        singleRecipe = new MutableLiveData<>();
        recipeRequestTimeout = new MutableLiveData<>();
    }

    public LiveData<List<Recipe>> getRecipes(){
        return recipes;
    }

    public LiveData<Recipe> getSingleRecipe(){
        return singleRecipe;
    }

    public LiveData<Boolean> isRecipeResponseTimedOut(){return recipeRequestTimeout;}

    public void searchRecipesApi(String query, int pageNumber){
        if(retrieveRecipesRunnable != null){
            retrieveRecipesRunnable = null;
        }

        retrieveRecipesRunnable = new RetrieveRecipesRunnable(query, pageNumber);
        final Future handler = AppExecutors.getInstance().getNetworkIO().submit(retrieveRecipesRunnable);

        AppExecutors.getInstance().getNetworkIO().schedule(new Runnable() {
            @Override
            public void run() {
                handler.cancel(true);
            }
        }, NETWORK_TIMEOUT, TimeUnit.MILLISECONDS);
    }

    public void searchRecipeById(String recipeId){
        if(retrieveRecipeRunnable != null){
            retrieveRecipeRunnable = null;
        }

        retrieveRecipeRunnable = new RetrieveRecipeRunnable(recipeId);

        final Future handler = AppExecutors.getInstance().getNetworkIO().submit(retrieveRecipeRunnable);

        recipeRequestTimeout.setValue(false);
        AppExecutors.getInstance().getNetworkIO().schedule(new Runnable() {
            @Override
            public void run() {
                recipeRequestTimeout.postValue(true);
                handler.cancel(true);
            }
        }, NETWORK_TIMEOUT, TimeUnit.MILLISECONDS);
    }

    private class RetrieveRecipesRunnable implements Runnable{

        private String query;
        private int pageNumber;
        private boolean cancelRequest;

        private RetrieveRecipesRunnable(String query, int pageNumber) {
            this.query = query;
            this.pageNumber = pageNumber;
            cancelRequest = false;
        }

        @Override
        public void run() {
            try {
                Response response = getRecipes(query, pageNumber).execute();
                if(cancelRequest){
                    return;
                }

                if(response.code() == 200){
                    List<Recipe> list = new ArrayList<>(((RecipeSearchResponse)response.body()).getRecipes());
                    if(pageNumber == 1){
                        recipes.postValue(list);
                    }else{
                        List<Recipe> currentRecipes = recipes.getValue();
                        currentRecipes.addAll(list);
                        recipes.postValue(currentRecipes);
                    }
                }else{
                    String error = response.errorBody().string();
                    Log.e(TAG, "Response Error: " + error);
                    recipes.postValue(null);
                }
            } catch (IOException e) {
                e.printStackTrace();
                recipes.postValue(null);
            }
        }

        private Call<RecipeSearchResponse> getRecipes(String query, int pageNumber){
            return ServiceGenerator.getRecipeApi().searchRecipe(Constants.API_KEY, query, String.valueOf(pageNumber));
        }

        private void cancelRequest(){
            Log.d(TAG, "Cancelling search query");
            cancelRequest = true;
        }
    }

    private class RetrieveRecipeRunnable implements Runnable{

        private String recipeId;
        private boolean cancelRequest;

        private RetrieveRecipeRunnable(String recipeId) {
            this.recipeId = recipeId;
            cancelRequest = false;
        }

        @Override
        public void run() {
            try {
                Response response = getRecipe(recipeId).execute();
                if(cancelRequest){
                    return;
                }

                if(response.code() == 200){
                    Recipe recipe = ((RecipeResponse)response.body()).getRecipe();
                    singleRecipe.postValue(recipe);
                }else{
                    String error = response.errorBody().string();
                    Log.e(TAG, "Response Error: " + error);
                    singleRecipe.postValue(null);
                }
            } catch (IOException e) {
                e.printStackTrace();
                singleRecipe.postValue(null);
            }
        }

        private Call<RecipeResponse> getRecipe(String recipeId){
            return ServiceGenerator.getRecipeApi().getRecipe(Constants.API_KEY, recipeId);
        }

        private void cancelRequest(){
            Log.d(TAG, "Cancelling search query");
            cancelRequest = true;
        }
    }

    public void cancelRequest(){
        if(retrieveRecipesRunnable != null) {
            retrieveRecipesRunnable.cancelRequest();
        }
        if(retrieveRecipeRunnable != null){
            retrieveRecipeRunnable.cancelRequest();
        }
    }
}
