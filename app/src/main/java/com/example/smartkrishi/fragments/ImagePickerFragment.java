package com.example.smartkrishi.fragments;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.*;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.DialogFragment;

import com.example.smartkrishi.R;
import com.example.smartkrishi.utils.PestDetectionActivity;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ImagePickerFragment extends DialogFragment {

    private Uri photoUri;
    private File photoFile;

    private final ActivityResultLauncher<String[]> permissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), result -> {
                boolean allGranted = !result.containsValue(false);
                if (allGranted) {
                    showChoiceDialog();
                } else {
                    Toast.makeText(getContext(), "Permissions denied", Toast.LENGTH_SHORT).show();
                    dismiss();
                }
            });

    private final ActivityResultLauncher<Intent> cameraLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    startPestDetection(photoFile.getAbsolutePath());
                }
                dismiss();
            });

    private final ActivityResultLauncher<Intent> galleryLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                    Uri selectedImageUri = result.getData().getData();
                    if (selectedImageUri != null) {
                        startPestDetection(selectedImageUri.toString());
                    }
                }
                dismiss();
            });

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = new Dialog(requireContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(new View(requireContext())); // no actual layout
        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);

        // Set dialog fade-in/out animations
        if (dialog.getWindow() != null) {
            dialog.getWindow().getAttributes().windowAnimations = R.style.DialogFadeAnimation;
        }

        return dialog;
    }

    @Override
    public void onStart() {
        super.onStart();
        checkPermissionsAndStart();

        Dialog dialog = getDialog();
        if (dialog != null && dialog.getWindow() != null) {
            Window window = dialog.getWindow();
            window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            window.setBackgroundDrawableResource(R.drawable.blur_dim); // blur-like background
        }
    }


    private void checkPermissionsAndStart() {
        String[] requiredPermissions;

        if (Build.VERSION.SDK_INT >= 33) {
            requiredPermissions = new String[]{
                    Manifest.permission.CAMERA,
                    Manifest.permission.READ_MEDIA_IMAGES,
                    Manifest.permission.READ_MEDIA_VIDEO
            };
        } else {
            requiredPermissions = new String[]{
                    Manifest.permission.CAMERA,
                    Manifest.permission.READ_EXTERNAL_STORAGE
            };
        }

        boolean permissionsGranted = true;
        for (String permission : requiredPermissions) {
            if (ContextCompat.checkSelfPermission(requireContext(), permission) != PackageManager.PERMISSION_GRANTED) {
                permissionsGranted = false;
                break;
            }
        }

        if (!permissionsGranted) {
            permissionLauncher.launch(requiredPermissions);
        } else {
            showChoiceDialog();
        }
    }

    private void showChoiceDialog() {
        String[] options = {"Camera", "Gallery"};
        new AlertDialog.Builder(requireContext())
                .setTitle("Select Image From")
                .setItems(options, (dialog, which) -> {
                    if (which == 0) {
                        openCamera();
                    } else {
                        openGallery();
                    }
                })
                .setOnCancelListener(dialog -> dismiss())
                .show();
    }

    private void openCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        try {
            photoFile = createImageFile();
            photoUri = FileProvider.getUriForFile(
                    requireContext(),
                    requireContext().getPackageName() + ".provider",
                    photoFile);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
            cameraLauncher.launch(intent);
        } catch (IOException e) {
            Toast.makeText(getContext(), "Failed to create image file", Toast.LENGTH_SHORT).show();
            dismiss();
        }
    }

    private void openGallery() {
        Intent pickIntent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        pickIntent.addCategory(Intent.CATEGORY_OPENABLE);
        pickIntent.setType("image/*");
        galleryLauncher.launch(pickIntent);
    }

    private void startPestDetection(String imagePathOrUriString) {
        Intent intent = new Intent(requireContext(), PestDetectionActivity.class);
        intent.putExtra("image_path", imagePathOrUriString);
        startActivity(intent);
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String fileName = "IMG_" + timeStamp;
        File storageDir = requireContext().getExternalFilesDir("Pictures");
        return File.createTempFile(fileName, ".jpg", storageDir);
    }
}
