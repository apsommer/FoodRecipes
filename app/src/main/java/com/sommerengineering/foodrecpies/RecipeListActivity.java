package com.sommerengineering.foodrecpies;

import androidx.appcompat.app.AppCompatActivity;
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
        testRetrofitRequest();
    }

    private void initRecycler() {

        recyclerView = findViewById(R.id.recipe_list);
        adapter = new RecipeRecyclerAdapter(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

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

    public void searchRecipesApi(String query, int page) {

        // call viewmodel
        recipeListViewModel.searchRecipesApi(query, page);
    }

    private void testRetrofitRequest() {
        searchRecipesApi("chicken" , 1);
    }

    @Override
    public void onRecipeClick(int position) {

    }

    @Override
    public void onCategoryClick(String category) {

    }
}