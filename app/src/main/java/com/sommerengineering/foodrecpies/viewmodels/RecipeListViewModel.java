package com.sommerengineering.foodrecpies.viewmodels;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.sommerengineering.foodrecpies.models.Recipe;
import com.sommerengineering.foodrecpies.repositories.RecipeRepository;

import java.util.List;

public class RecipeListViewModel extends ViewModel {

    private RecipeRepository recipeRepository;
    private boolean isViewingRecipes;

    public RecipeListViewModel() {
        recipeRepository = RecipeRepository.getInstance();
        isViewingRecipes = false;
    }

    public LiveData<List<Recipe>> getRecipes() {

        // call repo
        return recipeRepository.getRecipes();
    }

    public void searchRecipesApi(String query, int page) {

        // call repo
        recipeRepository.searchRecipesApi(query, page);
        isViewingRecipes = true;
    }

    public boolean isViewingRecipes() {
        return isViewingRecipes;
    }

    public void setIsViewingRecipes(boolean isViewingRecipes) {
        this.isViewingRecipes = isViewingRecipes;
    }
}
