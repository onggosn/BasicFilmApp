package com.pegadaian.filmapps.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "film";
    public static final int version = 1;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "Create Table filmData (" + "kode INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name VARCHAR(200), " +
                "rating VARCHAR(10), " +
                "director VARCHAR(100), " +
                "genre VARCHAR(100), " +
                "description VARCHAR(225), " +
                "runtime VARCHAR(10), " +
                "release_date VARCHAR(20), "+
                "image" +")";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS film");
        onCreate(db);
    }
}
