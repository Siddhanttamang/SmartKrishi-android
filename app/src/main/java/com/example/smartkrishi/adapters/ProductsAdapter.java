package com.example.smartkrishi.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.bumptech.glide.Glide;
import com.example.smartkrishi.R;
import com.example.smartkrishi.models.Products;

import java.util.ArrayList;
import java.util.List;

public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.ProductViewHolder> {
    private final List<Products> originalList;
    private List<Products> filteredList;

    public ProductsAdapter(List<Products> productList) {
        this.originalList = productList;
        this.filteredList = new ArrayList<>(productList);
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_products, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Products p = filteredList.get(position);
        holder.productName.setText(p.getName());
        holder.productPrice.setText("Rs. " + p.getPrice() + "/ KG");
        holder.productQuantity.setText("Quantity: " + p.getQuantity() + " KG");
        holder.productSeller.setText("Seller Name: " + p.getUser_name());
        holder.productLocation.setText("Seller Address: " + p.getUser_address());

        Glide.with(holder.itemView.getContext())
                .load(p.getImage_url())
                .placeholder(R.drawable.ic_launcher_foreground)
                .into(holder.productImage);

        holder.contactButton.setOnClickListener(v -> {
            String phone = p.getUser_contact();
            Context context = v.getContext();
            new AlertDialog.Builder(context)
                    .setTitle("Contact Seller")
                    .setItems(new CharSequence[]{"Call", "WhatsApp", "SMS"}, (dialog, which) -> {
                        switch (which) {
                            case 0:
                                context.startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phone)));
                                break;
                            case 1:
                                context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://wa.me/" + phone)));
                                break;
                            case 2:
                                Intent smsIntent = new Intent(Intent.ACTION_VIEW, Uri.fromParts("sms", phone, null));
                                smsIntent.putExtra("sms_body", "Hi! I'm interested in your product.");
                                context.startActivity(smsIntent);
                                break;
                        }
                    })
                    .setNegativeButton("Cancel", null)
                    .show();
        });
    }

    @Override
    public int getItemCount() {
        return filteredList.size();
    }

    public void filter(String query) {
        filteredList = new ArrayList<>();
        if (query == null || query.trim().isEmpty()) {
            filteredList.addAll(originalList);
        } else {
            String lowerQuery = query.toLowerCase();
            for (Products p : originalList) {
                if (p.getName().toLowerCase().contains(lowerQuery) ||
                        p.getUser_name().toLowerCase().contains(lowerQuery) ||
                        p.getUser_address().toLowerCase().contains(lowerQuery)) {
                    filteredList.add(p);
                }
            }
        }
        notifyDataSetChanged();
    }

    static class ProductViewHolder extends RecyclerView.ViewHolder {
        ImageView productImage;
        TextView productName, productPrice, productSeller, productLocation, productQuantity;
        Button contactButton;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.productImage);
            productName = itemView.findViewById(R.id.productName);
            productPrice = itemView.findViewById(R.id.productPrice);
            productQuantity = itemView.findViewById(R.id.productQuantity);
            productSeller = itemView.findViewById(R.id.productSeller);
            productLocation = itemView.findViewById(R.id.productLocation);
            contactButton = itemView.findViewById(R.id.contactSellerButton);
        }
    }
}
