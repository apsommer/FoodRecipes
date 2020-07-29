package com.sommerengineering.foodrecpies.viewmodels;

import android.util.Log;

import androidx.appcompat.widget.SearchView;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.sommerengineering.foodrecpies.models.Recipe;
import com.sommerengineering.foodrecpies.repositories.RecipeRepository;

import java.util.List;

import static android.content.ContentValues.TAG;

public class RecipeListViewModel extends ViewModel {

    private RecipeRepository recipeRepository;
    private boolean isViewingRecipes;
    private boolean isPerformingQuery;

    public RecipeListViewModel() {
        recipeRepository = RecipeRepository.getInstance();
        isPerformingQuery = false;
        isViewingRecipes = false;
    }

    // call repo
    public LiveData<List<Recipe>> getRecipes() {
        return recipeRepository.getRecipes();
    }

    public void searchRecipesApi(String query, int page) {

        // call repo
        isViewingRecipes = true;
        isPerformingQuery = true;
        recipeRepository.searchRecipesApi(query, page);
    }

    public void searchNextPage() {

        // if we are performing a query or viewing categories do not request next page
        if (isPerformingQuery || !isViewingRecipes) return;
        recipeRepository.searchNextPage();
    }

    public boolean isViewingRecipes() {
        return isViewingRecipes;
    }

    public void setIsViewingRecipes(boolean isViewingRecipes) {
        this.isViewingRecipes = isViewingRecipes;
    }

    public void setIsPerformingQuery(boolean isPerformingQuery) {
        this.isPerformingQuery = isPerformingQuery;
    }

    public boolean isPerformingQuery() {
        return isPerformingQuery;
    }

    // confusing mechanism, use navigation component
    public boolean onBackedPressed() {

        Log.d(TAG, "onBackedPressed: isViewingRecipes: " + isViewingRecipes +
            " isPerformingQuery: " + isPerformingQuery);

        if (isPerformingQuery) {
            recipeRepository.cancelRequest();
            isPerformingQuery = false;
        }

        if (isViewingRecipes) {
            isViewingRecipes = false;
            return false;
        }
        return true;
    }
}
