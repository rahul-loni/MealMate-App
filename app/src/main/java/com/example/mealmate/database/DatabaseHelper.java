package com.example.mealmate.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "MealMateee.db";
    private static final int DATABASE_VERSION = 1;  // Keep it as 1 to create all tables in the same version

    // Table Names
    public static final String TABLE_MEAL = "Meal";
    public static final String TABLE_GROCERY = "Grocery";
    public static final String TABLE_USER = "User";
    public static final String TABLE_INGREDIENT = "Ingredient";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create Meal Table
        String createMealTable = "CREATE TABLE " + TABLE_MEAL + " (" +
                "mealId INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "mealName TEXT, " +
                "mealImage TEXT, " +  // Image path
                "instructions TEXT, " +
                "category TEXT, " +
                "day TEXT, " +  // Sunday, Monday, etc.
                "favourite INTEGER DEFAULT 0)";
        db.execSQL(createMealTable);

        // Create Grocery Table
        String createGroceryTable = "CREATE TABLE " + TABLE_GROCERY + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "item TEXT, " +
                "price REAL, " +
                "quantity INTEGER, " +
                "category TEXT, " +
                "itemImage TEXT, " +  // Image path
                "purchased INTEGER DEFAULT 0, " +  // 0 = Not Purchased, 1 = Purchased
                "storeLocation TEXT)";
        db.execSQL(createGroceryTable);

        // Create User Table
        String createUserTable = "CREATE TABLE " + TABLE_USER + " (" +
                "username TEXT PRIMARY KEY, " +
                "userImage TEXT, " +
                "bio TEXT)";
        db.execSQL(createUserTable);

        // Create Ingredient Table
        String createIngredientsTable = "CREATE TABLE " + TABLE_INGREDIENT + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "mealId INTEGER, " +
                "name TEXT, " +
                "FOREIGN KEY (mealId) REFERENCES Meal(mealId) ON DELETE CASCADE)";
        db.execSQL(createIngredientsTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // No need to drop tables when upgrading from version 1, because you're not making breaking changes yet.
        // However, if you plan to upgrade later (e.g., add a column), handle that here:
        if (oldVersion < 2) {
            // Example of adding new tables or columns in the future
            // You would only need this if you're upgrading to version 2, etc.
            // db.execSQL("ALTER TABLE " + TABLE_MEAL + " ADD COLUMN calories INTEGER DEFAULT 0");
        }
    }
}
