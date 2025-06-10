package com.example.smartkrishi.api;

import com.example.smartkrishi.Responses.ProductsResponse;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ProductsApi {
    @GET("api/vegetables")
    Call<ProductsResponse> getAllProducts();

}
