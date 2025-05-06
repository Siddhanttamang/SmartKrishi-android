package com.example.smartkrishi.utils;
import ai.onnxruntime.*;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import java.io.InputStream;
import java.nio.FloatBuffer;
import java.util.Collections;

public class CameraActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // TODO: Add camera processing code here
    }
    private FloatBuffer preprocessImage(Bitmap bitmap) {
        int imageSize = 128;
        Bitmap resized = Bitmap.createScaledBitmap(bitmap, imageSize, imageSize, true);
        FloatBuffer buffer = FloatBuffer.allocate(3 * imageSize * imageSize);
        buffer.rewind();

        for (int y = 0; y < imageSize; y++) {
            for (int x = 0; x < imageSize; x++) {
                int pixel = resized.getPixel(x, y);
                float r = ((pixel >> 16) & 0xFF) / 255.0f;
                float g = ((pixel >> 8) & 0xFF) / 255.0f;
                float b = (pixel & 0xFF) / 255.0f;

                buffer.put((r - 0.485f) / 0.229f);
                buffer.put((g - 0.456f) / 0.224f);
                buffer.put((b - 0.406f) / 0.225f);
            }
        }
        buffer.rewind();
        return buffer;
    }
    private String runInference(FloatBuffer buffer) {
        try {
            OrtEnvironment env = OrtEnvironment.getEnvironment();
            OrtSession.SessionOptions opts = new OrtSession.SessionOptions();

            InputStream is = getAssets().open("model.onnx");
            byte[] modelBytes = new byte[is.available()];
            is.read(modelBytes);
            is.close();

            OrtSession session = env.createSession(modelBytes, opts);
            OnnxTensor inputTensor = OnnxTensor.createTensor(env, buffer, new long[]{1, 3, 128, 128});
            OrtSession.Result result = session.run(Collections.singletonMap("input", inputTensor));

            float[][] output = (float[][]) result.get(0).getValue();

            int maxIndex = 0;
            for (int i = 1; i < output[0].length; i++) {
                if (output[0][i] > output[0][maxIndex]) {
                    maxIndex = i;
                }
            }

            String[] labels = {
                    "Bean_angular_leaf_spot", "Bean_bean_rust", "Bean_healthy",
                    "Brinjal_fruit_and_shoot_borer", "Brinjal_leaf_curl", "Brinjal_phomopsis_blight", "Brinjal_healthy",
                    "Chilli_leaf_spot", "Chilli_whitefly", "Chilli_healthy",
                    "Maize_leaf_blight", "Maize_rust", "Maize_healthy",
                    "Rice_bacterial_leaf_blight", "Rice_brown_spot", "Rice_leaf_smut", "Rice_healthy",
                    "Tomato_early_blight", "Tomato_leaf_curl", "Tomato_septoria_leaf_spot", "Tomato_spider_mites", "Tomato_healthy"
            };

            return labels[maxIndex];

        } catch (Exception e) {
            e.printStackTrace();
            return "Error";
        }
    }


}
