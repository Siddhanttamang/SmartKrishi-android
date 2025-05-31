package com.example.smartkrishi.utils;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.smartkrishi.R;
import com.example.smartkrishi.ml.ONNXClassifier;
import com.example.smartkrishi.models.Recommendation;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.List;

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

            String[] parts = predictedLabel.split("_", 2);
            String crop = parts[0];
            String disease = parts[1].replace("_", " ");
            Log.d("DEBUG", "Looking for match: " + crop + " - " + disease);

            for (Recommendation r : list) {
                Log.d("DEBUG", "Checking: " + r.crop + " - " + r.disease);
                if (r.crop.trim().equalsIgnoreCase(crop.trim()) && r.disease.trim().equalsIgnoreCase(disease.trim())) {
                    return r;
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Fallback if no match found
        Recommendation fallback = new Recommendation();
        fallback.crop = predictedLabel.split("_")[0];
        fallback.disease = predictedLabel.split("_", 2)[1].replace("_", " ");
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
