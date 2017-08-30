package com.votafore.songbook;

import android.app.Application;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.google.firebase.FirebaseApp;
import com.votafore.songbook.database.Base;
import com.votafore.songbook.database.Fetcher;


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

    public void addGroupItem(String groupID, String songID){

        // at first make sure that song has not been added into database
        SQLiteDatabase db = mBase.getWritableDatabase();

        Cursor cursor = db.query(Base.TABLE_GROUP_CONTENT, null, "group_id=? and song_id=?", new String[]{groupID, songID}, null, null, null);

        if(cursor.getCount() > 0){
            cursor.close();
            db.close();
            return;
        }

        ContentValues values = new ContentValues();

        values.put("group_id", groupID);
        values.put("song_id", songID);

        db.insert(Base.TABLE_GROUP_CONTENT, null, values);

        db.close();
    }

    public void removeGroupItem(String group_id, String song_id){

        SQLiteDatabase db = mBase.getWritableDatabase();

        db.delete(Base.TABLE_GROUP_CONTENT, "group_id=? and song_id=?", new String[]{group_id, song_id});

        db.close();
    }

}
