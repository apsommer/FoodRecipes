package com.sommerengineering.foodrecpies;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
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
    private SearchView searchView;

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

        // custom toolbar
        setSupportActionBar(findViewById(R.id.toolbar));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.recipe_search_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        // normally switch statement here for each menu item, just one for this example
        if (item.getItemId() == R.id.action_categories) {
            displaySearchCategories();
        }
        return super.onOptionsItemSelected(item);
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

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {

                // means "when user can no longer scroll in vertical direction"
                if (!recyclerView.canScrollVertically(1)) {
                    recipeListViewModel.searchNextPage();
                }
            }
        });
    }

    @Override // called every time back button is pressed
    public void onBackPressed() {

        if (recipeListViewModel.onBackedPressed()) super.onBackPressed();
        else displaySearchCategories();
    }

    private void initSearchView() {

        searchView = findViewById(R.id.search_view);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {

                // turn on loading indicator
                adapter.displayLoading();

                // call viewmodel
                recipeListViewModel.searchRecipesApi(query, 1);

                // clear focus for proper back button behavior
                searchView.clearFocus();

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    // pub/sub pattern goes all the way client runnable
    private void subscribeObservers() {

        recipeListViewModel.getRecipes().observe(this, new Observer<List<Recipe>>() {

            @Override
            public void onChanged(List<Recipe> recipes) {

                Log.d(TAG, "onChanged: ");

                // do not update recipes if we are viewing categories
                if (recipes == null || !recipeListViewModel.isViewingRecipes()) return;

                recipeListViewModel.setIsViewingRecipes(true);
                recipeListViewModel.setIsPerformingQuery(false);
                Testing.printRecipes(recipes, TAG);
                adapter.setRecipes(recipes);
            }
        });

        recipeListViewModel.isQueryExhausted().observe(this, new Observer<Boolean>() {

            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean) {
                    adapter.setQueryExhausted();
                }
            }
        });
    }

    @Override
    public void onRecipeClick(int position) {
        Intent intent = new Intent(this, RecipeActivity.class);
        intent.putExtra("recipe", adapter.getSelectedRecipe(position));
        startActivity(intent);
    }

    @Override
    public void onCategoryClick(String category) {

        adapter.displayLoading();
        recipeListViewModel.searchRecipesApi(category, 1);

        // clear focus for proper back button behavior
        searchView.clearFocus();
    }
}