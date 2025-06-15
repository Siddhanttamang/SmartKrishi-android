package com.example.smartkrishi.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.smartkrishi.models.Reports;

import java.util.ArrayList;
import java.util.List;

public class ReportDAO {
    private final SmartKrishiDBHelper dbHelper;

    public ReportDAO(Context context) {
        dbHelper = new SmartKrishiDBHelper(context);
    }

    // Insert a new report
    public void insertReport(Reports report) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("crop_name", report.getCropName());
        values.put("disease", report.getDisease());
        values.put("recommendation", report.getRecommendation());
        values.put("user_id", report.getUserId());
        values.put("created_at", report.getCreatedAt());

        db.insert("report", null, values);
        db.close();
    }

    // Get all reports (optional: you can filter by user_id too)
    public List<Reports> getAllReports() {
        List<Reports> reportList = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query("report", null, null, null, null, null, "id DESC");

        if (cursor.moveToFirst()) {
            do {
                Reports report = new Reports(
                        cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                        cursor.getString(cursor.getColumnIndexOrThrow("crop_name")),
                        cursor.getString(cursor.getColumnIndexOrThrow("disease")),
                        cursor.getString(cursor.getColumnIndexOrThrow("recommendation")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("user_id")),
                        cursor.getString(cursor.getColumnIndexOrThrow("created_at"))
                );
                reportList.add(report);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return reportList;
    }
}
