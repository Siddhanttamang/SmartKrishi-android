package com.example.smartkrishi.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.smartkrishi.R;
import com.example.smartkrishi.Services.NewsService;
import com.example.smartkrishi.Services.ProductsService;
import com.example.smartkrishi.adapters.NewsAdapter;
import com.example.smartkrishi.adapters.ProductsAdapter;
import com.example.smartkrishi.models.News;
import com.example.smartkrishi.models.Products;

import java.util.List;

public class MarketFragment extends Fragment {

    private RecyclerView productsRecyclerView;
    private ProductsAdapter productsAdapter;
    private View productsLoading; // Can be ProgressBar or LottieAnimationView

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.products, container, false);

        productsRecyclerView = view.findViewById(R.id.productsRecyclerView);
        productsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        productsLoading = view.findViewById(R.id.productsLoading); // 👈 initialize the loader view

        // Show loading
        productsLoading.setVisibility(View.VISIBLE);
        productsRecyclerView.setVisibility(View.GONE);

        ProductsService productsService = new ProductsService();
        productsService.fetchProducts(new ProductsService.ProductsCallback() {
            @Override
            public void onSuccess(List<Products> productsList) {
                productsAdapter = new ProductsAdapter(productsList);
                productsRecyclerView.setAdapter(productsAdapter);

                // Hide loading
                productsLoading.setVisibility(View.GONE);
                productsRecyclerView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onFailure(String errorMessage) {
                Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show();

                // Hide loading (in case of failure too)
                productsLoading.setVisibility(View.GONE);
            }
        });

        return view;
    }
}
