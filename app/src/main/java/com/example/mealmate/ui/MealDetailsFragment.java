package com.example.mealmate.ui;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.mealmate.R;
import com.example.mealmate.database.GroceryDAO;
import com.example.mealmate.database.MealDAO;
import com.example.mealmate.models.Grocery;
import com.example.mealmate.models.Meal;

import java.util.List;

public class MealDetailsFragment extends Fragment {

    private static final int REQUEST_PERMISSION_CODE = 100;

    private ImageView mealImageView;
    private TextView mealNameTextView, mealCategoryTextView, mealInstructionsTextView, ingredientsTextView;
    private Button addToGroceryButton, toggleFavoriteButton, deleteMealButton;
    private MealDAO mealDAO;
    private Meal meal;
    private Spinner daySpinner; // Spinner to select the day

    public MealDetailsFragment() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_meal_details, container, false);

        // Initialize views
        mealImageView = view.findViewById(R.id.meal_image);
        mealNameTextView = view.findViewById(R.id.meal_name);
        mealCategoryTextView = view.findViewById(R.id.meal_category);
        mealInstructionsTextView = view.findViewById(R.id.meal_instructions);
        addToGroceryButton = view.findViewById(R.id.add_to_grocery_button);
        toggleFavoriteButton = view.findViewById(R.id.toggle_favorite_button);
        deleteMealButton = view.findViewById(R.id.delete_meal_button);
        ingredientsTextView = view.findViewById(R.id.ingredients);
        daySpinner = view.findViewById(R.id.day_spinner); // Initialize the spinner

        mealDAO = new MealDAO(getContext());

        // Request permissions if necessary
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_MEDIA_IMAGES}, REQUEST_PERMISSION_CODE);
        }

        if (getArguments() != null) {
            meal = (Meal) getArguments().getSerializable("meal");

            // Populate UI with meal details
            mealNameTextView.setText(meal.getMealName());
            mealCategoryTextView.setText(meal.getCategory());
            mealInstructionsTextView.setText(meal.getInstructions());
            // Convert list to a proper string
            List<String> ingredientsList = meal.getIngredients();
            if (ingredientsList != null && !ingredientsList.isEmpty()) {
                String formattedIngredients = "- " + String.join("\n- ", ingredientsList);
                ingredientsTextView.setText(formattedIngredients);
            } else {
                ingredientsTextView.setText("No ingredients available.");
            }


            // Set up the spinner for day selection
            String[] daysOfWeek = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
            ArrayAdapter<String> dayAdapter = new ArrayAdapter<>(getContext(), R.layout.spinner_item, daysOfWeek);
            dayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            daySpinner.setAdapter(dayAdapter);

            // Set the current day in the spinner
            int currentDayPosition = getDayPosition(meal.getDay());
            daySpinner.setSelection(currentDayPosition);

            // Handle day change
            daySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                    String selectedDay = daysOfWeek[position];
                    meal.setDay(selectedDay); // Update the meal object
                    mealDAO.updateMeal(meal); // Save the updated meal to the database

                    Toast.makeText(getContext(), "Meal day updated to " + selectedDay, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parentView) {}
            });

            // Load the image with Glide
            String mealImageUrl = meal.getMealImage();
            if (mealImageUrl != null && !mealImageUrl.isEmpty()) {
                Glide.with(this)
                        .load(mealImageUrl)
                        .into(mealImageView);
            } else {
                // If there's no image, set a placeholder or default image
                mealImageView.setImageResource(R.drawable.mealmate_logo); // Use your default image resource here
            }

            // Set Favorite button text
            updateFavoriteButton();

            // Handle button clicks
            addToGroceryButton.setOnClickListener(v -> addIngredientsToGrocery());
            toggleFavoriteButton.setOnClickListener(v -> toggleFavorite());
            deleteMealButton.setOnClickListener(v -> deleteMeal());
        }

        return view;
    }

    private void addIngredientsToGrocery() {
        GroceryDAO groceryDAO = new GroceryDAO(getContext()); // Initialize DAO

        // Get ingredients list
        List<String> ingredientsList = meal.getIngredients();

        if (ingredientsList == null || ingredientsList.isEmpty()) {
            Toast.makeText(getContext(), "No ingredients to add!", Toast.LENGTH_SHORT).show();
            return;
        }

        for (String ingredient : ingredientsList) {
            ingredient = ingredient.trim(); // Remove extra spaces

            if (!ingredient.isEmpty()) {
                Grocery grocery = new Grocery(
                        0, // ID (Auto-increment)
                        ingredient, // Item name
                        0.0, // Default price
                        1, // Default quantity
                        "Uncategorized", // Default category
                        "", // No image by default
                        false, // Not purchased yet
                        "" // No store location by default
                );

                groceryDAO.addGrocery(grocery); // Add to database
            }
        }

        Toast.makeText(getContext(), "Ingredients added to Grocery List!", Toast.LENGTH_SHORT).show();
    }


    private void toggleFavorite() {
        meal.setFavourite(!meal.isFavourite());
        mealDAO.updateMeal(meal);
        updateFavoriteButton();
        Toast.makeText(getContext(), meal.isFavourite() ? "Added to Favorites" : "Removed from Favorites", Toast.LENGTH_SHORT).show();
    }

    private void updateFavoriteButton() {
        toggleFavoriteButton.setText(meal.isFavourite() ? "Unfavorite" : "Favorite");
    }

    private void deleteMeal() {
        mealDAO.deleteMeal(meal.getMealId());
        Toast.makeText(getContext(), "Meal Deleted", Toast.LENGTH_SHORT).show();

        // Return to HomeFragment after deletion
        getParentFragmentManager().popBackStack();
    }

    // Helper method to get the position of the day in the array
    private int getDayPosition(String day) {
        String[] daysOfWeek = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
        for (int i = 0; i < daysOfWeek.length; i++) {
            if (daysOfWeek[i].equals(day)) {
                return i;
            }
        }
        return 0; // Default to Sunday if not found
    }

    // Handle permission request result
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, now you can load the image
                Toast.makeText(getContext(), "Permission granted!", Toast.LENGTH_SHORT).show();
            } else {
                // Permission denied
                Toast.makeText(getContext(), "Permission denied. Cannot load image.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
