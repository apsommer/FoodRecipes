package com.sommerengineering.foodrecpies;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.sommerengineering.foodrecpies.models.Recipe;
import com.sommerengineering.foodrecpies.reqeusts.RecipeApi;
import com.sommerengineering.foodrecpies.reqeusts.ServiceGenerator;
import com.sommerengineering.foodrecpies.reqeusts.responses.RecipeResponse;
import com.sommerengineering.foodrecpies.reqeusts.responses.RecipeSearchResponse;
import com.sommerengineering.foodrecpies.viewmodels.RecipeListViewModel;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecipeListActivity extends BaseActivity {

    private static final String TAG = "RecipeListActivity ~~";

    private RecipeListViewModel recipeListViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // inflate this specific activity's full screen layout
        // this method is overriden in base class
        setContentView(R.layout.activity_recipe_list);

        recipeListViewModel = new ViewModelProvider(this).get(RecipeListViewModel.class);

        subscribeObservers();
    }

    private void subscribeObservers() {

        recipeListViewModel.getRecipes().observe(this, new Observer<List<Recipe>>() {

            @Override
            public void onChanged(List<Recipe> recipes) {

            }
        });
    }

    private void testRetrofitRequest() {

        RecipeApi recipeApi = ServiceGenerator.getRecipeApi();
        Call<RecipeSearchResponse> searchCall = recipeApi.searchRecipe("chicken", "2");

        searchCall.enqueue(new Callback<RecipeSearchResponse>() {

            @Override
            public void onResponse(Call<RecipeSearchResponse> call, Response<RecipeSearchResponse> response) {

                Log.d(TAG, "onResponse: server response: " + response.toString());

                if(response.code() == 200) {
                    Log.d(TAG, "onResponse: " + response.body().toString());
                    List<Recipe> recipes = new ArrayList<>(response.body().getRecipes());
                    for (Recipe recipe : recipes) {
                        Log.d(TAG, "onResponse: " + recipe.getTitle());
                    }
                }
            }

            @Override
            public void onFailure(Call<RecipeSearchResponse> call, Throwable t) {

            }
        });

        Call<RecipeResponse> recipeCall = recipeApi.getRecipe("41470");

        recipeCall.enqueue(new Callback<RecipeResponse>() {

            @Override
            public void onResponse(Call<RecipeResponse> call, Response<RecipeResponse> response) {
                Log.d(TAG, "onResponse: server response: " + response.toString());

                if(response.code() == 200) {

                    Log.d(TAG, "onResponse: " + response.body().toString());

                    Recipe recipe = response.body().getRecipe();
                    Log.d(TAG, "onResponse: " + recipe.getTitle());

                }
            }

            @Override
            public void onFailure(Call<RecipeResponse> call, Throwable t) {

            }
        });
    }

}