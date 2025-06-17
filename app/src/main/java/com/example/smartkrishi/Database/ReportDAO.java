package com.example.smartkrishi.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.smartkrishi.models.Reports;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ReportDAO {
    private final SmartKrishiDBHelper dbHelper;

    public ReportDAO(Context context) {
        dbHelper = new SmartKrishiDBHelper(context);
    }

    public void insertReport(Reports report, boolean isSynced) {
        String createdAt = report.getCreated_at();
        if (createdAt == null) {
            createdAt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
                    .format(new Date());
            report.setCreated_at(createdAt); // 🟢 Make sure it's set on the report object
        }

        if (isDuplicate(report)) return;

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("crop_name", report.getCrop_name());
        values.put("disease", report.getDisease());
        values.put("recommendation", report.getRecommendation());
        values.put("user_id", report.getUser_id());
        values.put("created_at", createdAt);
        values.put("synced", isSynced ? 1 : 0);

        db.insert("report", null, values);
        db.close();
    }


    public boolean isDuplicate(Reports report) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT 1 FROM report WHERE crop_name=? AND disease=? AND created_at=? LIMIT 1",
                new String[]{report.getCrop_name(), report.getDisease(), report.getCreated_at()});
        boolean exists = cursor.moveToFirst();
        cursor.close();
        db.close();
        return exists;
    }

    public List<Reports> getUnsyncedReports() {
        List<Reports> unsynced = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor c = db.query("report", null, "synced=0", null, null, null, "created_at DESC");
        while (c.moveToNext()) {
            Reports r = new Reports(
                    c.getString(c.getColumnIndexOrThrow("crop_name")),
                    c.getInt(c.getColumnIndexOrThrow("user_id")),
                    c.getString(c.getColumnIndexOrThrow("created_at")),
                    c.getString(c.getColumnIndexOrThrow("recommendation")),
                    c.getString(c.getColumnIndexOrThrow("disease"))
            );
            unsynced.add(r);
        }
        c.close();
        db.close();
        return unsynced;
    }

    public void markAsSynced(String createdAt) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues v = new ContentValues();
        v.put("synced", 1);
        db.update("report", v, "created_at=?", new String[]{createdAt});
        db.close();
    }
}

