package com.jordanmadrigal.foodrecipes;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
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
        setContentView(R.layout.activity_recipe_item);

        recipeImage = findViewById(R.id.recipe_image);
        recipeTitle = findViewById(R.id.recipe_title);
        recipeRank = findViewById(R.id.recipe_social_score);
        recipeIngredientsContainer = findViewById(R.id.ingredients_container);
        scrollView = findViewById(R.id.parent);
        singleRecipeViewModel = ViewModelProviders.of(this).get(SingleRecipeViewModel.class);

        showProgressBar(true);
        subscribeObservers();
        getIncomingIntent();
    }

    private void subscribeObservers(){
        singleRecipeViewModel.getSingleRecipe().observe(this, new Observer<Recipe>() {
            @Override
            public void onChanged(@Nullable Recipe recipe) {
                if(recipe != null){
                    if(recipe.getRecipe_id().equals(singleRecipeViewModel.getRecipeId())) {
                        setSingleRecipeProperties(recipe);
                        singleRecipeViewModel.setRetrievedRecipe(true);
                    }
                }
            }
        });

        singleRecipeViewModel.isRecipeResponseTimedOut().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean hasTimedOut) {
                if(hasTimedOut && !singleRecipeViewModel.hasRetrievedRecipe()){
                    Log.d(TAG, "---------------------Timed Out");
                }
            }
        });
    }

    private void setSingleRecipeProperties(Recipe singleRecipe){
        if(singleRecipe != null) {
            RequestOptions requestOptions = new RequestOptions().placeholder(R.drawable.ic_launcher_background);

            Glide.with(this)
                    .setDefaultRequestOptions(requestOptions)
                    .load(singleRecipe.getImage_url())
                    .into(recipeImage);

            recipeTitle.setText(singleRecipe.getTitle());
            recipeRank.setText(String.valueOf(Math.round(singleRecipe.getSocial_rank())));

            recipeIngredientsContainer.removeAllViews();
            for (String ingredient : singleRecipe.getIngredients()) {
                TextView ingredientTextView = new TextView(this);
                ingredientTextView.setText(ingredient);
                ingredientTextView.setTextSize(15);
                ingredientTextView.setLayoutParams(
                        new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                );
                recipeIngredientsContainer.addView(ingredientTextView);
            }

            showParent();
            showProgressBar(false);
        }
    }

    private void showParent(){
        scrollView.setVisibility(View.VISIBLE);
    }

    private void getIncomingIntent(){
        if(getIntent().hasExtra(RECIPE_KEY)){
            Recipe recipe = getIntent().getParcelableExtra(RECIPE_KEY);
            singleRecipeViewModel.searchRecipeById(recipe.getRecipe_id());
        }
    }
}
