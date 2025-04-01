package com.example.mealmate.models;

public class User {
    private String username;
    private String userImage;
    private String bio;

    public User(String username, String userImage, String bio) {
        this.username = username;
        this.userImage = userImage;
        this.bio = bio;
    }

    // Getters
    public String getUsername() { return username; }
    public String getUserImage() { return userImage; }
    public String getBio() { return bio; }

    // Setters
    public void setUsername(String username) { this.username = username; }
    public void setUserImage(String userImage) { this.userImage = userImage; }
    public void setBio(String bio) { this.bio = bio; }
}
