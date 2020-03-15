package com.jordanmadrigal.foodrecipes;

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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecipeListActivity extends BaseActivity {

    private static final String TAG = "RecipeListActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_list);
        
        Button button = findViewById(R.id.test);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                testRetrofitRequest();
                /*if(progressBar.getVisibility() == View.VISIBLE){
                    showProgressBar(false);
                }else{
                    showProgressBar(true);
                }*/
            }
        });
    }

    private void testRetrofitRequest(){
        RecipeApi recipeApi = ServiceGenerator.getRecipeApi();

        Call<RecipeSearchResponse> recipeSearchResponseCall = recipeApi
                .searchRecipe(Constants.API_KEY, "chicken", "1");

        recipeSearchResponseCall.enqueue(new Callback<RecipeSearchResponse>() {
            @Override
            public void onResponse(Call<RecipeSearchResponse> call, Response<RecipeSearchResponse> response) {
                Log.d(TAG, "Webserver response: " + response.toString());
                if(response.code() == 200){
                    Log.d(TAG, "JSON response: " + response.body().toString());
                    List<Recipe> recipes = new ArrayList<>(response.body().getRecipes());
                    for(Recipe recipe : recipes){
                        Log.d(TAG, "JSON response: " + recipe.getTitle());
                    }
                }
            }

            @Override
            public void onFailure(Call<RecipeSearchResponse> call, Throwable t) {
                Log.d(TAG, t.getMessage());
            }
        });
    }
}
