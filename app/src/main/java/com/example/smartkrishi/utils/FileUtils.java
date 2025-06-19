package com.example.smartkrishi.utils;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.OpenableColumns;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

public class FileUtils {
    public static File getFileFromUri(Context context, Uri uri) {
        File file = null;
        try {
            InputStream inputStream = context.getContentResolver().openInputStream(uri);
            String fileName = getFileName(context, uri);
            File tempFile = new File(context.getCacheDir(), fileName);
            FileOutputStream out = new FileOutputStream(tempFile);
            byte[] buf = new byte[1024];
            int len;
            while ((len = inputStream.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            out.close();
            inputStream.close();
            file = tempFile;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return file;
    }

    private static String getFileName(Context context, Uri uri) {
        String result = null;
        Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            int index = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
            if (index >= 0) {
                result = cursor.getString(index);
            }
            cursor.close();
        }

        if (result == null) {
            result = uri.getLastPathSegment();
        }

        return result;
    }

}
