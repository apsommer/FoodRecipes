package com.sommerengineering.foodrecpies.util;

import android.util.Log;

import com.sommerengineering.foodrecpies.models.Recipe;

import java.util.List;

public class Testing {

    public static void printRecipes(List<Recipe> recipes, String tag) {
        for (Recipe recipe : recipes) {
            Log.d(tag, "printRecipes: " + recipe.getTitle());
        }
    }
}
