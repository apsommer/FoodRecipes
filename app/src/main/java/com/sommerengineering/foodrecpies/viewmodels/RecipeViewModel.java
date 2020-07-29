package com.sommerengineering.foodrecpies.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.sommerengineering.foodrecpies.models.Recipe;
import com.sommerengineering.foodrecpies.repositories.RecipeRepository;

public class RecipeViewModel extends ViewModel {

    private RecipeRepository recipeRepository;
    public RecipeViewModel() {
        this.recipeRepository = RecipeRepository.getInstance();
    }

    public LiveData<Recipe> getRecipe() {
        return recipeRepository.getRecipe();
    }

    public void searchRecipeById(String id) {
        recipeRepository.searchRecipeById(id);
    }
}
