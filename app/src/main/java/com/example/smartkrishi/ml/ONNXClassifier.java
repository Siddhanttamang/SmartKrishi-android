package com.example.smartkrishi.ml;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.Log;


import ai.onnxruntime.*;


import java.io.InputStream;
import java.nio.FloatBuffer;
import java.util.Collections;

public class ONNXClassifier {

    private static final String TAG = "ONNXClassifier";
    private final OrtEnvironment env;
    private final OrtSession session;
    private final int inputSize = 128; // Match your model input shape

    // Model classes (in order!)
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

            // Load ONNX model from assets
            InputStream modelStream = context.getAssets().open("pest_model.onnx");
            byte[] modelBytes = new byte[modelStream.available()];
            modelStream.read(modelBytes);
            modelStream.close();

            OrtSession.SessionOptions options = new OrtSession.SessionOptions();
            session = env.createSession(modelBytes, options);

            // Verify input shape
            NodeInfo nodeInfo = session.getInputInfo().values().iterator().next();
            TensorInfo inputInfo = (TensorInfo) nodeInfo.getInfo();
            long[] shape = inputInfo.getShape();
            if (shape[2] != inputSize || shape[3] != inputSize) {
                throw new Exception("Model expects input size " + shape[2] + "x" + shape[3]);
            }

        } catch (Exception e) {
            Log.e(TAG, "Initialization error", e);
            throw new Exception("Failed to initialize ONNX classifier: " + e.getMessage());
        }
    }

    public String classify(Bitmap bitmap) throws Exception {
        Bitmap resized = Bitmap.createScaledBitmap(bitmap, inputSize, inputSize, true);

        FloatBuffer buffer = FloatBuffer.allocate(1 * 3 * inputSize * inputSize);
        for (int y = 0; y < inputSize; y++) {
            for (int x = 0; x < inputSize; x++) {
                int pixel = resized.getPixel(x, y);

                float r = ((Color.red(pixel) / 255.0f) - 0.485f) / 0.229f;
                float g = ((Color.green(pixel) / 255.0f) - 0.456f) / 0.224f;
                float b = ((Color.blue(pixel) / 255.0f) - 0.406f) / 0.225f;

                buffer.put(r);
                buffer.put(g);
                buffer.put(b);
            }
        }

        buffer.rewind();
        long[] shape = {1, 3, inputSize, inputSize};

        try (OnnxTensor inputTensor = OnnxTensor.createTensor(env, buffer, shape)) {
            OrtSession.Result result = session.run(Collections.singletonMap(
                    session.getInputNames().iterator().next(), inputTensor));

            float[][] output = (float[][]) result.get(0).getValue();
            return getTopClass(output[0]);
        }
    }

    private String getTopClass(float[] scores) {
        int maxIdx = 0;
        float sum = 0f;

        // Softmax: compute exp and sum
        float[] expScores = new float[scores.length];
        for (int i = 0; i < scores.length; i++) {
            expScores[i] = (float) Math.exp(scores[i]);
            sum += expScores[i];
        }

        // Find max after softmax
        float maxProb = 0f;
        for (int i = 0; i < expScores.length; i++) {
            float prob = expScores[i] / sum;
            if (prob > maxProb) {
                maxProb = prob;
                maxIdx = i;
            }
        }

        return classNames[maxIdx] + " (" + String.format("%.1f", maxProb * 100) + "%)";
    }

}
