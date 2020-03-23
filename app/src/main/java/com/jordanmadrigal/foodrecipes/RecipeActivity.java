package com.jordanmadrigal.foodrecipes;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.jordanmadrigal.foodrecipes.models.Recipe;
import com.jordanmadrigal.foodrecipes.viewmodels.SingleRecipeViewModel;

import static com.jordanmadrigal.foodrecipes.utils.Constants.RECIPE_KEY;

public class RecipeActivity extends BaseActivity {

    private static final String TAG = "RecipeActivity";

    private ImageView recipeImage;
    private TextView recipeTitle, recipeRank;
    private LinearLayout recipeIngredientsContainer;
    private ScrollView scrollView;
    private SingleRecipeViewModel singleRecipeViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        recipeImage = findViewById(R.id.recipe_image);
        recipeTitle = findViewById(R.id.recipe_title);
        recipeRank = findViewById(R.id.recipe_social_score);
        recipeIngredientsContainer = findViewById(R.id.ingredients_container);
        scrollView = findViewById(R.id.parent);
        singleRecipeViewModel = ViewModelProviders.of(this).get(SingleRecipeViewModel.class);

        subscribeObservers();
        getIncomingIntent();
    }

    private void subscribeObservers(){
        singleRecipeViewModel.getSingleRecipe().observe(this, new Observer<Recipe>() {
            @Override
            public void onChanged(@Nullable Recipe recipe) {
                if(recipe != null){
                    Log.d(TAG, "-------------------------------");
                    Log.d(TAG, "Onchanged: " + recipe.getTitle());
                    for(String ingredient : recipe.getIngredients()){
                        Log.d(TAG, "Ingredient: " + ingredient);
                    }
                }
            }
        });
    }

    private void getIncomingIntent(){
        if(getIntent().hasExtra(RECIPE_KEY)){
            Recipe recipe = getIntent().getParcelableExtra(RECIPE_KEY);
            singleRecipeViewModel.searchRecipeById(recipe.getRecipe_id());
        }
    }
}
