package com.sommerengineering.foodrecpies.reqeusts;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.sommerengineering.foodrecpies.AppExecutors;
import com.sommerengineering.foodrecpies.models.Recipe;
import com.sommerengineering.foodrecpies.reqeusts.responses.RecipeResponse;
import com.sommerengineering.foodrecpies.reqeusts.responses.RecipeSearchResponse;
import com.sommerengineering.foodrecpies.util.Constants;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Singleton pattern.
 */
public class RecipeApiClient {

    private static final String TAG = "RecipeApiClient ~~";

    // singleton
    private static RecipeApiClient instance;
    public static RecipeApiClient getInstance() {
        if (instance == null) {
            instance = new RecipeApiClient();
        }
        return instance;
    }

    // expose livedata streams
    private MutableLiveData<List<Recipe>> recipes;
    private MutableLiveData<Recipe> recipe;
    private MutableLiveData<Boolean> isRecipeTimedOut;
    private RecipeApiClient() {
        recipes = new MutableLiveData<>();
        recipe = new MutableLiveData<>();
        isRecipeTimedOut = new MutableLiveData<>();
    }

    public LiveData<List<Recipe>> getRecipes() {
        return recipes;
    }

    public LiveData<Recipe> getRecipe() {
        return recipe;
    }

    public LiveData<Boolean> isRecipeTimedOut() {
        return isRecipeTimedOut;
    }

    private RetrieveRecipesRunnable retrieveRecipesRunnable;
    public void searchRecipesApi(String query, int page) {

        // ensure new query
        if (retrieveRecipesRunnable != null) retrieveRecipesRunnable = null;
        retrieveRecipesRunnable = new RetrieveRecipesRunnable(query, page);

        // start the endpoint query
        final Future handler = AppExecutors.getInstance().networkIO().submit(retrieveRecipesRunnable);

        // cancel the query after timeout
        AppExecutors.getInstance().networkIO().schedule(new Runnable() {
            @Override
            public void run() {

                // let user know it's timed out
                handler.cancel(true);
            }
        }, Constants.NETWORK_TIMEOUT, TimeUnit.MILLISECONDS);
    }

    private class RetrieveRecipesRunnable implements Runnable {

        private String query;
        private int page;
        private boolean cancelRequest;

        public RetrieveRecipesRunnable(String query, int page) {
            this.query = query;
            this.page = page;
            cancelRequest = false;
        }

        @Override
        public void run() {

            try {

                // execute call on via background thread
                Response response = getRecipes(query, page).execute();

                Log.d(TAG, "run: here");

                // this waits until results received, no callback

                // user cancellation flag
                if (cancelRequest) return;

                // ok
                if (response.code() == 200) {

                    // get response
                    List<Recipe> list = new ArrayList<>();
                    list.addAll(((RecipeSearchResponse) response.body()).getRecipes());

                    if (page == 1) recipes.postValue(list);
                    else {

                        // get all previous recipes and append new ones
                        List<Recipe> current = recipes.getValue();
                        current.addAll(list);

                        // update livedata
                        recipes.postValue(current);
                    }

                // error
                } else {

                    Log.e(TAG, "run: " + response.errorBody());

                    // update livedata
                    recipes.postValue(null);
                }

            // error
            } catch (IOException e) {
                e.printStackTrace();
                recipes.postValue(null);
            }

        }

        // retrofit call
        private Call<RecipeSearchResponse> getRecipes(String query, int page) {
            return ServiceGenerator.getRecipeApi().searchRecipe(query, String.valueOf(page));
        }

        private void cancelRequest() {
            Log.d(TAG, "cancelRequest: canceling the search request.");
            cancelRequest = true;
        }
    }

    // following methods are very similar to above (future > executor > runnable makes retrofit call)
    private RetrieveRecipeRunnable retrieveRecipeRunnable;
    public void searchRecipeById(String id) {

        if (retrieveRecipeRunnable != null) retrieveRecipeRunnable = null;
        retrieveRecipeRunnable = new RetrieveRecipeRunnable(id);

        final Future handler = AppExecutors.getInstance().networkIO().submit(retrieveRecipeRunnable);
        isRecipeTimedOut.postValue(false);

        AppExecutors.getInstance().networkIO().schedule(new Runnable() {

            @Override
            public void run() {
                isRecipeTimedOut.postValue(true);
                handler.cancel(true);
            }
        }, Constants.NETWORK_TIMEOUT, TimeUnit.MILLISECONDS);
    }

    private class RetrieveRecipeRunnable implements Runnable {

        private String id;
        private boolean cancelRequest;

        public RetrieveRecipeRunnable(String id) {
            this.id = id;
            cancelRequest = false;
        }

        @Override
        public void run() {

            try {

                // execute call on via background thread
                Response response = getRecipe(id).execute();

                Log.d(TAG, "run: here");

                // this waits until results received, no callback

                // user cancellation flag
                if (cancelRequest) return;

                // ok
                if (response.code() == 200) {

                    // get response
                    Recipe selectedRecipe = (((RecipeResponse) response.body()).getRecipe());
                    recipe.postValue(selectedRecipe);

                // error
                } else {

                    Log.e(TAG, "run: " + response.errorBody());

                    // update livedata
                    recipe.postValue(null);
                }

            // error
            } catch (IOException e) {
                e.printStackTrace();
                recipe.postValue(null);
            }

        }

        // retrofit call
        private Call<RecipeResponse> getRecipe(String id) {
            return ServiceGenerator.getRecipeApi().getRecipe(id);
        }

        private void cancelRequest() {
            Log.d(TAG, "cancelRequest: canceling the search request.");
            cancelRequest = true;
        }
    }

    public void cancelRequest() {
        if (retrieveRecipesRunnable != null) retrieveRecipesRunnable.cancelRequest();
        if (retrieveRecipeRunnable != null) retrieveRecipeRunnable.cancelRequest();
    }
}
