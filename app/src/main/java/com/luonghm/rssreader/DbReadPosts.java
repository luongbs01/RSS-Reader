package com.luonghm.rssreader;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DbReadPosts extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "myDatabase.db";
    static final String TABLE_NAME1 = "READ_POSTS";
    static final String TABLE_NAME2 = "SAVED_POSTS";
    private static final int DATABASE_VERSION = 7;

    public static final String TITLE = "title";
    public static final String IMAGE = "image";
    public static final String PUBDATE = "pubDate";
    public static final String LINK = "link";

    public DbReadPosts(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String queryCreateTable1 = "CREATE TABLE " + TABLE_NAME1 + " ( " +
                TITLE + " VARCHAR(255) NOT NULL, " +
                IMAGE + " VARCHAR(255) NOT NULL, " +
                PUBDATE + " VARCHAR(255) NOT NULL, " +
                LINK + " VARCHAR(255) PRIMARY KEY)";
        String queryCreateTable2 = "CREATE TABLE " + TABLE_NAME2 + " ( " +
                TITLE + " VARCHAR(255) NOT NULL, " +
                IMAGE + " VARCHAR(255) NOT NULL, " +
                PUBDATE + " VARCHAR(255) NOT NULL, " +
                LINK + " VARCHAR(255) PRIMARY KEY)";
        db.execSQL(queryCreateTable1);
        db.execSQL(queryCreateTable2);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME1);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME2);
        onCreate(db);
    }

    public List<RSSModel> getAll(String TABLE_NAME) {
        List<RSSModel> rssModelList = new ArrayList<>();

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);

        cursor.moveToFirst();
        while(!cursor.isAfterLast()) {
            String title = cursor.getString(0);
            String image = cursor.getString(1);
            String pubDate = cursor.getString(2);
            String link = cursor.getString(3);
            rssModelList.add(new RSSModel(title, image, pubDate, link));

            cursor.moveToNext();
        }
        cursor.close();
        return rssModelList;
    }

    public void insert(RSSModel rssModel, String TABLE_NAME) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("INSERT INTO " + TABLE_NAME + " ( " + TITLE + ", "+ IMAGE + ", "+ PUBDATE + ", "+ LINK + ") VALUES (?, ?, ?, ?)",
                new String[] {rssModel.getTitle(), rssModel.getImage(), rssModel.getPubDate(), rssModel.getLink()});
    }

    public boolean check(RSSModel rssModel, String TABLE_NAME) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + LINK + " FROM " + TABLE_NAME, null);

        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            if (cursor.getString(0).equalsIgnoreCase(rssModel.getLink())) {
                cursor.close();
                return false;
            }
            cursor.moveToNext();
        }
        cursor.close();
        return true;
    }

    public void delete(RSSModel rssModel, String TABLE_NAME) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_NAME + " where " + LINK + " = ?", new String[] {rssModel.getLink()});
    }

    public void deleteAll(String TABLE_NAME) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        String queryCreateTable = "CREATE TABLE " + TABLE_NAME + " ( " +
                TITLE + " VARCHAR(255) NOT NULL, " +
                IMAGE + " VARCHAR(255) NOT NULL, " +
                PUBDATE + " VARCHAR(255) NOT NULL, " +
                LINK + " VARCHAR(255) PRIMARY KEY)";
        db.execSQL(queryCreateTable);
    }
}
