package com.jordanmadrigal.foodrecipes.adapters;

public interface OnRecipeListener {

    void onRecipeClick(int position);

    void onCategoryClick(String category);
}