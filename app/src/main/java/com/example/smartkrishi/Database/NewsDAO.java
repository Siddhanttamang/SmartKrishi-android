package com.example.smartkrishi.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.smartkrishi.models.News;

import java.util.ArrayList;
import java.util.List;

public class NewsDAO {
    private final SmartKrishiDBHelper dbHelper;

    public NewsDAO(Context context) {
        dbHelper = new SmartKrishiDBHelper(context);
    }

    public void insertNews(News news) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("title", news.getName());
        values.put("price", news.getPrice());
        values.put("updated_at", news.getUpdated_at());
        db.insert("news", null, values);
        db.close();
    }
    public void clearReports() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete("news", null, null); // Delete all rows
        db.close();
    }


    public List<News> getAllNews() {
        List<News> newsList = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query("news", null, null, null, null, null, "id DESC");

        if (cursor.moveToFirst()) {
            do {
                News news = new News(
                        cursor.getString(cursor.getColumnIndexOrThrow("title")),
                        cursor.getString(cursor.getColumnIndexOrThrow("price")),
                        cursor.getString(cursor.getColumnIndexOrThrow("updated_at"))
                );
                newsList.add(news);
            } while (cursor.moveToNext());
        }


        cursor.close();
        db.close();
        return newsList;
    }
}

