package com.sommerengineering.foodrecpies.reqeusts;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.sommerengineering.foodrecpies.models.Recipe;

import java.util.List;

/**
 * Singleton pattern.
 */
public class RecipeApiClient {

    private static RecipeApiClient instance;
    private MutableLiveData<List<Recipe>> recipes;

    public static RecipeApiClient getInstance() {
        if (instance == null) {
            instance = new RecipeApiClient();
        }
        return instance;
    }

    private RecipeApiClient() {
        recipes = new MutableLiveData<>();
    }

    public LiveData<List<Recipe>> getRecipes() {
        return recipes;
    }
}
