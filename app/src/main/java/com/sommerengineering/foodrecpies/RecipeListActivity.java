package com.sommerengineering.foodrecpies;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.sommerengineering.foodrecpies.adapters.OnRecipeListener;
import com.sommerengineering.foodrecpies.adapters.RecipeRecyclerAdapter;
import com.sommerengineering.foodrecpies.models.Recipe;
import com.sommerengineering.foodrecpies.reqeusts.RecipeApi;
import com.sommerengineering.foodrecpies.reqeusts.ServiceGenerator;
import com.sommerengineering.foodrecpies.reqeusts.responses.RecipeResponse;
import com.sommerengineering.foodrecpies.reqeusts.responses.RecipeSearchResponse;
import com.sommerengineering.foodrecpies.util.Testing;
import com.sommerengineering.foodrecpies.util.VerticalSpacingItemDecorator;
import com.sommerengineering.foodrecpies.viewmodels.RecipeListViewModel;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecipeListActivity extends BaseActivity implements OnRecipeListener {

    private static final String TAG = "RecipeListActivity ~~";

    private RecipeListViewModel recipeListViewModel;
    private RecyclerView recyclerView;
    private RecipeRecyclerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // inflate this specific activity's full screen layout
        // this method is overriden in base class
        setContentView(R.layout.activity_recipe_list);

        // get singleton viewmodel
        recipeListViewModel = new ViewModelProvider(this).get(RecipeListViewModel.class);

        // subscribe to livedata streams
        initRecycler();
        subscribeObservers();
        initSearchView();

        // either viewing recipes, or categories
        if(!recipeListViewModel.isViewingRecipes()) displaySearchCategories();
    }

    private void displaySearchCategories() {
        recipeListViewModel.setIsViewingRecipes(false);
        adapter.displaySearchCategories();
    }

    private void initRecycler() {

        recyclerView = findViewById(R.id.recipe_list);
        adapter = new RecipeRecyclerAdapter(this);
        VerticalSpacingItemDecorator decoration = new VerticalSpacingItemDecorator(30);
        recyclerView.addItemDecoration(decoration);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    private void initSearchView() {

        final SearchView searchView = findViewById(R.id.search_view);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {

                // turn on loading indicator
                adapter.displayLoading();

                // call viewmodel
                recipeListViewModel.searchRecipesApi(query, 1);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    private void subscribeObservers() {

        recipeListViewModel.getRecipes().observe(this, new Observer<List<Recipe>>() {

            @Override
            public void onChanged(List<Recipe> recipes) {

                if (recipes == null) return;
                progressBar.setVisibility(View.INVISIBLE);
                Testing.printRecipes(recipes, TAG);
                adapter.setRecipes(recipes);
            }
        });
    }

    @Override
    public void onRecipeClick(int position) {

    }

    @Override
    public void onCategoryClick(String category) {
        adapter.displayLoading();
        recipeListViewModel.searchRecipesApi(category, 1);
    }
}