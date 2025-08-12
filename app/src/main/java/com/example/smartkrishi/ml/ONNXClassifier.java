package com.example.smartkrishi.ml;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.Log;

import ai.onnxruntime.*;

import java.io.InputStream;
import java.nio.FloatBuffer;
import java.util.*;

public class ONNXClassifier {

    private static final String TAG = "ONNXClassifier";
    private final OrtEnvironment env;
    private final OrtSession session;
    private final int inputSize = 128; // Match PyTorch model input

    private final String[] classNames = {
            "Bean_angular_leaf_spot", "Bean_bean_rust",
            "Cauliflower_Alternaria_Leaf_Spot", "Cauliflower_Black_Rot",
            "Cauliflower_Cabbage_aphid_colony", "Cauliflower_Downy_Mildew",
            "Cauliflower_ring_spot", "Paddy_Bacterial_leaf_blight",
            "Paddy_Brown_spot", "Paddy_Leaf_smut", "Potato_Early_blight",
            "Potato_Late_blight", "Potato_healthy", "Tomato_Bacterial_spot",
            "Tomato_Early_blight", "Tomato_Late_blight", "Tomato_Leaf_Mold",
            "Tomato_Septoria_leaf_spot", "Tomato_Spider_mites_Two-spotted_spider_mite",
            "Tomato_Target_Spot", "Tomato_Tomato_mosaic_virus", "Tomato_healthy"
    };

    public ONNXClassifier(Context context) throws Exception {
        try {
            env = OrtEnvironment.getEnvironment();

            InputStream modelStream = context.getAssets().open("pest_model.onnx");
            byte[] modelBytes = new byte[modelStream.available()];
            modelStream.read(modelBytes);
            modelStream.close();

            OrtSession.SessionOptions options = new OrtSession.SessionOptions();
            session = env.createSession(modelBytes, options);

        } catch (Exception e) {
            Log.e(TAG, "Initialization error", e);
            throw new Exception("Failed to initialize ONNX classifier: " + e.getMessage());
        }
    }

    /**
     * Classify single bitmap (no TTA).
     * Returns top prediction string with confidence.
     */
    public String classify(Bitmap bitmap) throws Exception {
        float[] scores = runModel(bitmap);
        float[] probs = softmax(scores);
        int maxIndex = argMax(probs);
        return classNames[maxIndex] + " (" + String.format("%.1f", probs[maxIndex] * 100) + "%)";
    }

    /**
     * Run model on single image.
     */
    private float[] runModel(Bitmap bmp) throws Exception {
        FloatBuffer buffer = FloatBuffer.allocate(1 * 3 * inputSize * inputSize);
        Bitmap resized = Bitmap.createScaledBitmap(bmp, inputSize, inputSize, true);

// For each channel, fill all pixels
        for (int c = 0; c < 3; c++) {
            for (int y = 0; y < inputSize; y++) {
                for (int x = 0; x < inputSize; x++) {
                    int pixel = resized.getPixel(x, y);
                    float value;
                    switch (c) {
                        case 0: // R
                            value = ((Color.red(pixel) / 255.0f) - 0.485f) / 0.229f;
                            break;
                        case 1: // G
                            value = ((Color.green(pixel) / 255.0f) - 0.456f) / 0.224f;
                            break;
                        case 2: // B
                            value = ((Color.blue(pixel) / 255.0f) - 0.406f) / 0.225f;
                            break;
                        default:
                            value = 0;
                    }
                    buffer.put(value);
                }
            }
        }
        buffer.rewind();

        long[] shape = {1, 3, inputSize, inputSize};

        try (OnnxTensor inputTensor = OnnxTensor.createTensor(env, buffer, shape)) {
            OrtSession.Result result = session.run(Collections.singletonMap(
                    session.getInputNames().iterator().next(), inputTensor));

            float[][] output = (float[][]) result.get(0).getValue();
            return output[0];  // raw logits before softmax
        }
    }

    /**
     * Softmax function.
     */
    private float[] softmax(float[] scores) {
        float max = scores[0];
        for (float v : scores) {
            if (v > max) max = v;
        }
        float sum = 0f;
        float[] expScores = new float[scores.length];
        for (int i = 0; i < scores.length; i++) {
            expScores[i] = (float) Math.exp(scores[i] - max);
            sum += expScores[i];
        }
        for (int i = 0; i < scores.length; i++) {
            expScores[i] /= sum;
        }
        return expScores;
    }

    /**
     * Argmax helper.
     */
    private int argMax(float[] array) {
        int maxIndex = 0;
        float maxVal = array[0];
        for (int i = 1; i < array.length; i++) {
            if (array[i] > maxVal) {
                maxVal = array[i];
                maxIndex = i;
            }
        }
        return maxIndex;
    }
}
