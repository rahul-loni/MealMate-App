package com.example.mealmate.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mealmate.R;
import com.example.mealmate.adapters.GroceryAdapter;
import com.example.mealmate.database.GroceryDAO;
import com.example.mealmate.models.Grocery;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class GroceryFragment extends Fragment {

    private RecyclerView recyclerView;
    private GroceryAdapter adapter;
    private GroceryDAO groceryDAO;
    private Spinner categorySpinner;
    private String selectedCategory = "All"; // Default filter

    public GroceryFragment() {
        // Required empty constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_grocery, container, false);

        // Initialize Database
        groceryDAO = new GroceryDAO(requireContext());

        // Initialize RecyclerView
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Initialize Spinner for Filtering
        categorySpinner = view.findViewById(R.id.category_spinner);
        setupCategoryFilter();

        // Floating Action Button
        FloatingActionButton fabAddGrocery = view.findViewById(R.id.fabAddGrocery);
        fabAddGrocery.setOnClickListener(v -> {
            FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, new AddGroceryFragment());
            transaction.addToBackStack(null);
            transaction.commit();
        });

        // Load Groceries initially
        loadGroceries();

        // Set up Swipe Gestures
        setupSwipeGestures();

        return view;
    }

    private void setupCategoryFilter() {
        // Define categories
        String[] categories = {"All", "Vegetables", "Fruits", "Grains", "Dairy", "Meat", "Beverages"};

        // Set up Adapter for Spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), R.layout.spinner_item, categories);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(adapter);

        // Handle Selection Change
        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedCategory = categories[position];
                loadGroceries(); // Refresh the list based on selection
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedCategory = "All"; // Default to All
            }
        });
    }

    private void loadGroceries() {
        List<Grocery> allGroceries = groceryDAO.getAllGroceries();
        List<Grocery> filteredGroceries = new ArrayList<>();

        // Filter based on selected category
        if (selectedCategory.equals("All")) {
            filteredGroceries = allGroceries;
        } else {
            for (Grocery grocery : allGroceries) {
                if (grocery.getCategory() != null && grocery.getCategory().equalsIgnoreCase(selectedCategory)) {
                    filteredGroceries.add(grocery);
                }
            }
        }

        // Set Adapter
        adapter = new GroceryAdapter(requireContext(), filteredGroceries);
        recyclerView.setAdapter(adapter);
    }

    private void setupSwipeGestures() {
        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                Grocery grocery = adapter.getItem(position);

                if (direction == ItemTouchHelper.RIGHT) {
                    grocery.setPurchased(true);
                    groceryDAO.updateGrocery(grocery);
                    Toast.makeText(getContext(), "Marked as Purchased", Toast.LENGTH_SHORT).show();
                } else if (direction == ItemTouchHelper.LEFT) {
                    groceryDAO.deleteGrocery(grocery.getId());
                    Toast.makeText(getContext(), "Item Deleted", Toast.LENGTH_SHORT).show();
                }

                loadGroceries();
            }
        };
        new ItemTouchHelper(simpleCallback).attachToRecyclerView(recyclerView);
    }
}
