package com.example.smartkrishi.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.smartkrishi.R;
import com.example.smartkrishi.api.ReportAPi;
import com.example.smartkrishi.api.RetrofitClient;
import com.example.smartkrishi.ml.ONNXClassifier;
import com.example.smartkrishi.models.Recommendation;
import com.example.smartkrishi.models.UserLoginResponse;
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
    private Button btnOk;

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
        btnOk = findViewById(R.id.btnok);

        // Load image from file path
        String imagePath = getIntent().getStringExtra("image_path");
        if (imagePath != null) {
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
            imagePreview.setImageBitmap(bitmap);
            classify(bitmap);
        }

        // Load image from byte array (camera intent)
        byte[] byteArray = getIntent().getByteArrayExtra("image");
        if (byteArray != null) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
            imagePreview.setImageBitmap(bitmap);
            classify(bitmap);
        } else if (imagePath == null) {
            recommendationText.setText("No image received");
        }
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
                    if(r.crop!=null&&r.disease!=null&&r.recommendation!=null){
                        btnSave.setOnClickListener(e -> {

                            SharedPreferences preferences = getSharedPreferences("app_prefs", MODE_PRIVATE);
                            String token = preferences.getString("auth_token", null);



                            if (token != null) {

                                Recommendation data = new Recommendation(
                                        r.getRecommendation(),
                                        r.getDisease(),
                                        r.getCrop()
                                );

                                ReportAPi reportApi = RetrofitClient.getClient().create(ReportAPi.class);
                                Call<Void> call = reportApi.createReport("Bearer " + token, data);


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
                        });


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

    private Recommendation getRecommendation(String predictedLabel) {
        try {
            String json = loadJSONFromAsset("class_recommendations.json");
            Gson gson = new Gson();
            Type listType = new TypeToken<List<Recommendation>>() {}.getType();
            List<Recommendation> list = gson.fromJson(json, listType);

            // Remove probability info if present
            if (predictedLabel.contains(" (")) {
                predictedLabel = predictedLabel.substring(0, predictedLabel.indexOf(" ("));
            }

            // Parse class
            String[] parts = predictedLabel.split("_", 2);
            String crop = parts[0].trim().toLowerCase();
            String disease = parts[1].trim().replace("_", " ").toLowerCase();

            Log.d("DEBUG", "Looking for match: " + crop + " - " + disease);

            for (Recommendation r : list) {
                String rCrop = r.crop.trim().toLowerCase();
                String rDisease = r.disease.trim().toLowerCase();

                Log.d("DEBUG", "Checking: " + rCrop + " - " + rDisease);

                if (rCrop.equals(crop) && rDisease.equals(disease)) {
                    return r;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        // Fallback if no match found
        Recommendation fallback = new Recommendation();
        String[] parts = predictedLabel.split("_", 2);
        fallback.crop = parts[0];
        fallback.disease = parts[1].replace("_", " ");
        fallback.recommendation = "No recommendation available.";
        return fallback;
    }


    private String loadJSONFromAsset(String fileName) {
        String json = null;
        try {
            InputStream is = getAssets().open(fileName);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return json;
    }
}
