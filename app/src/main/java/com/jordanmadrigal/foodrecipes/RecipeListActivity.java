package com.jordanmadrigal.foodrecipes;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.View;
import android.widget.Button;


import com.jordanmadrigal.foodrecipes.adapters.OnRecipeListener;
import com.jordanmadrigal.foodrecipes.adapters.RecipeRecyclerAdapter;
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

        recyclerView = findViewById(R.id.recipe_list);
        searchView = findViewById(R.id.search_view);

        initRecyclerView();

        initSearchView();

        recipeListViewModel = ViewModelProviders.of(this).get(RecipeListViewModel.class);

        subscribeObservers();

    }

    private void subscribeObservers(){
        recipeListViewModel.getRecipes().observe(this, new Observer<List<Recipe>>() {
            @Override
            public void onChanged(@Nullable List<Recipe> recipes) {
                if (recipes != null) {
                    adapter.setRecipes(recipes);
                }
            }
        });
    }

    private void initRecyclerView(){ ;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new RecipeRecyclerAdapter(this);
        recyclerView.setAdapter(adapter);
    }

    private void initSearchView (){
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchRecipesApi(query, 1);
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

    }

    @Override
    public void onCategoryClick(String category) {

    }
}
