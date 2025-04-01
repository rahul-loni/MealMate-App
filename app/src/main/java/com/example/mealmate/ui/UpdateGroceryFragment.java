package com.example.mealmate.ui;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.mealmate.R;
import com.example.mealmate.database.GroceryDAO;
import com.example.mealmate.models.Grocery;

public class UpdateGroceryFragment extends Fragment {

    private EditText itemNameEditText, priceEditText, quantityEditText, storeLocationEditText;
    private EditText categoryEditText;

    private Button updateButton;
    private GroceryDAO groceryDAO;
    private Grocery grocery;


    public UpdateGroceryFragment() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_update_grocery, container, false);

        // Initialize Views
        itemNameEditText = view.findViewById(R.id.edit_item_name);
        priceEditText = view.findViewById(R.id.edit_price);
        quantityEditText = view.findViewById(R.id.edit_quantity);
        categoryEditText = view.findViewById(R.id.edit_category_spinner);
        updateButton = view.findViewById(R.id.update_grocery_button);
        storeLocationEditText = view.findViewById(R.id.edit_store_location);

        groceryDAO = new GroceryDAO(getContext());

        // Get the grocery item from arguments
        if (getArguments() != null && getArguments().getSerializable("grocery") instanceof Grocery) {
            grocery = (Grocery) getArguments().getSerializable("grocery");

            // Pre-fill fields
            assert grocery != null;
            itemNameEditText.setText(grocery.getItem());
            priceEditText.setText(String.valueOf(grocery.getPrice()));
            quantityEditText.setText(String.valueOf(grocery.getQuantity()));
            categoryEditText.setText(grocery.getCategory());
            storeLocationEditText.setText(grocery.getStoreLocation());

        }


        updateButton.setOnClickListener(v -> updateGroceryItem());

        return view;
    }

    private void updateGroceryItem() {
        String updatedName = itemNameEditText.getText().toString().trim();
        String updatedPrice = priceEditText.getText().toString().trim();
        String updatedQuantity = quantityEditText.getText().toString().trim();
        String updatedCategory = categoryEditText.getText().toString();
        String updatedStoreLocation = storeLocationEditText.getText().toString();

        if (TextUtils.isEmpty(updatedName)) {
            itemNameEditText.setError("Item name is required");
            return;
        }

        int price, quantity;
        try {
            price = Integer.parseInt(updatedPrice);
            quantity = Integer.parseInt(updatedQuantity);
        } catch (NumberFormatException e) {
            Toast.makeText(getContext(), "Enter valid numbers for price and quantity", Toast.LENGTH_SHORT).show();
            return;
        }

        grocery.setItem(updatedName);
        grocery.setPrice(price);
        grocery.setQuantity(quantity);
        grocery.setCategory(updatedCategory);
        grocery.setStoreLocation(updatedStoreLocation);

        groceryDAO.updateGrocery(grocery);
        Toast.makeText(getContext(), "Grocery Item Updated!", Toast.LENGTH_SHORT).show();

    }
}
