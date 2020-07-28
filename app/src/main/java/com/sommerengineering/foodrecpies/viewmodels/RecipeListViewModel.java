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

    public RecipeListViewModel() {
        recipeRepository = RecipeRepository.getInstance();
    }

    public LiveData<List<Recipe>> getRecipes() {

        // call repo
        return recipeRepository.getRecipes();
    }

    public void searchRecipesApi(String query, int page) {

        // call repo
        recipeRepository.searchRecipesApi(query, page);
    }
}
