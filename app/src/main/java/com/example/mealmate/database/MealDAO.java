package com.example.mealmate.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.mealmate.models.Meal;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MealDAO extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "MealMate.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_MEALS = "meals";

    public MealDAO(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_MEALS + " (" +
                "mealId INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "mealName TEXT, " +
                "mealImage TEXT, " +
                "ingredients TEXT, " +  // Stored as a comma-separated string
                "instructions TEXT, " +
                "category TEXT, " +
                "day TEXT, " +
                "favourite INTEGER)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MEALS);
        onCreate(db);
    }

    // ✅ Add a Meal
    public boolean addMeal(Meal meal) {
        if (meal == null) return false; // Check if the meal is null

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("mealName", meal.getMealName());
        values.put("mealImage", meal.getMealImage() != null ? meal.getMealImage() : "");  // Handle null images
        values.put("ingredients", String.join(",", meal.getIngredients())); // Convert list to string
        values.put("instructions", meal.getInstructions());
        values.put("category", meal.getCategory());
        values.put("day", meal.getDay());
        values.put("favourite", meal.isFavourite() ? 1 : 0);

        // Insert the meal and get the newly generated mealId
        long result = db.insert(TABLE_MEALS, null, values);

        // If insertion was successful, update mealId
        if (result != -1) {
            meal.setMealId((int) result);  // Set the mealId from the inserted row ID
        }

        db.close();
        return result != -1;
    }


    // ✅ Get All Meals
    public List<Meal> getAllMeals() {
        List<Meal> mealList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_MEALS, null);

        if (cursor.moveToFirst()) {
            do {
                int mealId = cursor.getInt(0);
                String mealName = cursor.getString(1);
                String mealImage = cursor.getString(2);
                List<String> ingredients = Arrays.asList(cursor.getString(3).split(",")); // Convert string to list
                String instructions = cursor.getString(4);
                String category = cursor.getString(5);
                String day = cursor.getString(6);
                boolean favourite = cursor.getInt(7) == 1;

                Meal meal = new Meal(mealName, mealImage, ingredients, instructions, category, day, favourite);
                meal.setMealId(mealId);  // ✅ Set the ID from the database

                mealList.add(meal);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return mealList;
    }

    // ✅ Update Meal
    public boolean updateMeal(Meal meal) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("mealName", meal.getMealName());
        values.put("mealImage", meal.getMealImage());
        values.put("ingredients", String.join(",", meal.getIngredients())); // Convert list to string
        values.put("instructions", meal.getInstructions());
        values.put("category", meal.getCategory());
        values.put("day", meal.getDay());
        values.put("favourite", meal.isFavourite() ? 1 : 0);

        int result = db.update(TABLE_MEALS, values, "mealId=?", new String[]{String.valueOf(meal.getMealId())});
        db.close();
        return result > 0;
    }

    // ✅ Delete Meal
    public boolean deleteMeal(int mealId) {
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete(TABLE_MEALS, "mealId=?", new String[]{String.valueOf(mealId)});
        db.close();
        return result > 0;
    }
}
