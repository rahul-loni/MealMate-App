package com.example.mealmate.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.mealmate.R;
import com.example.mealmate.models.Meal;
import com.bumptech.glide.Glide;

import java.util.List;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.MealViewHolder> {

    private List<Meal> mealList;
    private OnMealClickListener onMealClickListener;

    // Interface for click handling
    public interface OnMealClickListener {
        void onMealClick(Meal meal);
    }

    public SearchAdapter(List<Meal> mealList, OnMealClickListener listener) {
        this.mealList = mealList;
        this.onMealClickListener = listener;
    }

    @Override
    public MealViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.search_item, parent, false); // Corrected layout file
        return new MealViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SearchAdapter.MealViewHolder holder, int position) {
        Meal meal = mealList.get(position);
        holder.mealNameTextView.setText(meal.getMealName());
        holder.mealCategoryTextView.setText(meal.getCategory());




        // Load the image using Glide
        if (meal.getMealImage() != null && !meal.getMealImage().isEmpty()) {
            Glide.with(holder.mealImageView.getContext())
                    .load(meal.getMealImage())
                    .into(holder.mealImageView);
        } else {
            holder.mealImageView.setImageResource(R.drawable.mealmate_logo);
        }

        // Handle item click
        holder.itemView.setOnClickListener(v -> {
            if (onMealClickListener != null) {
                onMealClickListener.onMealClick(meal);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mealList.size();
    }

    public void updateList(List<Meal> newList) {
        mealList = newList;
        notifyDataSetChanged();
    }

    public static class MealViewHolder extends RecyclerView.ViewHolder {
        TextView mealNameTextView, mealCategoryTextView;
        ImageView mealImageView;

        public MealViewHolder(View itemView) {
            super(itemView);
            mealNameTextView = itemView.findViewById(R.id.search_meal_name);
            mealCategoryTextView = itemView.findViewById(R.id.search_meal_category);
            mealImageView = itemView.findViewById(R.id.search_meal_image);
        }
    }
}
