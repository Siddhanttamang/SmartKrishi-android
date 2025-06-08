package com.example.smartkrishi.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.smartkrishi.R;
import com.example.smartkrishi.models.Products;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {
    private final List<Products> productList;

    public ProductAdapter(List<Products> productList) {
        this.productList = productList;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_product, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Products p = productList.get(position);

        holder.productName.setText(p.getName());
        holder.productPrice.setText("Rs. " + p.getPrice() + "/" + p.getUnit());
        holder.productSeller.setText(p.getSeller());
        holder.productLocation.setText(p.getLocation());

        Glide.with(holder.itemView.getContext())
                .load(p.getImageUrl())
                .placeholder(R.drawable.ic_launcher_foreground)
                .into(holder.productImage);

        holder.contactButton.setOnClickListener(v -> {
            String phone = p.getContact();
            Context context = v.getContext();

            new AlertDialog.Builder(context)
                    .setTitle("Contact Seller")
                    .setItems(new CharSequence[]{"Call", "WhatsApp", "SMS"}, (dialog, which) -> {
                        switch (which) {
                            case 0: // Call
                                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                                callIntent.setData(Uri.parse("tel:" + phone));
                                context.startActivity(callIntent);
                                break;
                            case 1: // WhatsApp
                                Intent whatsappIntent = new Intent(Intent.ACTION_VIEW);
                                whatsappIntent.setData(Uri.parse("https://wa.me/" + phone));
                                context.startActivity(whatsappIntent);
                                break;
                            case 2: // SMS
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
        return productList.size();
    }

    static class ProductViewHolder extends RecyclerView.ViewHolder {
        ImageView productImage;
        TextView productName, productPrice, productSeller, productLocation;
        Button contactButton;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.productImage);
            productName = itemView.findViewById(R.id.productName);
            productPrice = itemView.findViewById(R.id.productPrice);
            productSeller = itemView.findViewById(R.id.productSeller);
            productLocation = itemView.findViewById(R.id.productLocation);
            contactButton = itemView.findViewById(R.id.contactSellerButton);
        }
    }
}
