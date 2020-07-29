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

    // singleton instance
    private static RecipeRepository instance;
    public static RecipeRepository getInstance() {

        if (instance == null) {
            instance = new RecipeRepository();
        }
        return instance;
    }

    // get api client
    private static RecipeApiClient client;
    private RecipeRepository() {
        client = RecipeApiClient.getInstance();
    }

    public LiveData<List<Recipe>> getRecipes() {

        // todo logic call local db cache, if desired

        // call remote source
        return client.getRecipes();
    }

    private String query;
    private int page;
    public void searchRecipesApi(String query, int page) {

        // ensure valid page number
        if (page == 0) page = 1;

        // save these members to automatically handle pagination request
        this.query = query;
        this.page = page;

        // call into retrofit client to actually make the query
        client.searchRecipesApi(query, page);
    }

    public void searchNextPage() {
        searchRecipesApi(query, page + 1);
    }

    public void cancelRequest() {
        client.cancelRequest();
    }
}
