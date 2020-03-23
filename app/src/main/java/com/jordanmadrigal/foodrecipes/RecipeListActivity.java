package com.jordanmadrigal.foodrecipes;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;


import com.jordanmadrigal.foodrecipes.adapters.OnRecipeListener;
import com.jordanmadrigal.foodrecipes.adapters.RecipeRecyclerAdapter;
import com.jordanmadrigal.foodrecipes.models.Recipe;
import com.jordanmadrigal.foodrecipes.requests.RecipeApi;
import com.jordanmadrigal.foodrecipes.requests.ServiceGenerator;
import com.jordanmadrigal.foodrecipes.requests.responses.RecipeSearchResponse;
import com.jordanmadrigal.foodrecipes.utils.Constants;
import com.jordanmadrigal.foodrecipes.utils.VerticalSpacingItemDecorator;
import com.jordanmadrigal.foodrecipes.viewmodels.RecipeListViewModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.jordanmadrigal.foodrecipes.utils.Constants.RECIPE_KEY;

public class RecipeListActivity extends BaseActivity implements OnRecipeListener {

    private static final String TAG = "RecipeListActivity";
    private RecipeListViewModel recipeListViewModel;
    private RecyclerView recyclerView;
    private RecipeRecyclerAdapter adapter;
    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_list);
        recipeListViewModel = ViewModelProviders.of(this).get(RecipeListViewModel.class);

        recyclerView = findViewById(R.id.recipe_list);
        searchView = findViewById(R.id.search_view);

        initRecyclerView();
        initSearchView();
        subscribeObservers();

        if(!recipeListViewModel.isViewingRecipes()){
            displaySearchCategories();
        }
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
    }

    private void subscribeObservers(){
        recipeListViewModel.getRecipes().observe(this, new Observer<List<Recipe>>() {
            @Override
            public void onChanged(@Nullable List<Recipe> recipes) {
                if (recipes != null) {
                    if(recipeListViewModel.isViewingRecipes()) {
                        recipeListViewModel.setIsPerformingQuery(false);
                        adapter.setRecipes(recipes);
                    }
                }
            }
        });
    }

    private void initRecyclerView(){ ;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new RecipeRecyclerAdapter(this);
        recyclerView.setAdapter(adapter);
        VerticalSpacingItemDecorator itemDecorator = new VerticalSpacingItemDecorator(30);
        recyclerView.addItemDecoration(itemDecorator);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView rv, int newState) {
                if(!recyclerView.canScrollVertically(1)){
                    recipeListViewModel.searchNextPage();
                }
            }
        });
    }

    private void initSearchView (){
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                adapter.displayLoading();
                searchRecipesApi(query, 1);
                searchView.clearFocus();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    public void searchRecipesApi(String query, int pageNumber){
        recipeListViewModel.searchRecipesApi(query, pageNumber);
    }

    @Override
    public void onRecipeClick(int position) {
        Intent intent = new Intent(this, RecipeActivity.class);
        intent.putExtra(RECIPE_KEY, adapter.getSelectedRecipe(position));
        startActivity(intent);
    }

    @Override
    public void onCategoryClick(String category) {
        adapter.displayLoading();
        searchRecipesApi(category, 1);
        searchView.clearFocus();
    }

    private void displaySearchCategories(){
        recipeListViewModel.setViewingRecipes(false);
        adapter.displaySearchCategories();
    }

    @Override
    public void onBackPressed() {
        if(recipeListViewModel.onBackPressed()) {
            super.onBackPressed();
        }else{
            displaySearchCategories();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == R.id.action_categories){
            displaySearchCategories();
        }

        return super.onOptionsItemSelected(item);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.recipe_search_menu, menu);
        return true;

    }

}
