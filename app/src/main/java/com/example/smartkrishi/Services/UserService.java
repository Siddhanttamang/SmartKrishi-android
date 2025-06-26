package com.example.smartkrishi.Services;

import com.example.smartkrishi.api.AuthApi;
import com.example.smartkrishi.api.RetrofitClient;
import com.example.smartkrishi.Responses.UserLoginResponse;
import com.example.smartkrishi.models.UserUpdateRequest;

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
    public void updateUser(String token, UserUpdateRequest request, UserCallback callback) {
        authApi.updateUser("Bearer " + token, request).enqueue(new Callback<UserLoginResponse.UserData>() {
            @Override
            public void onResponse(Call<UserLoginResponse.UserData> call, Response<UserLoginResponse.UserData> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onError("Failed to update user");
                }
            }

            @Override
            public void onFailure(Call<UserLoginResponse.UserData> call, Throwable t) {
                callback.onError("Network error: " + t.getMessage());
            }
        });
    }
    public void deleteAccount(String token, DeletionCallback callback) {
        authApi.deleteAccount("Bearer " + token).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess();
                } else {
                    callback.onError("Account deletion failed");
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                callback.onError("Network error: " + t.getMessage());
            }
        });
    }

    public interface DeletionCallback {
        void onSuccess();
        void onError(String message);
    }



    public interface UserCallback {
        void onSuccess(UserLoginResponse.UserData user);

        void onError(String message);
    }
}
