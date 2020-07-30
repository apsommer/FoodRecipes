package com.sommerengineering.foodrecpies;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
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
        showProgressBar(true);
        subscribeObservers();
    }

    private void getIncomingIntent() {

        if (!getIntent().hasExtra("recipe")) return;

        Recipe recipe = getIntent().getParcelableExtra("recipe");
        Log.d(TAG, "getIncomingIntent: " + recipe.getTitle());
        recipeViewModel.searchRecipeById(recipe.getRecipe_id());
    }

    private void subscribeObservers() {

        recipeViewModel.getRecipe().observe(this, new Observer<Recipe>() {

            @Override
            public void onChanged(Recipe recipe) {

                if (recipe == null) return;

                // since viewmodel survives activity destroy it retains the previous recipe in memory
                // effectively bails out of this method until a new recipe is retrieved (not the old one)
                if (!recipe.getRecipe_id().equals(recipeViewModel.getId())) return;

                Log.d(TAG, "onChanged: " + recipe.getTitle());
                recipeViewModel.setIsRecipeRetrieved(true);
                setRecipeProperties(recipe);
            }
        });

        recipeViewModel.isRecipeTimedOut().observe(this, new Observer<Boolean>() {

            @Override
            public void onChanged(Boolean aBoolean) {

                if (aBoolean && !recipeViewModel.getIsRecipeRetrieved()) {
                    Log.d(TAG, "onChanged: timed out ...");
                    displayErrorScreen("Check network connection.");
                }
            }
        });
    }

    private void displayErrorScreen(String errorMessage) {

        recipeTitle.setText("Error retrieving recipe ...");
        recipeRank.setText("");
        TextView textView = new TextView(this);
        if (!errorMessage.equals("")) textView.setText(errorMessage);
        else textView.setText("Error");
        textView.setTextSize(15);
        textView.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        ingredientsContainer.addView(textView);

        RequestOptions options = new RequestOptions().placeholder(R.drawable.ic_launcher_background);

        Glide.with(this)
                .setDefaultRequestOptions(options)
                .load(R.drawable.ic_launcher_background)
                .into(recipeImage);

        showParent();
        showProgressBar(false);
    }

    private void setRecipeProperties(Recipe recipe) {

        if (recipe == null) return;

        RequestOptions options = new RequestOptions().placeholder(R.drawable.ic_launcher_background);

        Glide.with(this)
                .setDefaultRequestOptions(options)
                .load(recipe.getImage_url())
                .into(recipeImage);

        recipeTitle.setText(recipe.getTitle());
        recipeRank.setText(String.valueOf(Math.round(recipe.getSocial_rank())));

        // ingredients are put into individual textviews at runtime
        ingredientsContainer.removeAllViews();
        for (String ingredient: recipe.getIngredients()) {
            TextView textView = new TextView(this);
            textView.setText(ingredient);
            textView.setTextSize(15);
            textView.setLayoutParams(new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            ingredientsContainer.addView(textView);
        }

        showParent();
        showProgressBar(false);
    }

    private void showParent() {
        scrollView.setVisibility(View.VISIBLE);
    }
}
