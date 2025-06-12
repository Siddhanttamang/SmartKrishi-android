package com.example.smartkrishi.Services;

import android.util.Log;

import com.example.smartkrishi.api.ProductsApi;
import com.example.smartkrishi.api.RetrofitClient;
import com.example.smartkrishi.models.Products;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductsService {
    private static final String TAG = "ProductApi";
    private final ProductsApi productsApi;

    public interface ProductsCallback {
        void onSuccess(List<Products> productsList);
        void onFailure(String errorMessage);
    }

    public ProductsService() {
        productsApi = RetrofitClient.getClient().create(ProductsApi.class);
    }

    public void fetchProducts(ProductsCallback callback) {
        productsApi.getAllProducts().enqueue(new Callback<List<Products>>() {
            @Override
            public void onResponse(Call<List<Products>> call, Response<List<Products>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onFailure("Failed: Invalid response");
                    Log.e(TAG, "Response error: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<List<Products>> call, Throwable t) {
                callback.onFailure("Error: " + t.getMessage());
                Log.e(TAG, "Retrofit error: ", t);
            }
        });
    }
}
