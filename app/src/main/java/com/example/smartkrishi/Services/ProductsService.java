package com.example.smartkrishi.Services;

import android.util.Log;

import com.example.smartkrishi.Responses.ProductsResponse;
import com.example.smartkrishi.api.ProductsApi;
import com.example.smartkrishi.api.RetrofitClient;
import com.example.smartkrishi.Responses.NewsResponse;
import com.example.smartkrishi.models.Products;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductsService {
    private static final String TAG = "ProductApi";
    private final ProductsApi productsApi;

    public interface ProductsCallback {
        void onSuccess(List<Products> ProductsList);
        void onFailure(String errorMessage);
    }

    public ProductsService() {
        productsApi = RetrofitClient.getClient().create(ProductsApi.class);
    }

    public void fetchProducts(ProductsService.ProductsCallback callback) {
        productsApi.getAllProducts().enqueue(new Callback<ProductsResponse>() {
            @Override
            public void onResponse(Call<ProductsResponse> call, Response<ProductsResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body().getData());
                } else {
                    callback.onFailure("Failed: Invalid response");
                    Log.e(TAG, "Error: " + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<ProductsResponse> call, Throwable t) {
                callback.onFailure("Error: " + t.getMessage());
                Log.e(TAG, "Retrofit error: ", t);
            }
        });
    }
}

