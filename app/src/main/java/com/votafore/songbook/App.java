package com.votafore.songbook;

import android.app.Application;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.votafore.songbook.database.Base;
import com.votafore.songbook.database.Fetcher;
import com.votafore.songbook.support.ListItem;

import java.util.ArrayList;
import java.util.List;

public class App extends Application {

    private List<ListItem> mChosenSongs;
    private Base mDataBase;

    private static App mThis;

    private int mCounter;

    @Override
    public void onCreate() {
        super.onCreate();

        mThis = this;

        mChosenSongs = new ArrayList<>();
        mDataBase    = new Base(getApplicationContext());
        mCounter     = 0;
    }

    public static App getInstance(){
        return mThis;
    }

    public void addSong(ListItem item){

        SQLiteDatabase db = mDataBase.getWritableDatabase();

        Cursor c = db.query("ChosenSongs", null, "song_id=?", new String[]{String.valueOf(item.id)}, null, null, "sort");

        if(c.getCount() > 0)
            return;

        Log.v("message", "Добавили пестню");

        ContentValues values;
        values = new ContentValues();
        values.put("song_id", item.id);
        values.put("sort", mCounter);

        db.insert("ChosenSongs", null, values);

        mCounter++;
    }

    public Cursor getChosenSong(){

        SQLiteDatabase db = mDataBase.getReadableDatabase();

        Cursor c = db.rawQuery("select ch.song_id is id, songs.title from ChosenSongs as ch inner join Songs as songs on ch.song_id=songs.id", null);

        return c;
    }

    public Cursor getData(Fetcher parameters){

        SQLiteDatabase db = mDataBase.getReadableDatabase();

        Cursor c = db.query(parameters.tableName, parameters.fields, parameters.filter, parameters.filterArgs, null, null, null);

        return c;
    }

    public void saveSong(String title, String text, int group){

        SQLiteDatabase db = mDataBase.getReadableDatabase();

        ContentValues values = new ContentValues();

        values.put("title", title);
        values.put("group_id", group);
        values.put("content", text);

        db.insert("Songs", null, values);
    }
}
