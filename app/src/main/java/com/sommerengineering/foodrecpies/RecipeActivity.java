package com.sommerengineering.foodrecpies;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.sommerengineering.foodrecpies.models.Recipe;

public class RecipeActivity extends BaseActivity {

    Recipe recipe;

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

        // get passed recipe object
        getIncomingIntent();
    }

    private void getIncomingIntent() {
        if (getIntent().hasExtra("recipe")) {
            recipe = getIntent().getParcelableExtra("recipe");
        }
    }
}
