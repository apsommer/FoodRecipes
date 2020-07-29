package com.sommerengineering.foodrecpies;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.sommerengineering.foodrecpies.models.Recipe;
import com.sommerengineering.foodrecpies.viewmodels.RecipeListViewModel;
import com.sommerengineering.foodrecpies.viewmodels.RecipeViewModel;

public class RecipeActivity extends BaseActivity {

    private static final String TAG = "RecipeActivity";

    Recipe recipe;
    RecipeViewModel recipeViewModel;

    // UI
    private ImageView recipeImage;
    private TextView recipeTitle, recipeRank;
    private LinearLayout ingredientsContainer;
    private ScrollView scrollView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);

        // get UI references
        recipeImage = findViewById(R.id.recipe_image);
        recipeTitle = findViewById(R.id.recipe_title);
        recipeRank = findViewById(R.id.recipe_social_score);
        ingredientsContainer = findViewById(R.id.ingredients_container);
        scrollView = findViewById(R.id.parent);

        // get singleton viewmodel
        recipeViewModel = new ViewModelProvider(this).get(RecipeViewModel.class);

        // get passed recipe object
        getIncomingIntent();

        subscribeObservers();
    }

    private void getIncomingIntent() {
        if (getIntent().hasExtra("recipe")) {
            recipe = getIntent().getParcelableExtra("recipe");
            Log.d(TAG, "getIncomingIntent: " + recipe.getTitle());
            recipeViewModel.searchRecipeById(recipe.getRecipe_id());
        }
    }

    private void subscribeObservers() {
        recipeViewModel.getRecipe().observe(this, new Observer<Recipe>() {

            @Override
            public void onChanged(Recipe recipe) {
                if (recipe == null) return;
                Log.d(TAG, "onChanged: " + recipe.getTitle());
                for (String ingredient: recipe.getIngredients()) Log.d(TAG, ingredient);
            }
        });
    }
}
