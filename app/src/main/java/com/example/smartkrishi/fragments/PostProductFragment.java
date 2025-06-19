package com.example.smartkrishi.fragments;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.smartkrishi.R;
import com.example.smartkrishi.api.MarketApi;
import com.example.smartkrishi.api.RetrofitClient;
import com.example.smartkrishi.utils.FileUtils;

import java.io.File;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PostProductFragment extends Fragment {

    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri selectedImageUri;

    private EditText nameInput, priceInput, quantityInput;
    private ImageView selectedImageView;
    private Button selectImageButton, postButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.post_product, container, false);

        nameInput = view.findViewById(R.id.nameInput);
        priceInput = view.findViewById(R.id.priceInput);
        quantityInput = view.findViewById(R.id.quantityInput);

        selectedImageView = view.findViewById(R.id.selectedImageView);
        selectImageButton = view.findViewById(R.id.selectImageButton);
        postButton = view.findViewById(R.id.postButton);

        selectImageButton.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, PICK_IMAGE_REQUEST);
        });

        postButton.setOnClickListener(v -> postProduct());

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            selectedImageUri = data.getData();
            selectedImageView.setImageURI(selectedImageUri);
            selectedImageView.setVisibility(View.VISIBLE);
        }
    }

    private void postProduct() {
        String name = nameInput.getText().toString().trim();
        String price = priceInput.getText().toString().trim();
        String quantity = quantityInput.getText().toString().trim();


        if (name.isEmpty() || price.isEmpty() || quantity.isEmpty() ||
                selectedImageUri == null) {
            Toast.makeText(getContext(), "Fill all fields and select image", Toast.LENGTH_SHORT).show();
            return;
        }

        SharedPreferences preferences = requireContext().getSharedPreferences("app_prefs", Activity.MODE_PRIVATE);
        String token = preferences.getString("auth_token", null);

        if (token == null) {
            Toast.makeText(getContext(), "User not logged in", Toast.LENGTH_SHORT).show();
            return;
        }

        File imageFile = FileUtils.getFileFromUri(requireContext(), selectedImageUri);
        if (imageFile == null) {
            Toast.makeText(getContext(), "Invalid image", Toast.LENGTH_SHORT).show();
            return;
        }

        RequestBody imageRequest = RequestBody.create(MediaType.parse("image/*"), imageFile);
        MultipartBody.Part imagePart = MultipartBody.Part.createFormData("image", imageFile.getName(), imageRequest);

        RequestBody namePart = RequestBody.create(MediaType.parse("text/plain"), name);
        RequestBody pricePart = RequestBody.create(MediaType.parse("text/plain"), price);
        RequestBody quantityPart = RequestBody.create(MediaType.parse("text/plain"), quantity);

        MarketApi api = RetrofitClient.getClient().create(MarketApi.class);
        Call<Void> call = api.createProduct("Bearer " + token,
                namePart, pricePart, quantityPart, imagePart);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getContext(), "Product posted successfully", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        String error = response.errorBody() != null ? response.errorBody().string() : "No error body";
                        Log.e("PostProduct", "Failed: " + response.code() + ", " + error);
                        Toast.makeText(getContext(), "Failed to post: " + response.code(), Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("PostProduct", "onFailure: " + t.getMessage());
                Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void clearForm() {
        nameInput.setText("");
        priceInput.setText("");
        quantityInput.setText("");

        selectedImageView.setImageDrawable(null);
        selectedImageView.setVisibility(View.GONE);
        selectedImageUri = null;
    }
}
