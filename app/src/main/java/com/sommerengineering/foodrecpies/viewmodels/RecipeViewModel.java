package com.sommerengineering.foodrecpies.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.sommerengineering.foodrecpies.models.Recipe;
import com.sommerengineering.foodrecpies.repositories.RecipeRepository;

public class RecipeViewModel extends ViewModel {

    private RecipeRepository recipeRepository;
    public RecipeViewModel() {
        this.recipeRepository = RecipeRepository.getInstance();
        isRecipeRetrieved = false;
    }

    public LiveData<Recipe> getRecipe() {
        return recipeRepository.getRecipe();
    }

    String id;
    public void searchRecipeById(String id) {
        this.id = id;
        recipeRepository.searchRecipeById(id);
    }
    public String getId() {
        return id;
    }

    public LiveData<Boolean> isRecipeTimedOut() {
        return recipeRepository.isRecipeTimedOut();
    }

    private boolean isRecipeRetrieved;
    public boolean getIsRecipeRetrieved() {
        return isRecipeRetrieved;
    }
    public void setIsRecipeRetrieved(boolean isRecipeRetrieved) {
        this.isRecipeRetrieved = isRecipeRetrieved;
    }
}
