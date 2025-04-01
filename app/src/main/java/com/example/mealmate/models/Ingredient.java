package com.example.mealmate.models;

public class Ingredient {
    private int id;
    private int mealId;
    private String name;

    public Ingredient(int id, int mealId, String name) {
        this.id = id;
        this.mealId = mealId;
        this.name = name;
    }

    // Getters & Setters
}
