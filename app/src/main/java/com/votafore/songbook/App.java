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

    private List<ListItem> mChoosenSongs;
    private Base mDataBase;

    private static App mThis;

    @Override
    public void onCreate() {
        super.onCreate();

        mThis = this;

        mChoosenSongs   = new ArrayList<>();
        mDataBase       = new Base(getApplicationContext());
    }

    public static App getInstance(){
        return mThis;
    }

    public void addSong(ListItem item){

        if(mChoosenSongs.contains(item))
            return;

        Log.v("message", "Добавили пестню");

        mChoosenSongs.add(item);
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
