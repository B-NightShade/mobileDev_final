package com.example.cs435_final;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import androidx.annotation.Nullable;

import java.io.File;
import java.io.IOException;

public class ImagesSQLiteHelper extends SQLiteOpenHelper {

    public static final String DB_NAME= "images.sqlite";
    public static final int DB_VERSION = 1;
    Context context1;
    static boolean addGroupBy;

    public ImagesSQLiteHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        context1 = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String create = "CREATE TABLE states (_id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT);";
        sqLiteDatabase.execSQL(create);
        create = "CREATE TABLE entries (entryid INTEGER PRIMARY KEY AUTOINCREMENT, year INTEGER, month INTEGER, day INTEGER, date TEXT, description TEXT, image TEXT," +
                "stateId INTEGER, FOREIGN KEY (stateID) REFERENCES states (_id))";
        sqLiteDatabase.execSQL(create);
    }

    int getStateCount(){
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM states", null);
        return cursor.getCount();
    }

    void insertState(String name){
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        sqLiteDatabase.insert("states", null, contentValues);
    }

    String getState(int position){
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        Cursor cursor = sqLiteDatabase.query("states",
                new String[]{"_id", "name"},
                null, null, null, null, null);
        if(cursor.moveToPosition(position)){
            String name = cursor.getString(1);
            return name;
        }
        return null;
    }

    void insertEntry(int year, int month, int day, String date, String description, String image, int stateId){
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        ContentValues contentValues =new ContentValues();
        contentValues.put("year", year);
        contentValues.put("month", month);
        contentValues.put("day", day);
        contentValues.put("date", date);
        contentValues.put("description", description);
        contentValues.put("image", image);
        contentValues.put("stateId", stateId);
        sqLiteDatabase.insert("entries", null, contentValues);
    }

    int count(int stateId){
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("select * from entries where stateId = ?",new String[]{String.valueOf(stateId)});
        return cursor.getCount();
    }

    String getDate(int stateId, int position){
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        String cmd = "select * from entries where stateId = ?";
        if (addGroupBy){
            cmd += "ORDER BY year DESC, month DESC, day DESC";
        }
        Cursor cursor = sqLiteDatabase.rawQuery(cmd,new String[]{String.valueOf(stateId)});
        if(cursor.moveToPosition(position)){
            String date = cursor.getString(4);
            return date;
        }
        return "";
    }

    String getDescription(int stateId, int position){
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        String cmd = "select * from entries where stateId = ?";
        if (addGroupBy){
            cmd += "ORDER BY year DESC, month DESC, day DESC";
        }
        Cursor cursor = sqLiteDatabase.rawQuery(cmd,new String[]{String.valueOf(stateId)});
        if(cursor.moveToPosition(position)){
            String desc = cursor.getString(5);
            return desc;
        }
        return "";
    }

    Bitmap getImage(int stateId, int position){
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        String cmd = "select * from entries where stateId = ?";
        if (addGroupBy){
            cmd += "ORDER BY year DESC, month DESC, day DESC";
        }
        Cursor cursor = sqLiteDatabase.rawQuery(cmd,new String[]{String.valueOf(stateId)});
        if(cursor.moveToPosition(position)){
            String img = cursor.getString(6);
            File file = context1.getFileStreamPath(img);
            Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
            Log.v("here", "imageGet");
            return bitmap;
        }
        return null;
    }

    int getEntryId(int stateId, int position){
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        String cmd = "select * from entries where stateId = ?";
        if (addGroupBy){
            cmd += "ORDER BY year DESC, month DESC, day DESC";
        }
        Cursor cursor = sqLiteDatabase.rawQuery(cmd,new String[]{String.valueOf(stateId)});
        if(cursor.moveToPosition(position)){
            int EntryId = cursor.getInt(0);
            return EntryId;
        }
        return 0;
    }

    void delete(int id){
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        sqLiteDatabase.delete("entries", "entryid = ?", new String[]{String.valueOf(id)});
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
