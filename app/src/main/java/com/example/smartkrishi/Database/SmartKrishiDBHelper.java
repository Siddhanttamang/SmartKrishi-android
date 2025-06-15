package com.example.smartkrishi.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SmartKrishiDBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "smartkrishi.db";
    private static final int DATABASE_VERSION = 1;

    public SmartKrishiDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Table for News
        db.execSQL("CREATE TABLE news (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "title TEXT, " +
                "price TEXT)");

        // Table for Weather
        db.execSQL("CREATE TABLE weather (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "temperature TEXT, " +
                "description TEXT, " +
                "date TEXT)");
        //Table for PestReport
        db.execSQL("CREATE TABLE report (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "crop_name TEXT, " +
                "disease TEXT, " +
                "recommendation TEXT, " +
                "user_id INTEGER,"+
                "created_at TEXT)");


    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS news");
        db.execSQL("DROP TABLE IF EXISTS weather");
        db.execSQL("DROP TABLE IF EXISTS report");
        onCreate(db);
    }

}
