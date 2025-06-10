package com.example.smartkrishi.Services;

import com.example.smartkrishi.api.AuthApi;
import com.example.smartkrishi.api.RetrofitClient;
import com.example.smartkrishi.Responses.UserLoginResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserService {
    private final AuthApi authApi;

    public UserService() {
        this.authApi = RetrofitClient.getClient().create(AuthApi.class);
    }

    public void getCurrentUser(String token, UserCallback callback) {
        authApi.getCurrentUser("Bearer " + token).enqueue(new Callback<UserLoginResponse.UserData>() {
            @Override
            public void onResponse(Call<UserLoginResponse.UserData> call, Response<UserLoginResponse.UserData> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onError("Failed to fetch user info");
                }
            }

            @Override
            public void onFailure(Call<UserLoginResponse.UserData> call, Throwable t) {
                callback.onError("Network error: " + t.getMessage());
            }
        });
    }

    public interface UserCallback {
        void onSuccess(UserLoginResponse.UserData user);

        void onError(String message);
    }
}
