package com.pegadaian.filmapps.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.pegadaian.filmapps.model.Film;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class FilmDatabase {
    private DBHelper dbHelper;

    private static final String tableName = "filmData";
    private static final String fieldName = "name";
    private static final String fieldRating = "rating";
    private static final String fieldDirector = "director";
    private static final String fieldGenre = "genre";
    private static final String fieldDescription = "description";
    private static final String fieldRuntime = "runtime";
    private static final String fieldRelease = "release_date";
    private static final String fieldImage = "image";
    private static final String fieldKode = "kode";
    String TAG = "Film Database";

    public FilmDatabase(Context context) {
        dbHelper = new DBHelper(context);
    }

    public void insertData(Film film) {
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(fieldName, film.getFilmName());
        contentValues.put(fieldRating, film.getFilmRating());
        contentValues.put(fieldDirector, film.getFilmDirector());
        contentValues.put(fieldGenre, film.getFilmGenre());
        contentValues.put(fieldDescription, film.getFilmDescription());
        contentValues.put(fieldRuntime, film.getFilmRuntime());
        contentValues.put(fieldRelease, film.getFilmRelease());
        contentValues.put(fieldImage, film.getFilmImg());
        Log.i(TAG, "Data List Insert : " + film.getKode());
        long cek = database.insert(tableName, null, contentValues);
        Log.i(TAG, "Data List Insert : " + cek);
        database.close();
    }

    public void deleteFilm(String id) {
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        String selection = fieldKode +" = ? ";
        String []args = {""+id};
        int cursor = database.delete(tableName, selection, args);
        database.close();
    }

    public void updateFilm(Film film, String id) {
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        String selection = fieldKode +" = ? ";
        String []args = {""+id};
        ContentValues contentValues = new ContentValues();
        contentValues.put(fieldName, film.getFilmName());
        contentValues.put(fieldRating, film.getFilmRating());
        contentValues.put(fieldDirector, film.getFilmDirector());
        contentValues.put(fieldGenre, film.getFilmGenre());
        contentValues.put(fieldDescription, film.getFilmDescription());
        contentValues.put(fieldRuntime, film.getFilmRuntime());
        contentValues.put(fieldRelease, film.getFilmRelease());
        contentValues.put(fieldImage, film.getFilmImg());
        long cursor = database.update(tableName, contentValues, selection, args);
        database.close();
    }

    public ArrayList<Film> getListData() {
        ArrayList<Film> listFilm = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(tableName, null, null, null, null, null, null);
        while (cursor.moveToNext()) {
            Film film = new Film();
            film.setKode(cursor.getString(cursor.getColumnIndex(fieldKode)));
            film.setFilmName(cursor.getString(cursor.getColumnIndex(fieldName)));
            film.setFilmGenre(cursor.getString(cursor.getColumnIndex(fieldGenre)));
            film.setFilmDirector(cursor.getString(cursor.getColumnIndex(fieldDirector)));
            film.setFilmRating(cursor.getString(cursor.getColumnIndex(fieldRating)));
            film.setFilmRuntime(cursor.getString(cursor.getColumnIndex(fieldRuntime)));
            film.setFilmRelease(cursor.getString(cursor.getColumnIndex(fieldRelease)));
            film.setFilmDescription(cursor.getString(cursor.getColumnIndex(fieldDescription)));
            film.setFilmImg(cursor.getString(cursor.getColumnIndex(fieldImage)));
            listFilm.add(film);
        }
        db.close();
        return  listFilm;
    }

    public Film getFilmById(String id) {
        Film film = new Film();
        SQLiteDatabase database = dbHelper.getReadableDatabase();
        String selection = fieldKode +" = ? ";
        String []args = {""+id};
        Cursor cursor = database.query(tableName, null, selection, args, null, null, null);
        if (cursor.moveToNext()) {
            film = new Film();
            film.setKode(cursor.getString(cursor.getColumnIndex(fieldKode)));
            film.setFilmName(cursor.getString(cursor.getColumnIndex(fieldName)));
            film.setFilmGenre(cursor.getString(cursor.getColumnIndex(fieldGenre)));
            film.setFilmDirector(cursor.getString(cursor.getColumnIndex(fieldDirector)));
            film.setFilmRating(cursor.getString(cursor.getColumnIndex(fieldRating)));
            film.setFilmRuntime(cursor.getString(cursor.getColumnIndex(fieldRuntime)));
            film.setFilmRelease(cursor.getString(cursor.getColumnIndex(fieldRelease)));
            film.setFilmDescription(cursor.getString(cursor.getColumnIndex(fieldDescription)));
            film.setFilmImg(cursor.getString(cursor.getColumnIndex(fieldImage)));
        }
        database.close();
        return film;
    }

}
