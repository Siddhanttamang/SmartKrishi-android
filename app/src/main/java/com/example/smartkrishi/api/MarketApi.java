package com.example.smartkrishi.api;

import com.example.smartkrishi.models.Products;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface MarketApi {
    @GET("api/vegetables/")
    Call<List<Products>> getAllProducts();
    @Multipart
    @POST("api/vegetables/")
    Call<Void> createProduct(
            @Header("Authorization") String token,
            @Part("name") RequestBody name,
            @Part("price") RequestBody price,
            @Part("quantity") RequestBody quantity,
            @Part MultipartBody.Part image
    );

}
