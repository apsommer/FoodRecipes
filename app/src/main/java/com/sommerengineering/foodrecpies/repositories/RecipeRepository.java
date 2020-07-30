package com.sommerengineering.foodrecpies.repositories;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.sommerengineering.foodrecpies.models.Recipe;
import com.sommerengineering.foodrecpies.reqeusts.RecipeApi;
import com.sommerengineering.foodrecpies.reqeusts.RecipeApiClient;

import java.util.List;

/**
 * Singleton pattern.
 */
public class RecipeRepository {

    private static final String TAG = "RecipeRepository";

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
        initMediators();
    }

    public LiveData<Recipe> getRecipe() {
        return client.getRecipe();
    }

    public LiveData<Boolean> isRecipeTimedOut() {
        return client.isRecipeTimedOut();
    }

    // mediator is an intermediary in livedata emissions
    private MutableLiveData<Boolean> isQueryExhausted = new MutableLiveData<>();
    private MediatorLiveData<List<Recipe>> recipes = new MediatorLiveData<>();

    // return mediator livedata
    public LiveData<List<Recipe>> getRecipes() {
        return recipes;
    }

    // mediator pattern
    public void initMediators() {

        LiveData<List<Recipe>> recipeSource = client.getRecipes();
        recipes.addSource(recipeSource, new Observer<List<Recipe>>() {

            @Override
            public void onChanged(List<Recipe> recipeList) {

                Log.d(TAG, "onChanged: mediator");

                if (recipes == null) {
                    // todo search db cache
                    doneQuery(null);
                } else {
                    recipes.setValue(recipeList);
                    doneQuery(recipeList);
                }
            }
        });
    }

    public LiveData<Boolean> isQueryExhausted() {
        return isQueryExhausted;
    }

    // criteria for exhausted pagination
    public void doneQuery(List<Recipe> list) {

        if (list == null) isQueryExhausted.setValue(true);
        else if (list.size() % 30 != 0) isQueryExhausted.setValue(true);
    }

    private String query;
    private int page;
    public void searchRecipesApi(String query, int page) {

        // ensure valid page number
        if (page == 0) page = 1;

        // save these members to automatically handle pagination request
        this.query = query;
        this.page = page;

        isQueryExhausted.setValue(false);

        // call into retrofit client to actually make the query
        client.searchRecipesApi(query, page);
    }

    public void searchRecipeById(String id) {
        client.searchRecipeById(id);
    }

    public void searchNextPage() {
        searchRecipesApi(query, page + 1);
    }

    public void cancelRequest() {
        client.cancelRequest();
    }
}
