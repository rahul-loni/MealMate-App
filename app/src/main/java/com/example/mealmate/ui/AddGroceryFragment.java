package com.example.mealmate.ui;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.mealmate.R;
import com.example.mealmate.database.GroceryDAO;
import com.example.mealmate.models.Grocery;

public class AddGroceryFragment extends Fragment {

    private static final int PICK_IMAGE_REQUEST = 1;

    private EditText itemName, itemPrice, itemQuantity, itemCategory, storeLocation;
    private ImageView imagePreview;
    private Button btnSelectImage, btnSave;
    private GroceryDAO groceryDAO;
    private Uri selectedImageUri; // Store image URI

    public AddGroceryFragment() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_grocery, container, false);

        groceryDAO = new GroceryDAO(requireContext());

        itemName = view.findViewById(R.id.editItemName);
        itemPrice = view.findViewById(R.id.editItemPrice);
        itemQuantity = view.findViewById(R.id.editItemQuantity);
        itemCategory = view.findViewById(R.id.editItemCategory);
        storeLocation = view.findViewById(R.id.editStoreLocation);
        imagePreview = view.findViewById(R.id.imagePreview);
        btnSelectImage = view.findViewById(R.id.btnSelectImage);
        btnSave = view.findViewById(R.id.btnSave);

        // Image selection
        btnSelectImage.setOnClickListener(v -> openImageChooser());

        // Save grocery item
        btnSave.setOnClickListener(v -> saveGroceryItem());

        return view;
    }

    private void openImageChooser() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            selectedImageUri = data.getData();
            Glide.with(this).load(selectedImageUri).into(imagePreview);
        }
    }

    private void saveGroceryItem() {
        Grocery grocery = new Grocery(0,
                itemName.getText().toString(),
                Double.parseDouble(itemPrice.getText().toString()),
                Integer.parseInt(itemQuantity.getText().toString()),
                itemCategory.getText().toString(),
                selectedImageUri != null ? selectedImageUri.toString() : null, // Store image URI
                false,
                storeLocation.getText().toString());

        groceryDAO.addGrocery(grocery);
        Toast.makeText(getContext(), "Item Added", Toast.LENGTH_SHORT).show();


    }
}
