package com.example.smartkrishi.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.smartkrishi.R;
import com.example.smartkrishi.ml.ONNXClassifier;

public class PestDetectionActivity extends AppCompatActivity {

    private ImageView imagePreview;
    private TextView resultText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pest_detection);

        imagePreview = findViewById(R.id.image_preview);
        resultText = findViewById(R.id.result_text);
        String imagePath = getIntent().getStringExtra("image_path");
        if (imagePath != null) {
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
            imagePreview.setImageBitmap(bitmap);
            classify(bitmap);
        }

        // Get image byte array from intent
        byte[] byteArray = getIntent().getByteArrayExtra("image");
        if (byteArray != null) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
            imagePreview.setImageBitmap(bitmap);

            classify(bitmap); // Call your ONNXClassifier
        } else {
            resultText.setText("No image received.");
        }
    }

    private void classify(Bitmap bitmap) {
        new Thread(() -> {
            try {
                ONNXClassifier classifier = new ONNXClassifier(this);
                String prediction = classifier.classify(bitmap);

                runOnUiThread(() -> resultText.setText("Prediction: " + prediction));
            } catch (Exception e) {
                e.printStackTrace();
                runOnUiThread(() -> {
                    resultText.setText("Error: " + e.getMessage());
                    Toast.makeText(this, "Inference failed", Toast.LENGTH_SHORT).show();
                });
            }
        }).start();
    }
}
