package com.example.smartkrishi.Services;

import android.content.Context;

import com.example.smartkrishi.api.AuthApi;
import com.example.smartkrishi.api.RetrofitClient;
import com.example.smartkrishi.models.UserLoginRequest;
import com.example.smartkrishi.Responses.UserLoginResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginService {
    private final AuthApi authApi;

    public LoginService(Context context) {
        authApi = RetrofitClient.getClient().create(AuthApi.class);
    }

    public void login(String email, String password, LoginCallback callback) {
        UserLoginRequest request = new UserLoginRequest(email, password);

        authApi.login(request).enqueue(new Callback<UserLoginResponse>() {
            @Override
            public void onResponse(Call<UserLoginResponse> call, Response<UserLoginResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body().getAccessToken());

                } else {
                    callback.onError("Invalid email or password");
                }
            }

            @Override
            public void onFailure(Call<UserLoginResponse> call, Throwable t) {
                callback.onError("Network error: " + t.getMessage());
            }
        });
    }

    public interface LoginCallback {
        void onSuccess(String token);

        void onError(String message);
    }
}
