package com.example.mealmate.models;

import java.io.Serializable;

public class Grocery  implements Serializable {
    private int id;
    private String item;
    private double price;
    private int quantity;
    private String category;
    private String itemImage;
    private boolean purchased;
    private String storeLocation;

    public Grocery(int id, String item, double price, int quantity, String category,
                   String itemImage, boolean purchased, String storeLocation) {
        this.id = id;
        this.item = item;
        this.price = price;
        this.quantity = quantity;
        this.category = category;
        this.itemImage = itemImage;
        this.purchased = purchased;
        this.storeLocation = storeLocation;
    }

    // Getters
    public int getId() { return id; }
    public String getItem() { return item; }
    public double getPrice() { return price; }
    public int getQuantity() { return quantity; }
    public String getCategory() { return category; }
    public String getItemImage() { return itemImage; }
    public boolean isPurchased() { return purchased; }
    public String getStoreLocation() { return storeLocation; }

    // Setters
    public void setId(int id) { this.id = id; }
    public void setItem(String item) { this.item = item; }
    public void setPrice(double price) { this.price = price; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public void setCategory(String category) { this.category = category; }
    public void setItemImage(String itemImage) { this.itemImage = itemImage; }
    public void setPurchased(boolean purchased) { this.purchased = purchased; }
    public void setStoreLocation(String storeLocation) { this.storeLocation = storeLocation; }
}
