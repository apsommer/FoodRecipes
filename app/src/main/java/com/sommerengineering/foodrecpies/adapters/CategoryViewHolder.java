package com.sommerengineering.foodrecpies.adapters;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sommerengineering.foodrecpies.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class CategoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    OnRecipeListener listener;
    TextView categoryTitle;
    CircleImageView categoryImage;

    public CategoryViewHolder(@NonNull View itemView, OnRecipeListener listener) {
        super(itemView);

        this.listener = listener;
        categoryImage = itemView.findViewById(R.id.category_image);
        categoryTitle = itemView.findViewById(R.id.category_title);

        itemView.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        listener.onCategoryClick(categoryTitle.getText().toString());
    }
}
