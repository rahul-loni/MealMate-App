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

public class MealAdapter extends RecyclerView.Adapter<MealAdapter.MealViewHolder> {

    private List<Meal> mealList;
    private OnMealClickListener onMealClickListener;

    // Interface for click handling
    public interface OnMealClickListener {
        void onMealClick(Meal meal);
    }

    public MealAdapter(List<Meal> mealList, OnMealClickListener listener) {
        this.mealList = mealList;
        this.onMealClickListener = listener;
    }

    @Override
    public MealViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_meal, parent, false);
        return new MealViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MealViewHolder holder, int position) {
        Meal meal = mealList.get(position);
        holder.mealNameTextView.setText(meal.getMealName());
        holder.mealCategoryTextView.setText(meal.getCategory());
        holder.mealDayTextView.setText(meal.getDay());

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

    public static class MealViewHolder extends RecyclerView.ViewHolder {
        TextView mealNameTextView, mealCategoryTextView, mealDayTextView;
        ImageView mealImageView;

        public MealViewHolder(View itemView) {
            super(itemView);
            mealNameTextView = itemView.findViewById(R.id.meal_name);
            mealCategoryTextView = itemView.findViewById(R.id.meal_category);
            mealDayTextView = itemView.findViewById(R.id.meal_day);
            mealImageView = itemView.findViewById(R.id.meal_image);
        }
    }
}
