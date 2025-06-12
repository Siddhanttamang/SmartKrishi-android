package com.example.smartkrishi.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartkrishi.R;
import com.example.smartkrishi.Services.ProductsService;
import com.example.smartkrishi.adapters.ProductsAdapter;
import com.example.smartkrishi.models.Products;

import java.util.List;

public class MarketFragment extends Fragment {

    private RecyclerView productsRecyclerView;
    private ProductsAdapter productsAdapter;
    private View productsLoading;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.products, container, false);

        productsRecyclerView = view.findViewById(R.id.productsRecyclerView);
        productsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        productsLoading = view.findViewById(R.id.productsLoading);

        // Show loading initially
        productsLoading.setVisibility(View.VISIBLE);
        productsRecyclerView.setVisibility(View.GONE);

        // Fetch products from API
        ProductsService productsService = new ProductsService();
        productsService.fetchProducts(new ProductsService.ProductsCallback() {
            @Override
            public void onSuccess(List<Products> productsList) {
                // Only update UI if fragment is attached to activity
                if (isAdded() && getContext() != null) {
                    productsAdapter = new ProductsAdapter(productsList);
                    productsRecyclerView.setAdapter(productsAdapter);

                    productsLoading.setVisibility(View.GONE);
                    productsRecyclerView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(String errorMessage) {
                if (isAdded() && getContext() != null) {
                    Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show();
                    productsLoading.setVisibility(View.GONE);
                }
            }
        });

        return view;
    }
}
