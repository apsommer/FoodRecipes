package com.sommerengineering.foodrecpies.repositories;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.sommerengineering.foodrecpies.models.Recipe;
import com.sommerengineering.foodrecpies.reqeusts.RecipeApi;
import com.sommerengineering.foodrecpies.reqeusts.RecipeApiClient;

import java.util.List;

/**
 * Singleton pattern.
 */
public class RecipeRepository {

    private static RecipeRepository instance;
    private static RecipeApiClient client;

    // singleton instance
    public static RecipeRepository getInstance() {

        if (instance == null) {
            instance = new RecipeRepository();
        }
        return instance;
    }

    private RecipeRepository() {
        client = RecipeApiClient.getInstance();
    }

    public LiveData<List<Recipe>> getRecipes() {

        // call local cache or remote source
        return client.getRecipes();
    }

    public void searchRecipesApi(String query, int page) {

        // ensure valid page number
        if (page == 0) page = 1;
        client.searchRecipesApi(query, page);
    }
}
