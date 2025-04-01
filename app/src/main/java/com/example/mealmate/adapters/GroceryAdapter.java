package com.example.mealmate.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.mealmate.R;
import com.example.mealmate.models.Grocery;
import com.example.mealmate.ui.UpdateGroceryFragment;

import java.util.List;

public class GroceryAdapter extends RecyclerView.Adapter<GroceryAdapter.GroceryViewHolder> {

    private Context context;
    private List<Grocery> groceryList;

    public GroceryAdapter(Context context, List<Grocery> groceryList) {
        this.context = context;
        this.groceryList = groceryList;
    }

    @NonNull
    @Override
    public GroceryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_grocery, parent, false);
        return new GroceryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GroceryViewHolder holder, int position) {
        Grocery grocery = groceryList.get(position);

        holder.itemName.setText(grocery.getItem());
        holder.itemPrice.setText("Price: $" + grocery.getPrice());
        holder.itemQuantity.setText("Qty: " + grocery.getQuantity());
        holder.itemCategory.setText("Category: " + grocery.getCategory());

        // Show store location if available
        if (grocery.getStoreLocation() != null && !grocery.getStoreLocation().isEmpty()) {
            holder.storeLocation.setText("Store: " + grocery.getStoreLocation());
        } else {
            holder.storeLocation.setText("Store: Not Set");
        }

        // Show Purchased Status
        if (grocery.isPurchased()) {
            holder.purchasedStatus.setImageResource(R.drawable.baseline_check_circle_24);
        } else {
            holder.purchasedStatus.setImageResource(R.drawable.baseline_radio_button_unchecked_24);
        }

        // Load Image using Glide
        if (grocery.getItemImage() != null && !grocery.getItemImage().isEmpty()) {
            Glide.with(holder.itemView.getContext())
                    .load(grocery.getItemImage())
                    .placeholder(R.drawable.mealmate_logo)
                    .into(holder.itemImage);
        } else {
            holder.itemImage.setImageResource(R.drawable.mealmate_logo);
        }

        // **Edit Button: Open UpdateGroceryFragment**
        holder.editButton.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putSerializable("grocery", grocery);

            UpdateGroceryFragment updateFragment = new UpdateGroceryFragment();
            updateFragment.setArguments(bundle);

            // Open UpdateGroceryFragment
            ((FragmentActivity) context).getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, updateFragment)
                    .addToBackStack(null)
                    .commit();
        });

        // **Share Button: Share Grocery Item Details**
        holder.shareButton.setOnClickListener(v -> {
            String shareText = "Grocery Item: " + grocery.getItem() +
                    "\nCategory: " + grocery.getCategory() +
                    "\nPrice: $" + grocery.getPrice() +
                    "\nQuantity: " + grocery.getQuantity() +
                    "\nStore: " + (grocery.getStoreLocation().isEmpty() ? "Not Set" : grocery.getStoreLocation());

            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareText);
            context.startActivity(Intent.createChooser(shareIntent, "Share Grocery Item"));
        });
    }

    @Override
    public int getItemCount() {
        return groceryList.size();
    }

    public Grocery getItem(int position) {
        return groceryList.get(position);
    }

    static class GroceryViewHolder extends RecyclerView.ViewHolder {
        public ImageView itemImage;
        TextView itemName, itemPrice, itemQuantity, itemCategory, storeLocation;
        ImageView purchasedStatus;
        Button editButton, shareButton;

        GroceryViewHolder(View itemView) {
            super(itemView);
            itemName = itemView.findViewById(R.id.itemName);
            itemPrice = itemView.findViewById(R.id.itemPrice);
            itemQuantity = itemView.findViewById(R.id.itemQuantity);
            itemCategory = itemView.findViewById(R.id.itemCategory);
            storeLocation = itemView.findViewById(R.id.storeLocation);
            purchasedStatus = itemView.findViewById(R.id.purchasedStatus);
            itemImage = itemView.findViewById(R.id.itemImage);
            editButton = itemView.findViewById(R.id.edit_button);
            shareButton = itemView.findViewById(R.id.share_button);
        }
    }
}
