package com.sommerengineering.foodrecpies.adapters;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;

import com.sommerengineering.foodrecpies.R;

public class RecipeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    // attributes of recipe details layout
    TextView title, publisher, score;
    AppCompatImageView image;
    OnRecipeListener onRecipeListener;

    public RecipeViewHolder(@NonNull View itemView, OnRecipeListener onRecipeListener) {
        super(itemView);

        // forward clicks to adapter -> activity
        this.onRecipeListener = onRecipeListener;
        itemView.setOnClickListener(this);

        // get references to view entities
        title.findViewById(R.id.recipe_title);
        publisher.findViewById(R.id.recipe_publisher);
        score.findViewById(R.id.recipe_social_score);
        image.findViewById(R.id.recipe_image);
    }

    @Override
    public void onClick(View view) {

        // forward clicks to adapter -> activity
        onRecipeListener.onRecipeClick(getAdapterPosition());
    }
}
