package com.example.mealmate.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.example.mealmate.models.Grocery;
import java.util.ArrayList;
import java.util.List;

public class GroceryDAO {
    private SQLiteDatabase db;

    public GroceryDAO(Context context) {
        DatabaseHelper dbHelper = new DatabaseHelper(context);
        db = dbHelper.getWritableDatabase();
    }

    // Insert a new grocery item
    public long addGrocery(Grocery grocery) {
        ContentValues values = new ContentValues();
        values.put("item", grocery.getItem());
        values.put("price", grocery.getPrice());
        values.put("quantity", grocery.getQuantity());
        values.put("category", grocery.getCategory());
        values.put("itemImage", grocery.getItemImage());
        values.put("purchased", grocery.isPurchased() ? 1 : 0);
        values.put("storeLocation", grocery.getStoreLocation());
        return db.insert(DatabaseHelper.TABLE_GROCERY, null, values);
    }

    // Get all grocery items
    public List<Grocery> getAllGroceries() {
        List<Grocery> groceryList = new ArrayList<>();
        Cursor cursor = db.query(DatabaseHelper.TABLE_GROCERY, null, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                Grocery grocery = new Grocery(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getDouble(2),
                        cursor.getInt(3),
                        cursor.getString(4),
                        cursor.getString(5),
                        cursor.getInt(6) == 1,
                        cursor.getString(7)
                );
                groceryList.add(grocery);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return groceryList;
    }

    // Update grocery item
    public int updateGrocery(Grocery grocery) {
        ContentValues values = new ContentValues();
        values.put("item", grocery.getItem());
        values.put("price", grocery.getPrice());
        values.put("quantity", grocery.getQuantity());
        values.put("category", grocery.getCategory());
        values.put("itemImage", grocery.getItemImage());
        values.put("purchased", grocery.isPurchased() ? 1 : 0);
        values.put("storeLocation", grocery.getStoreLocation());

        return db.update(DatabaseHelper.TABLE_GROCERY, values, "id=?", new String[]{String.valueOf(grocery.getId())});
    }

    // Delete grocery item
    public void deleteGrocery(int id) {
        db.delete(DatabaseHelper.TABLE_GROCERY, "id=?", new String[]{String.valueOf(id)});
    }
}
