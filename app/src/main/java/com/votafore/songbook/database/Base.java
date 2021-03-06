package com.votafore.songbook.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class Base extends SQLiteOpenHelper {

    private static int      VERSION = 25;
    private static String   DB_NAME = "DBSongs.db";

    public static String TABLE_GROUPS         = "Groups";
    public static String TABLE_GROUP_CONTENT  = "GroupContent";
    public static String TABLE_SONGS          = "Songs";
    public static String TABLE_CHOSEN         = "ChosenSongs";

    public Base(Context context) {
        super(context, DB_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("create table " +  TABLE_SONGS           + " (id text, title text, content text)");
        db.execSQL("create table " +  TABLE_GROUPS          + " (id text, title text)");
        db.execSQL("create table " +  TABLE_CHOSEN          + " (song_id integer, sort integer)");
        db.execSQL("create table " +  TABLE_GROUP_CONTENT   + " (group_id text, song_id text)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("drop table if exists " +  TABLE_SONGS);
        db.execSQL("drop table if exists " +  TABLE_CHOSEN);
        db.execSQL("drop table if exists " +  TABLE_GROUPS);
        db.execSQL("drop table if exists " +  TABLE_GROUP_CONTENT);

        onCreate(db);
    }
}
