package com.jordanmadrigal.foodrecipes;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.jordanmadrigal.foodrecipes.models.Recipe;

public class RecipeActivity extends BaseActivity {

    public static final String RECIPE_KEY = "recipe";
    private ImageView recipeImage;
    private TextView recipeTitle, recipeRank;
    private LinearLayout recipeIngredientsContainer;
    private ScrollView scrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        recipeImage = findViewById(R.id.recipe_image);
        recipeTitle = findViewById(R.id.recipe_title);
        recipeRank = findViewById(R.id.recipe_social_score);
        recipeIngredientsContainer = findViewById(R.id.ingredients_container);
        scrollView = findViewById(R.id.parent);

        getIncomingIntent();
    }

    private void getIncomingIntent(){
        if(getIntent().hasExtra(RECIPE_KEY)){
            Recipe recipe = getIntent().getParcelableExtra(RECIPE_KEY);
        }
    }
}
