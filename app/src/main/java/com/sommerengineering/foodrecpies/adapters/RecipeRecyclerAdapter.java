package com.sommerengineering.foodrecpies.adapters;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.sommerengineering.foodrecpies.R;
import com.sommerengineering.foodrecpies.models.Recipe;
import com.sommerengineering.foodrecpies.util.Constants;

import java.util.ArrayList;
import java.util.List;

/**
 * Classic recycler adapter. Viewholder in separate class.
 */
public class RecipeRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int RECIPE_TYPE = 1;
    private static final int LOADING_TYPE = 2;
    private static final int CATEGORY_TYPE = 3;

    private List<Recipe> recipes;
    private OnRecipeListener onRecipeListener;

    public RecipeRecyclerAdapter(OnRecipeListener onRecipeListener) {
        this.onRecipeListener = onRecipeListener;
        recipes = new ArrayList<>();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view;
        switch (viewType) {

            case RECIPE_TYPE: {
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.layout_recipe_list_item, parent, false);
                return new RecipeViewHolder(view, onRecipeListener);

            } case LOADING_TYPE: {
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.layout_loading_list_item, parent, false);
                return new LoadingViewHolder(view);

            } case CATEGORY_TYPE: {
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.layout_category_list_item, parent, false);
                return new CategoryViewHolder(view, onRecipeListener);

            } default: {
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.layout_recipe_list_item, parent, false);
                return new RecipeViewHolder(view, onRecipeListener);
            }
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int i) {

        // loading list item has no data to bind
        if (getItemViewType(i) == LOADING_TYPE) return;

        if (getItemViewType(i) == CATEGORY_TYPE) {

            // glide library handles fetching url
            RequestOptions requestOptions = new RequestOptions()
                    .placeholder(R.drawable.ic_launcher_background);

            Glide.with(holder.itemView.getContext())
                    .setDefaultRequestOptions(requestOptions)
                    .load(holder.itemView.getContext().getResources().getIdentifier(
                            recipes.get(i).getImage_url(), "drawable", holder.itemView.getContext().getPackageName()))
                    .into(((CategoryViewHolder) holder).categoryImage);

            ((CategoryViewHolder) holder).categoryTitle.setText(recipes.get(i).getTitle());
            return;
        }

        // normal recipe list item ...

        // set layout attributes with data from the recipes list
        ((RecipeViewHolder) holder).title.setText(recipes.get(i).getTitle());
        ((RecipeViewHolder) holder).publisher.setText(recipes.get(i).getPublisher());
        ((RecipeViewHolder) holder).score.setText(
                String.valueOf(Math.round(recipes.get(i).getSocial_rank())));

        // glide library handles fetching url
        RequestOptions requestOptions = new RequestOptions()
                .placeholder(R.drawable.ic_launcher_background);

        Glide.with(((RecipeViewHolder) holder).itemView)
                .setDefaultRequestOptions(requestOptions)
                .load(recipes.get(i).getImage_url())
                .into(((RecipeViewHolder) holder).image);
    }

    @Override
    public int getItemViewType(int position) {
        if (recipes.get(position).getSocial_rank() == -1) return CATEGORY_TYPE;
        else if (recipes.get(position).getTitle().equals("LOADING...")) return LOADING_TYPE;
        return RECIPE_TYPE;
    }

    public void displaySearchCategories() {

        List<Recipe> categories = new ArrayList<>();
        for (int i = 0; i < Constants.DEFAULT_SEARCH_CATEGORIES.length; i ++) {
            Recipe recipe = new Recipe();
            recipe.setTitle(Constants.DEFAULT_SEARCH_CATEGORY_IMAGES[i]);
            recipe.setImage_url(Constants.DEFAULT_SEARCH_CATEGORY_IMAGES[i]);
            recipe.setSocial_rank(-1);
            categories.add(recipe);
        }
        recipes = categories;
        notifyDataSetChanged();
    }

    public void displayLoading() {

        // toggle the loading custom animation
        // means "is not already loading, and we want to show loading, so trigger the custom view"
        if (!isLoading()) {

            // use the recipe title to identify the loading state
            Recipe recipe = new Recipe();
            recipe.setTitle("LOADING...");
            List<Recipe> loadingList = new ArrayList<>();
            loadingList.add(recipe);
            recipes = loadingList;
            notifyDataSetChanged();
        }
    }

    private boolean isLoading() {

        if (recipes != null && recipes.size() > 0) {
            if (recipes.get(recipes.size()-1).getTitle().equals("LOADING...")) return true;
        }
        return false;
    }

    @Override
    public int getItemCount() {
        if (recipes == null) return 0;
        return recipes.size();
    }

    // initialize with adapter, can not be in constructor
    public void setRecipes(List<Recipe> recipes) {
        this.recipes = recipes;
        notifyDataSetChanged(); // turns off the loading indicator
    }
}

