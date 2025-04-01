package com.example.mealmate.models;

import android.widget.ImageView;
import java.io.Serializable;
import java.util.List;

public class Meal implements Serializable {
    private int mealId;
    private String mealName;
    private String mealImage;
    private List<String> ingredients;
    private String instructions;
    private String category;
    private String day;
    private boolean favourite;

    public Meal(String mealName, String mealImage, List<String> ingredients, String instructions, String category, String day, boolean favourite) {
        this.mealName = mealName;
        this.mealImage = mealImage;
        this.ingredients = ingredients;
        this.instructions = instructions;
        this.category = category;
        this.day = day;
        this.favourite = favourite;
        this.mealId = -1;  // Set default value for mealId
    }

    public Meal() {

    }


    // Getters
    public int getMealId() { return mealId; }
    public String getMealName() { return mealName; }
    public String getMealImage() { return mealImage; }
    public List<String> getIngredients() { return ingredients; }
    public String getInstructions() { return instructions; }
    public String getCategory() { return category; }
    public String getDay() { return day; }
    public boolean isFavourite() { return favourite; }

    // Setters
    public void setMealId(int mealId) { this.mealId = mealId; }
    public void setMealName(String mealName) { this.mealName = mealName; }
    public void setMealImage(String mealImage) { this.mealImage = mealImage; }
    public void setIngredients(List<String> ingredients) { this.ingredients = ingredients; }
    public void setInstructions(String instructions) { this.instructions = instructions; }
    public void setCategory(String category) { this.category = category; }
    public void setDay(String day) { this.day = day; }
    public void setFavourite(boolean favourite) { this.favourite = favourite; }
}
