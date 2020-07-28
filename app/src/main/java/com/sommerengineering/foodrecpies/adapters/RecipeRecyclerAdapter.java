package com.sommerengineering.foodrecpies.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.sommerengineering.foodrecpies.R;
import com.sommerengineering.foodrecpies.models.Recipe;

import java.util.List;

/**
 * Classic recycler adapter. Viewholder in separate class.
 */
public class RecipeRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Recipe> recipes;
    private OnRecipeListener onRecipeListener;

    public RecipeRecyclerAdapter(List<Recipe> recipes, OnRecipeListener onRecipeListener) {
        this.recipes = recipes;
        this.onRecipeListener = onRecipeListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        // todo recycler to use multiple layouts
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_recipe_list_item, parent, false);
        return new RecipeViewHolder(view, onRecipeListener);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int i) {

        // set layout attributes with data from the recipes list
        ((RecipeViewHolder) holder).title.setText(recipes.get(i).getTitle());
        ((RecipeViewHolder) holder).publisher.setText(recipes.get(i).getPublisher());
        ((RecipeViewHolder) holder).score.setText(
                String.valueOf(Math.round(recipes.get(i).getSocial_rank())));

        // glide library handles fetching url
        RequestOptions requestOptions = new RequestOptions()
                .placeholder(R.drawable.ic_launcher_background);

        Glide.with(holder.itemView.getContext())
                .setDefaultRequestOptions(requestOptions)
                .load(recipes.get(i).getImage_url())
                .into(((RecipeViewHolder) holder).image);
    }

    @Override
    public int getItemCount() {
        return recipes.size();
    }

    // initialize with adapter, probably should be in constructor
    public void setRecipes(List<Recipe> recipes) {
        this.recipes = recipes;
        notifyDataSetChanged();
    }
}

