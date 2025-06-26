package com.example.smartkrishi.api;

import com.example.smartkrishi.models.UserLoginRequest;
import com.example.smartkrishi.Responses.UserLoginResponse;
import com.example.smartkrishi.models.UserRegister;
import com.example.smartkrishi.models.UserUpdateRequest;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.PATCH;
import retrofit2.http.POST;

public interface AuthApi {
    @POST("api/auth/register")
    Call<UserRegister>createUser(@Body UserRegister user);

    @POST("api/auth/login")
    Call<UserLoginResponse> login(@Body UserLoginRequest request);

    @GET("/api/users/me")
    Call<UserLoginResponse.UserData> getCurrentUser(@Header("Authorization") String token);
    @PATCH("/api/users/me")
    Call<UserLoginResponse.UserData> updateUser(
            @Header("Authorization") String token,
            @Body UserUpdateRequest request
    );
    @DELETE("/api/users/me")
    Call<Void> deleteAccount(@Header("Authorization") String token);



}
