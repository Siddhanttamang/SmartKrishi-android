package com.example.smartkrishi.Services;

import android.content.Context;

import com.example.smartkrishi.api.AuthApi;
import com.example.smartkrishi.api.RetrofitClient;
import com.example.smartkrishi.models.UserRegister;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterService {
    private static final String TAG = "AuthService";

    private final AuthApi authApi;
    public RegisterService(Context context){
        authApi = RetrofitClient.getClient().create(AuthApi.class);

    }
    public void registerUser(UserRegister user, AuthCallback callback) {
        Call<UserRegister> call = authApi.createUser(user);
        call.enqueue(new Callback<UserRegister>() {
            @Override
            public void onResponse(Call<UserRegister> call, Response<UserRegister> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess("User registered successfully.");
                } else {
                    callback.onError("Registration failed. Error: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<UserRegister> call, Throwable t) {
                callback.onError("Network error: " + t.getMessage());
            }
        });
    }

    public interface AuthCallback {
        void onSuccess(String message);
        void onError(String error);
    }
}
