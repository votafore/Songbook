package com.votafore.songbook.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class Base extends SQLiteOpenHelper {

    private static int      VERSION = 5;
    private static String   DB_NAME = "DBSongs.db";
    public static int       ID_GROUP12 = 1;
    public static int       ID_GROUP34 = 2;
    public static int       ID_GROUP56 = 3;

    public Base(Context context) {
        super(context, DB_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("create table Songs (id integer primary key autoincrement, group_id integer, title text)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("drop table Songs");

        onCreate(db);
    }
}
