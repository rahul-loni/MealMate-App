package com.example.mealmate.ui;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.mealmate.R;
import com.example.mealmate.database.MealDAO;
import com.example.mealmate.models.Meal;

import java.util.Arrays;

public class AddMealFragment extends Fragment {

    private static final int PICK_IMAGE_REQUEST = 1;

    private EditText mealNameEditText, ingredientsEditText, instructionsEditText;
    private Spinner categorySpinner, daySpinner;
    private ImageView mealImageView;
    private Button saveButton, selectImageButton;
    private MealDAO mealDao;
    private Uri imageUri;  // Store the image URI
    private Switch favoriteToggle;  // Reference to the Switch for favorite

    public AddMealFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_meal, container, false);

        // Initialize views
        mealNameEditText = view.findViewById(R.id.meal_name);
        ingredientsEditText = view.findViewById(R.id.ingredients);
        instructionsEditText = view.findViewById(R.id.instructions);
        categorySpinner = view.findViewById(R.id.category_spinner);
        daySpinner = view.findViewById(R.id.day_spinner);
        mealImageView = view.findViewById(R.id.image_preview);
        saveButton = view.findViewById(R.id.save_button);
        selectImageButton = view.findViewById(R.id.select_image_button);
        favoriteToggle = view.findViewById(R.id.favorite_toggle);  // Initialize the Switch

        mealDao = new MealDAO(getContext());  // Initialize the MealDAO

        // Set up spinners with categories and days
        setupSpinners();

        // Set up button click listeners
        selectImageButton.setOnClickListener(v -> openGallery());
        saveButton.setOnClickListener(v -> saveMeal());

        return view;
    }

    private void setupSpinners() {
        // Populate the spinners (you can use arrays or dynamic data)
        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(getContext(),
                R.layout.spinner_item, new String[]{"Breakfast", "Lunch", "Dinner"});
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(categoryAdapter);

        ArrayAdapter<String> dayAdapter = new ArrayAdapter<>(getContext(),
                R.layout.spinner_item, new String[]{"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"});
        dayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        daySpinner.setAdapter(dayAdapter);
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();  // Get the image URI
            mealImageView.setImageURI(imageUri);  // Display the selected image in the ImageView
        }
    }

    private void saveMeal() {
        String mealName = mealNameEditText.getText().toString();
        String ingredients = ingredientsEditText.getText().toString();
        String instructions = instructionsEditText.getText().toString();
        String category = categorySpinner.getSelectedItem().toString();
        String day = daySpinner.getSelectedItem().toString();

        // Validate inputs
        if (mealName.isEmpty() || ingredients.isEmpty() || instructions.isEmpty()) {
            Toast.makeText(getContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Convert ingredients to a comma-separated string
        String mealImage = (imageUri != null) ? imageUri.toString() : ""; // Store the URI as a string

        // Get the favorite value from the Switch (true if checked, false otherwise)
        boolean isFavorite = favoriteToggle.isChecked();

        // Create the meal object with the correct favorite status
        Meal newMeal = new Meal(mealName, mealImage, Arrays.asList(ingredients.split(",")), instructions, category, day, isFavorite);

        // Insert the meal into the database
        boolean success = mealDao.addMeal(newMeal);
        if (success) {
            Toast.makeText(getContext(), "Meal added successfully", Toast.LENGTH_SHORT).show();
            getActivity().getSupportFragmentManager().popBackStack();  // Go back to the previous fragment
        } else {
            Toast.makeText(getContext(), "Failed to add meal", Toast.LENGTH_SHORT).show();
        }
    }
}
