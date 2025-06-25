package com.example.smartkrishi.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.smartkrishi.R;
import com.example.smartkrishi.api.MarketApi;
import com.example.smartkrishi.api.RetrofitClient;
import com.example.smartkrishi.models.Products;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditProductFragment extends Fragment {
    private EditText nameInput, priceInput, quantityInput;
    private Button updateButton, deleteButton;
    private Products product;

    public static EditProductFragment newInstance(Products product) {
        EditProductFragment fragment = new EditProductFragment();
        Bundle args = new Bundle();
        args.putSerializable("product", product);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            product = (Products) getArguments().getSerializable("product");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.edit_product_fragment, container, false);

        nameInput = view.findViewById(R.id.nameInput);
        priceInput = view.findViewById(R.id.priceInput);
        quantityInput = view.findViewById(R.id.quantityInput);
        updateButton = view.findViewById(R.id.updateButton);
        deleteButton = view.findViewById(R.id.deleteButton);

        nameInput.setText(product.getName());
        priceInput.setText(String.valueOf(product.getPrice()));
        quantityInput.setText(String.valueOf(product.getQuantity()));

        SharedPreferences preferences = requireContext().getSharedPreferences("app_prefs", Context.MODE_PRIVATE);
        String token = preferences.getString("auth_token", null);

        updateButton.setOnClickListener(v -> {
            String name = nameInput.getText().toString().trim();
            String price = priceInput.getText().toString().trim();
            String quantity = quantityInput.getText().toString().trim();

            if (name.isEmpty() || price.isEmpty() || quantity.isEmpty()) {
                Toast.makeText(getContext(), "All fields are required", Toast.LENGTH_SHORT).show();
                return;
            }

            Map<String, String> updates = new HashMap<>();
            updates.put("name", name);
            updates.put("price", price);
            updates.put("quantity", quantity);

            MarketApi api = RetrofitClient.getClient().create(MarketApi.class);
            api.updateProduct("Bearer " + token, product.getId(), updates).enqueue(new Callback<Products>() {
                @Override
                public void onResponse(Call<Products> call, Response<Products> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(getContext(), "Product updated", Toast.LENGTH_SHORT).show();
                        requireActivity().onBackPressed();
                    } else {
                        Toast.makeText(getContext(), "Update failed", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Products> call, Throwable t) {
                    Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });

        deleteButton.setOnClickListener(v -> {
            MarketApi api = RetrofitClient.getClient().create(MarketApi.class);
            api.deleteProduct("Bearer " + token, product.getId()).enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(getContext(), "Product deleted", Toast.LENGTH_SHORT).show();
                        requireActivity().onBackPressed();
                    } else {
                        Toast.makeText(getContext(), "Delete failed", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });

        return view;
    }
}
