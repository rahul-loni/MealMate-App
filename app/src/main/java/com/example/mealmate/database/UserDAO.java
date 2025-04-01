package com.example.mealmate.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.example.mealmate.models.User;

public class UserDAO {
    private SQLiteDatabase db;
    private static final String TABLE_USER = "user"; // Ensure table name is correct

    public UserDAO(Context context) {
        DatabaseHelper dbHelper = new DatabaseHelper(context);
        db = dbHelper.getWritableDatabase();
    }

    public void saveUser(User user) {
        ContentValues values = new ContentValues();
        values.put("username", user.getUsername());
        values.put("bio", user.getBio());
        values.put("userImage", user.getUserImage());

        // Check if user exists
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_USER + " WHERE username = ?", new String[]{user.getUsername()});

        if (cursor.getCount() > 0) {
            // Update existing user
            db.update(TABLE_USER, values, "username = ?", new String[]{user.getUsername()});
        } else {
            // Insert new user
            db.insert(TABLE_USER, null, values);
        }
        cursor.close();
    }

    public User getUser(String username) {
        Cursor cursor = db.query(TABLE_USER, null, "username=?", new String[]{username}, null, null, null);
        if (cursor.moveToFirst()) {
            User user = new User(cursor.getString(0), cursor.getString(1), cursor.getString(2));
            cursor.close();
            return user;
        }
        cursor.close();
        return null;
    }

    public User getFirstUser() {
        Cursor cursor = db.query(TABLE_USER, null, null, null, null, null, null, "1");
        if (cursor.moveToFirst()) {
            User user = new User(cursor.getString(0), cursor.getString(1), cursor.getString(2));
            cursor.close();
            return user;
        }
        cursor.close();
        return null;
    }
}
