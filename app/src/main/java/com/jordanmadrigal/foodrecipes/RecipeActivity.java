package com.jordanmadrigal.foodrecipes;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.jordanmadrigal.foodrecipes.models.Recipe;

import static com.jordanmadrigal.foodrecipes.utils.Constants.RECIPE_KEY;

public class RecipeActivity extends BaseActivity {

    private static final String TAG = "RecipeActivity";

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

            Log.d(TAG, "Parsable: " + recipe.getTitle());
        }
    }
}
