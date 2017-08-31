package com.votafore.songbook;

import android.app.Application;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.google.firebase.FirebaseApp;
import com.votafore.songbook.database.Base;
import com.votafore.songbook.database.Fetcher;
import com.votafore.songbook.model.Song;


public class FireApp extends Application {

    private static FireApp mThis;

    public static FireApp getInstance(){
        return mThis;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        FirebaseApp.initializeApp(getApplicationContext());

        mThis = this;

        mBase = new Base(getApplicationContext());
    }


    /*********** LOCAL SQLITE DATABASE ***************/

    Base mBase;

    public Cursor getData(Fetcher params){

        SQLiteDatabase db = mBase.getReadableDatabase();

        return db.query(params.tableName, params.fields, params.filter, params.filterArgs, null, null, params.orderBy);
    }

    public void addSong(Song song){

        // at first make sure that song had not been added into the database
        if(songIsLoaded(song))
            return;

        SQLiteDatabase db = mBase.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put("id"     , song.id);
        values.put("title"  , song.title);
        values.put("content", song.text);

        db.insert(Base.TABLE_SONGS, null, values);

        db.close();
    }

    public boolean songIsLoaded(Song song){

        SQLiteDatabase db = mBase.getReadableDatabase();

        Cursor cursor = db.query(Base.TABLE_SONGS, null, "id=?", new String[]{song.id}, null, null, null);

        boolean result = cursor.getCount() > 0;

        cursor.close();
        db.close();

        return result;
    }

}
