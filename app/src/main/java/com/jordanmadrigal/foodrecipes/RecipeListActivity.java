package com.jordanmadrigal.foodrecipes;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.jordanmadrigal.foodrecipes.models.Recipe;
import com.jordanmadrigal.foodrecipes.requests.RecipeApi;
import com.jordanmadrigal.foodrecipes.requests.ServiceGenerator;
import com.jordanmadrigal.foodrecipes.requests.responses.RecipeSearchResponse;
import com.jordanmadrigal.foodrecipes.utils.Constants;
import com.jordanmadrigal.foodrecipes.viewmodels.RecipeListViewModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecipeListActivity extends BaseActivity {

    private static final String TAG = "RecipeListActivity";
    private RecipeListViewModel recipeListViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_list);

        recipeListViewModel = ViewModelProviders.of(this).get(RecipeListViewModel.class);

        subscribeObservers();

        findViewById(R.id.test).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                testRetrofitRequest();
            }
        });
    }

    private void subscribeObservers(){
        recipeListViewModel.getRecipes().observe(this, new Observer<List<Recipe>>() {
            @Override
            public void onChanged(@Nullable List<Recipe> recipes) {
                if (recipes != null) {
                    for(Recipe recipe: recipes){
                        Log.d(TAG, "Title: " + recipe.getTitle());
                    }
                }
            }
        });
    }

    private void testRetrofitRequest(){
        searchRecipesApi("chicken breast", 1);
    }

    public void searchRecipesApi(String query, int pageNumber){
        recipeListViewModel.searchRecipesApi(query, pageNumber);
    }
}
