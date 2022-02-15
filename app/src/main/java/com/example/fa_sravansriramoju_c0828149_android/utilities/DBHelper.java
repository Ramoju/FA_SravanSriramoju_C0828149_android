package com.example.fa_sravansriramoju_c0828149_android.utilities;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.fa_sravansriramoju_c0828149_android.Model.Place;

import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {
    private SQLiteDatabase db;

    private static final String DB_NAME = "PLACES_DATABASE";

    private static final String TABLE_PLACES = "PLACES";
    private static final String COL_ID = "ID";
    private static final String COL_TITLE = "TITLE";
    private static final String COL_ADDRESS = "ADDRESS";
    private static final String COL_VISITED = "VISITED";
    private static final String COL_LATITUDE = "LATITUDE";
    private static final String COL_LONGITUDE = "LONGITUDE";

    public DBHelper(@Nullable Context context) {
        super(context, DB_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_CATEGORIES + "(" + CAT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + CAT_NAME + " TEXT," + CAT_STATUS + " INTEGER)");
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_PLACES + "(" + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COL_TITLE + " TEXT," + COL_ADDRESS + " TEXT," + COL_VISITED + " INTEGER," + COL_LATITUDE + " DOUBLE," + COL_LONGITUDE + " DOUBLE)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PLACES);
        onCreate(db);
    }

    public void insertPlace(Place place){
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_TITLE, place.getTitle());
        values.put(COL_ADDRESS, place.getAddress());
        values.put(COL_VISITED, place.isVisited());
        values.put(COL_LATITUDE, place.getLatitude());
        values.put(COL_LONGITUDE, place.getLongitude());
        db.insert(TABLE_PLACES,null, values);
    }

    public void deletePlace(int id){
        db = this.getWritableDatabase();
        db.delete(TABLE_PLACES,"ID=?", new String[]{String.valueOf(id)});
    }


    public void updatePlace(int id, String title, String address, double latitude, double longitude){
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_TITLE, title);
        values.put(COL_ADDRESS, address);
        values.put(COL_LATITUDE, latitude);
        values.put(COL_LONGITUDE, longitude);
        db.update(TABLE_PLACES , values , "ID=?" , new String[]{String.valueOf(id)});
    }

    public void updateVisited(int id, int visited) {
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_VISITED, visited);
        db.update(TABLE_PLACES , values , "ID=?" , new String[]{String.valueOf(id)});
    }

    public List<Place> getAllPlaces() {
        db = this.getWritableDatabase();
        Cursor cursor = null;
        List<Place> places = new ArrayList<>();
        db.beginTransaction();
        try {
            cursor = db.query(TABLE_PLACES, null, null, null, null, null, null);
            if (cursor != null){
                if (cursor.moveToFirst()){
                    do {
                        Place place = new Place();
                        place.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COL_ID)));
                        place.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(COL_TITLE)));
                        place.setAddress(cursor.getString(cursor.getColumnIndexOrThrow(COL_ADDRESS)));
                        place.setVisited(cursor.getInt(cursor.getColumnIndexOrThrow(COL_VISITED)));
                        place.setLatitude(cursor.getDouble(cursor.getColumnIndexOrThrow(COL_LATITUDE)));
                        place.setLongitude(cursor.getDouble(cursor.getColumnIndexOrThrow(COL_LONGITUDE)));
                        places.add(place);
                    }while (cursor.moveToNext());
                }
            }
        }finally {
            db.endTransaction();
            cursor.close();
        }
        return places;
    }
}
