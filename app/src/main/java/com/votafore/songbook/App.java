package com.votafore.songbook;

import android.app.Application;
import android.content.ContentValues;
import android.database.ContentObservable;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.votafore.songbook.database.Base;
import com.votafore.songbook.database.Fetcher;
import com.votafore.songbook.support.ListItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

public class App extends Application {

    private List<ListItem> mChosenSongs;
    private Base mDataBase;

    private int mCounter;

    @Override
    public void onCreate() {
        super.onCreate();

        mChosenSongs = new ArrayList<>();
        mDataBase    = new Base(getApplicationContext());
        mCounter     = 0;
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

        Cursor c = db.rawQuery("select ch.song_id as id, songs.title from ChosenSongs as ch inner join Songs as songs on ch.song_id=songs.id", null);

        return c;
    }

    public void deleteChosen(String id){

        SQLiteDatabase db = mDataBase.getReadableDatabase();

        db.delete("ChosenSongs", "song_id=?", new String[]{String.valueOf(id)});
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
