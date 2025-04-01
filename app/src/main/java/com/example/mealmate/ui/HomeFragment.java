package com.example.mealmate.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mealmate.R;
import com.example.mealmate.adapters.MealAdapter;
import com.example.mealmate.database.MealDAO;
import com.example.mealmate.database.UserDAO;
import com.example.mealmate.models.Meal;
import com.example.mealmate.models.User;
import com.example.mealmate.utils.DateUtils;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private RecyclerView todaysMealsRecyclerView;
    private RecyclerView favoriteMealsRecyclerView;
    private RecyclerView categoryRecyclerView;
    private Spinner dayFilterSpinner;
    private Spinner categorySpinner;
    private MealDAO mealDao;
    private TextView welcomeTextView;
    private MealAdapter todaysMealsAdapter;
    private MealAdapter favoriteMealsAdapter;
    private MealAdapter categoryMealsAdapter;
    private List<Meal> allMeals;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Initialize views
        todaysMealsRecyclerView = view.findViewById(R.id.todays_meals_list);
        favoriteMealsRecyclerView = view.findViewById(R.id.favorite_meals_list);
        categoryRecyclerView = view.findViewById(R.id.category_list);
        dayFilterSpinner = view.findViewById(R.id.day_filter_spinner);
        categorySpinner = view.findViewById(R.id.category_spinner);
        welcomeTextView = view.findViewById(R.id.welcome_text);

        // Initialize MealDAO
        mealDao = new MealDAO(getContext());

        // Set up RecyclerView layout managers (All are Horizontal)
        LinearLayoutManager todaysMealsLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        todaysMealsRecyclerView.setLayoutManager(todaysMealsLayoutManager);

        LinearLayoutManager favoriteMealsLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        favoriteMealsRecyclerView.setLayoutManager(favoriteMealsLayoutManager);

        LinearLayoutManager categoryLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        categoryRecyclerView.setLayoutManager(categoryLayoutManager);

        // Load all meals
        allMeals = mealDao.getAllMeals();

//        loadUserProfile();

        // Set up Spinners
        setupSpinners();

        // Load meals based on default filters
        loadMeals(DateUtils.getCurrentDay());

        return view;
    }

//    private void loadUserProfile() {
//        UserDAO userDAO = new UserDAO(getContext());  // Make sure UserDAO is properly initialized
//        User currentUser = userDAO.getUser("sandesh");  // Change the ID as per your logic (e.g., logged-in user ID)
//
//        if (currentUser != null) {
//            // Update the welcome message with the username
//            String welcomeMessage = "Welcome, " + currentUser.getUsername();
//            welcomeTextView.setText(welcomeMessage);
//        } else {
//            // If no user found, show a default message
//            welcomeTextView.setText("Welcome, Sandesh");
//        }
//    }


    private void setupSpinners() {
        ArrayAdapter<String> dayAdapter = new ArrayAdapter<>(
                getContext(),
                R.layout.spinner_item,
                DateUtils.getDaysOfWeek()
        );
        dayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dayFilterSpinner.setAdapter(dayAdapter);
        dayFilterSpinner.setSelection(DateUtils.getCurrentDayIndex());

        dayFilterSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedDay = (String) parent.getItemAtPosition(position);
                loadMeals(selectedDay);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        ArrayAdapter<CharSequence> categoryAdapter = ArrayAdapter.createFromResource(
                getContext(),
                R.array.meal_categories,
                R.layout.spinner_item
        );
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(categoryAdapter);

        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedCategory = (String) parent.getItemAtPosition(position);
                filterMealsByCategory(selectedCategory);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    private void loadMeals(String selectedDay) {
        if (allMeals == null || allMeals.isEmpty()) {
            Toast.makeText(getContext(), "No meals found", Toast.LENGTH_SHORT).show();
            return;
        }

        List<Meal> filteredMeals = new ArrayList<>();
        for (Meal meal : allMeals) {
            if (meal.getDay() != null && meal.getDay().equalsIgnoreCase(selectedDay)) {
                filteredMeals.add(meal);
            }
        }

        List<Meal> favoriteMeals = new ArrayList<>();
        for (Meal meal : allMeals) {
            if (meal.isFavourite()) {
                favoriteMeals.add(meal);
            }
        }

        // Set up adapters with click listener to open MealDetailsFragment
        todaysMealsAdapter = new MealAdapter(filteredMeals, this::openMealDetails);
        favoriteMealsAdapter = new MealAdapter(favoriteMeals, this::openMealDetails);

        todaysMealsRecyclerView.setAdapter(todaysMealsAdapter);
        favoriteMealsRecyclerView.setAdapter(favoriteMealsAdapter);

        todaysMealsAdapter.notifyDataSetChanged();
        favoriteMealsAdapter.notifyDataSetChanged();
    }

    private void filterMealsByCategory(String category) {
        if (allMeals == null || allMeals.isEmpty()) {
            return;
        }

        List<Meal> filteredCategoryMeals = new ArrayList<>();
        for (Meal meal : allMeals) {
            if (meal.getCategory() != null && meal.getCategory().equalsIgnoreCase(category)) {
                filteredCategoryMeals.add(meal);
            }
        }

        categoryMealsAdapter = new MealAdapter(filteredCategoryMeals, this::openMealDetails);
        categoryRecyclerView.setAdapter(categoryMealsAdapter);
        categoryMealsAdapter.notifyDataSetChanged();
    }

    private void openMealDetails(Meal meal) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("meal", meal);

        MealDetailsFragment mealDetailsFragment = new MealDetailsFragment();
        mealDetailsFragment.setArguments(bundle);

        getParentFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, mealDetailsFragment)
                .addToBackStack(null)
                .commit();
    }
}
