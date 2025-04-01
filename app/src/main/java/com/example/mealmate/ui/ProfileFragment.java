package com.example.mealmate.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
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
import com.example.mealmate.database.UserDAO;
import com.example.mealmate.models.User;

import static android.app.Activity.RESULT_OK;

public class ProfileFragment extends Fragment {

    private EditText editUsername, editBio;
    private ImageView profileImageView;
    private Button editProfileButton, saveProfileButton, uploadImageButton;

    private UserDAO userDAO;
    private User currentUser;
    private static final int PICK_IMAGE_REQUEST = 1;

    public ProfileFragment() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        // Initialize Views
        editUsername = view.findViewById(R.id.edit_username);
        editBio = view.findViewById(R.id.edit_bio);
        profileImageView = view.findViewById(R.id.profile_image);
        editProfileButton = view.findViewById(R.id.edit_profile_button);
        saveProfileButton = view.findViewById(R.id.save_profile_button);
        uploadImageButton = view.findViewById(R.id.upload_image_button);

        userDAO = new UserDAO(getContext());

        // Load user data
        loadUserProfile();

        // Edit profile button click listener
        editProfileButton.setOnClickListener(v -> enableEditMode());

        // Save profile button click listener
        saveProfileButton.setOnClickListener(v -> saveProfile());

        // Upload image button click listener
        uploadImageButton.setOnClickListener(v -> openImagePicker());

        return view;
    }

    private void loadUserProfile() {
        // Fetch current user data (just get the first user in the table)
        currentUser = userDAO.getUser("Anusha"); // A new method to get the first user from the database

        if (currentUser == null) {
            // If no user data is found, set default values
            currentUser = new User("Anusha", "", "This is a bio");

            // Optionally, set a default image URI
            Glide.with(getContext()).load(R.drawable.mealmate_logo).into(profileImageView);

            // Save the user data to the database so that it persists
            userDAO.saveUser(currentUser);
        }

        // Set user data in the views
        editUsername.setText(currentUser.getUsername());
        editBio.setText(currentUser.getBio());
        if (!TextUtils.isEmpty(currentUser.getUserImage())) {
            Glide.with(getContext()).load(currentUser.getUserImage()).into(profileImageView);
        } else {
            Glide.with(getContext()).load(R.drawable.mealmate_logo).into(profileImageView); // Default image if none exists
        }

        // Initially make the fields non-editable
        editUsername.setEnabled(false);
        editBio.setEnabled(false);
    }


    private void enableEditMode() {
        // Enable editing for username and bio fields
        editUsername.setEnabled(true);
        editBio.setEnabled(true);
        uploadImageButton.setVisibility(View.VISIBLE); // Show upload image button
        saveProfileButton.setVisibility(View.VISIBLE); // Show save profile button

        // Hide edit profile button
        editProfileButton.setVisibility(View.GONE);
    }

    private void saveProfile() {
        String updatedUsername = editUsername.getText().toString().trim();
        String updatedBio = editBio.getText().toString().trim();

        if (TextUtils.isEmpty(updatedUsername) || TextUtils.isEmpty(updatedBio)) {
            Toast.makeText(getContext(), "Username and Bio cannot be empty", Toast.LENGTH_SHORT).show();
            return;
        }

        if (currentUser != null) {
            currentUser.setUsername(updatedUsername);
            currentUser.setBio(updatedBio);

            // Save profile using the username as the identifier
            userDAO.saveUser(currentUser);

            editUsername.setEnabled(false);
            editBio.setEnabled(false);
            uploadImageButton.setVisibility(View.GONE);
            saveProfileButton.setVisibility(View.GONE);
            editProfileButton.setVisibility(View.VISIBLE);

            Toast.makeText(getContext(), "Profile updated successfully", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getContext(), "Error: User not found", Toast.LENGTH_SHORT).show();
        }
    }


    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            String imageUri = data.getData().toString();

            // Set image in ImageView
            Glide.with(this).load(imageUri).into(profileImageView);

            // Update currentUser object
            if (currentUser != null) {
                currentUser.setUserImage(imageUri);
                userDAO.saveUser(currentUser); // Save image URL to database
            }
        }
    }

}
