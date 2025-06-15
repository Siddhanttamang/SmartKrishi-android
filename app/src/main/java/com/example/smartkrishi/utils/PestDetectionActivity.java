package com.example.smartkrishi.utils;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.smartkrishi.R;
import com.example.smartkrishi.api.ReportsAPi;
import com.example.smartkrishi.api.RetrofitClient;
import com.example.smartkrishi.ml.ONNXClassifier;
import com.example.smartkrishi.models.Recommendation;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PestDetectionActivity extends Activity {

    private ImageView imagePreview;
    private TextView cropText;
    private TextView diseaseText;
    private TextView recommendationText;
    private Button btnSave;
    private Button btnCancel;

    private Bitmap currentBitmap = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pest_detection);

        // Initialize UI components
        imagePreview = findViewById(R.id.image_preview);
        cropText = findViewById(R.id.crop);
        diseaseText = findViewById(R.id.disease);
        recommendationText = findViewById(R.id.recommendation);
        btnSave = findViewById(R.id.btnSave);
        btnCancel = findViewById(R.id.btnCancel);

        btnCancel.setOnClickListener(e -> finish());

        try {
            boolean imageHandled = false;

            // Handle image path or URI string
            String imagePathOrUriString = getIntent().getStringExtra("image_path");
            if (imagePathOrUriString != null) {
                Bitmap bitmap = loadBitmapFromPathOrUri(imagePathOrUriString);
                if (bitmap != null) {
                    imagePreview.setImageBitmap(bitmap);
                    currentBitmap = bitmap;
                    classify(bitmap);
                    imageHandled = true;
                }
            }

            // Handle byte array image (optional fallback)
            if (!imageHandled) {
                byte[] byteArray = getIntent().getByteArrayExtra("image");
                if (byteArray != null) {
                    Bitmap bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
                    imagePreview.setImageBitmap(bitmap);
                    currentBitmap = bitmap;
                    classify(bitmap);
                    imageHandled = true;
                }
            }

            if (!imageHandled) {
                recommendationText.setText("No image received");
            }

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Failed to load image", Toast.LENGTH_SHORT).show();
        }
    }

    private Bitmap loadBitmapFromPathOrUri(String pathOrUri) {
        try {
            Uri uri = Uri.parse(pathOrUri);

            if ("file".equals(uri.getScheme()) || uri.getScheme() == null) {
                // This is a file path
                return BitmapFactory.decodeFile(pathOrUri);
            } else {
                // This is a content Uri (gallery or Android 14+ selection)
                InputStream inputStream = getContentResolver().openInputStream(uri);
                if (inputStream != null) {
                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                    inputStream.close();
                    return bitmap;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Error loading image: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        return null;
    }

    private void classify(Bitmap bitmap) {
        new Thread(() -> {
            try {
                ONNXClassifier classifier = new ONNXClassifier(this);
                String prediction = classifier.classify(bitmap);

                runOnUiThread(() -> {
                    Recommendation r = getRecommendation(prediction);
                    cropText.setText("Crop: " + r.crop);
                    diseaseText.setText("Disease: " + r.disease);
                    recommendationText.setText("Recommendation:\n" + r.recommendation);

                    if (r.crop != null && r.disease != null && r.recommendation != null) {
                        btnSave.setOnClickListener(e -> saveReport(r));
                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
                runOnUiThread(() -> {
                    recommendationText.setText("Error: " + e.getMessage());
                    Toast.makeText(this, "Inference failed", Toast.LENGTH_SHORT).show();
                });
            }
        }).start();
    }

    private void saveReport(Recommendation r) {
        SharedPreferences preferences = getSharedPreferences("app_prefs", MODE_PRIVATE);
        String token = preferences.getString("auth_token", null);

        if (token != null) {
            ReportsAPi reportsApi = RetrofitClient.getClient().create(ReportsAPi.class);
            Call<Void> call = reportsApi.createReport("Bearer " + token, r);

            call.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(PestDetectionActivity.this, "Report saved successfully", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(PestDetectionActivity.this, "Failed to save: " + response.code(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Toast.makeText(PestDetectionActivity.this, "Network error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        } else {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show();
        }
    }

    private Recommendation getRecommendation(String predictedLabel) {
        try {
            String json = loadJSONFromAsset("class_recommendations.json");
            Gson gson = new Gson();
            Type listType = new TypeToken<List<Recommendation>>() {}.getType();
            List<Recommendation> list = gson.fromJson(json, listType);

            if (predictedLabel.contains(" (")) {
                predictedLabel = predictedLabel.substring(0, predictedLabel.indexOf(" ("));
            }

            String[] parts = predictedLabel.split("_", 2);
            String crop = parts[0].trim().toLowerCase();
            String disease = parts[1].trim().replace("_", " ").toLowerCase();

            for (Recommendation r : list) {
                if (r.crop.trim().toLowerCase().equals(crop) &&
                        r.disease.trim().toLowerCase().equals(disease)) {
                    return r;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        // Fallback if no match
        Recommendation fallback = new Recommendation();
        String[] parts = predictedLabel.split("_", 2);
        fallback.crop = parts[0];
        fallback.disease = parts[1].replace("_", " ");
        fallback.recommendation = "No recommendation available.";
        return fallback;
    }

    private String loadJSONFromAsset(String fileName) {
        try {
            InputStream is = getAssets().open(fileName);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            return new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }
}
