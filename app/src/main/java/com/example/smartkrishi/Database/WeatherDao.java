package com.example.smartkrishi.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.smartkrishi.Database.SmartKrishiDBHelper;
import com.example.smartkrishi.models.Weather;

import java.util.ArrayList;
import java.util.List;

public class WeatherDao {
    private final SmartKrishiDBHelper dbHelper;

    public WeatherDao(Context context) {
        dbHelper = new SmartKrishiDBHelper(context);
    }

    public void insertWeather(Weather weather) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("temperature", weather.getTemperature());
        values.put("description", weather.getDescription());
        values.put("date", weather.getDate());
        db.insert("weather", null, values);
        db.close();
    }

    public List<Weather> getAllWeather() {
        List<Weather> weatherList = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query("weather", null, null, null, null, null, "id DESC");

        if (cursor.moveToFirst()) {
            do {
                Weather weather = new Weather(
                        cursor.getString(cursor.getColumnIndexOrThrow("temperature")),
                        cursor.getString(cursor.getColumnIndexOrThrow("description")),
                        cursor.getString(cursor.getColumnIndexOrThrow("date"))
                );
                weatherList.add(weather);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return weatherList;
    }
}
