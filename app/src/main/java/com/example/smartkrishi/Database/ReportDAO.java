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

    public void insertReport(Reports report) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("crop_name", report.getCrop_name());
        values.put("disease", report.getDisease());
        values.put("recommendation", report.getRecommendation());
        values.put("user_id", report.getUser_id());
        values.put("created_at", report.getCreated_at()); // Format: "yyyy-MM-dd"

        db.insert("report", null, values);
        db.close();
    }

    public void clearReports() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete("report", null, null); // Delete all rows
        db.close();
    }
    public void syncReports(List<Reports> newReports) {
        clearReports();
        for (Reports report : newReports) {
            insertReport(report);
        }
    }

    public List<Reports> getReports() {
        List<Reports> reportList = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM report ORDER BY created_at DESC", null);

        while (c.moveToNext()) {
            Reports reports = new Reports(
                    c.getString(c.getColumnIndexOrThrow("crop_name")),
                    c.getInt(c.getColumnIndexOrThrow("user_id")),
                    c.getString(c.getColumnIndexOrThrow("created_at")),
                    c.getString(c.getColumnIndexOrThrow("recommendation")),
                    c.getString(c.getColumnIndexOrThrow("disease"))
            );
            reportList.add(reports);
        }

        c.close();
        db.close();
        return reportList;
    }
}
