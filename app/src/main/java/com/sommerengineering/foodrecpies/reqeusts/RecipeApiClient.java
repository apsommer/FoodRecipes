package com.sommerengineering.foodrecpies.reqeusts;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.sommerengineering.foodrecpies.AppExecutors;
import com.sommerengineering.foodrecpies.models.Recipe;
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

    private static final String TAG = "RecipeApiClient";

    // singleton
    private static RecipeApiClient instance;
    public static RecipeApiClient getInstance() {
        if (instance == null) {
            instance = new RecipeApiClient();
        }
        return instance;
    }

    // expose livedata stream
    private MutableLiveData<List<Recipe>> recipes;
    private RecipeApiClient() {
        recipes = new MutableLiveData<>();
    }
    public LiveData<List<Recipe>> getRecipes() {
        return recipes;
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

                // let user know its timed out
                handler.cancel(true);
            }
        }, Constants.NETWORK_TIMEOUT, TimeUnit.MILLISECONDS);
    }

    // define
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
}
