package com.example.mealmate.ui;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.mealmate.R;
import com.example.mealmate.database.MealDAO;
import com.example.mealmate.models.Meal;

import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends Fragment {

    private RecyclerView searchRecyclerView;
    private EditText searchEditText;
    private SearchAdapter searchAdapter;
    private List<Meal> mealList = new ArrayList<>();
    private MealDAO mealDAO;

    private static final int REQUEST_PERMISSIONS = 100;

    public SearchFragment() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        searchRecyclerView = view.findViewById(R.id.search_results_list);
        searchEditText = view.findViewById(R.id.search_bar);
        mealDAO = new MealDAO(getContext());

        // Setup RecyclerView
        searchRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        searchAdapter = new SearchAdapter(getContext(), mealList);
        searchRecyclerView.setAdapter(searchAdapter);

        // Listen to text changes in the search box
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int after) {
                filterMeals(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });

        // Check and request permissions
        if (checkPermissions()) {
            // Load all meals initially if permissions are granted
            loadAllMeals();
        } else {
            requestPermissions();
        }

        return view;
    }

    private boolean checkPermissions() {
        return ContextCompat.checkSelfPermission(getContext(), android.Manifest.permission.INTERNET) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(getContext(), android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(getActivity(),
                new String[]{android.Manifest.permission.INTERNET, android.Manifest.permission.READ_EXTERNAL_STORAGE},
                REQUEST_PERMISSIONS);
    }

    private void loadAllMeals() {
        mealList.clear();
        mealList.addAll(mealDAO.getAllMeals());
        searchAdapter.notifyDataSetChanged();
    }

    private void filterMeals(String query) {
        List<Meal> filteredMeals = new ArrayList<>();
        for (Meal meal : mealDAO.getAllMeals()) {
            if (meal.getMealName().toLowerCase().contains(query.toLowerCase())) {
                filteredMeals.add(meal);
            }
        }
        mealList.clear();
        mealList.addAll(filteredMeals);
        searchAdapter.notifyDataSetChanged();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_PERMISSIONS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // If permissions are granted, load the meals
                loadAllMeals();
            } else {
                // Permissions not granted, show a message
                Toast.makeText(getContext(), "Permissions are required to load meal images.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // Adapter for RecyclerView
    public static class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.SearchViewHolder> {

        private Context context;
        private List<Meal> mealList;

        public SearchAdapter(Context context, List<Meal> mealList) {
            this.context = context;
            this.mealList = mealList;
        }

        @NonNull
        @Override
        public SearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.search_item, parent, false);
            return new SearchViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull SearchViewHolder holder, int position) {
            Meal meal = mealList.get(position);

            // Set meal name
            holder.mealNameTextView.setText(meal.getMealName());
            // Set meal category
            holder.mealCategoryTextView.setText(meal.getCategory());

            // Load image with Glide
            String mealImageUrl = meal.getMealImage();
            if (mealImageUrl != null && !mealImageUrl.isEmpty()) {
                Glide.with(context)
                        .load(mealImageUrl)
                        .into(holder.mealImageView);
            } else {
                holder.mealImageView.setImageResource(R.drawable.mealmate_logo); // Placeholder
            }

            // Handle item click to open meal details
            holder.itemView.setOnClickListener(v -> {
                // Create a bundle to pass the meal object to the MealDetailsFragment
                Bundle bundle = new Bundle();
                bundle.putSerializable("meal", meal); // Pass the meal object

                // Use FragmentTransaction to replace the current fragment with the MealDetailsFragment
                MealDetailsFragment mealDetailsFragment = new MealDetailsFragment();
                mealDetailsFragment.setArguments(bundle); // Set the bundle to the fragment

                // Check if the context is an instance of FragmentActivity (to safely use FragmentManager)
                if (context instanceof FragmentActivity) {
                    ((FragmentActivity) context).getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container, mealDetailsFragment) // Replace the container with MealDetailsFragment
                            .addToBackStack(null) // Add the transaction to the back stack
                            .commit();
                }
            });
        }


        @Override
        public int getItemCount() {
            return mealList.size();
        }

        public static class SearchViewHolder extends RecyclerView.ViewHolder {

            ImageView mealImageView;
            TextView mealNameTextView;
            TextView mealCategoryTextView;

            public SearchViewHolder(View itemView) {
                super(itemView);
                mealImageView = itemView.findViewById(R.id.search_meal_image);
                mealNameTextView = itemView.findViewById(R.id.search_meal_name);
                mealCategoryTextView = itemView.findViewById(R.id.search_meal_category);
            }
        }
    }
}
