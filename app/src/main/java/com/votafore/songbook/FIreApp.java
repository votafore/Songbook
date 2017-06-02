package com.votafore.songbook;

import android.app.Application;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.IBinder;
import android.util.Log;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.votafore.songbook.database.Base;
import com.votafore.songbook.database.Fetcher;
import com.votafore.songbook.database.SynchroService;
import com.votafore.songbook.firetestmodel.Group;
import com.votafore.songbook.firetestmodel.Song;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by User on 23.05.2017.
 *
 *
 */

public class FIreApp extends Application {

    private static FIreApp mThis;

    public static FIreApp getInstance(){
        return mThis;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        mThis = this;

        mBase = new Base(getApplicationContext());

        //startService(new Intent(getApplicationContext(), SynchroService.class));

        ServiceConnection srvConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {

            }

            @Override
            public void onServiceDisconnected(ComponentName name) {

            }
        };

        //bindService(new Intent(getApplicationContext(), SynchroService.class), srvConnection, Context.BIND_AUTO_CREATE);
    }


    /*********** LOCAL SQLITE DATABASE ***************/

    Base mBase;

    public Cursor getData(Fetcher params){

        SQLiteDatabase db = mBase.getReadableDatabase();

        Cursor c = db.query(params.tableName, params.fields, params.filter, params.filterArgs, null, null, null);

        return c;
    }



    public void addSong(Song song){

        // at first make sure that song had not been added to the database
        SQLiteDatabase db = mBase.getWritableDatabase();

        Cursor cursor = db.query(Base.TABLE_SONGS, null, "id=?", new String[]{song.id}, null, null, null);

        if(cursor.getCount() > 0){
            cursor.close();
            db.close();
            return;
        }

        ContentValues values = new ContentValues();

        values.put("id"     , song.id);
        values.put("title"  , song.title);
        values.put("content", song.text);

        db.insert(Base.TABLE_SONGS, null, values);

        db.close();
    }

    public void updateSong(Song song){

        SQLiteDatabase db = mBase.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put("title"  , song.title);
        values.put("content", song.text);

        db.update(Base.TABLE_SONGS, values, "id=?", new String[]{song.id});

        db.close();
    }

    public void removeSong(Song song){

        SQLiteDatabase db = mBase.getWritableDatabase();

        db.delete(Base.TABLE_GROUP_CONTENT  , "song_id=?"  , new String[]{song.id});
        db.delete(Base.TABLE_SONGS          , "id=?"        , new String[]{song.id});

        db.close();
    }



    public void addGroup(Group group){

        // at first make sure that song had not been added to the database
        SQLiteDatabase db = mBase.getWritableDatabase();

        Cursor cursor = db.query(Base.TABLE_GROUPS, null, "id=?", new String[]{group.id}, null, null, null);

        if(cursor.getCount() > 0){
            cursor.close();
            db.close();
            return;
        }

        ContentValues values = new ContentValues();

        values.put("id", group.id);
        values.put("title", group.title);

        db.insert(Base.TABLE_GROUPS, null, values);

        db.close();
    }

    public void removeGroup(Group group){

        SQLiteDatabase db = mBase.getWritableDatabase();

        db.delete(Base.TABLE_GROUP_CONTENT  , "group_id=?"  , new String[]{group.id});
        db.delete(Base.TABLE_GROUPS         , "id=?"        , new String[]{group.id});

        db.close();
    }

    public void updateGroup(Group group){

        SQLiteDatabase db = mBase.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put("title"  , group.title);

        db.update(Base.TABLE_GROUPS, values, "id=?", new String[]{group.id});

        db.close();
    }


    public void addGroupItem(String groupID, String songID){

        // at first make sure that song had not been added to the database
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



    public Cursor getSongsByGroup(String groupID){

        SQLiteDatabase db = mBase.getReadableDatabase();

        Cursor cursor = db.rawQuery("select " +
                                    " * " +
                                    "from " + Base.TABLE_SONGS + " " +
                                    "where " +
                                    "id in (select content.song_id from " + Base.TABLE_GROUP_CONTENT + " as content where content.group_id=?)"
                , new String[]{groupID});

        return cursor;
    }



    public void addChosenSong(String id){

        // at first make sure that song had not been added to the database
        SQLiteDatabase db = mBase.getWritableDatabase();

        Cursor cursor = db.query(Base.TABLE_CHOSEN, null, "song_id=?", new String[]{id}, null, null, null);

        if(cursor.getCount() > 0){
            cursor.close();
            db.close();
            return;
        }

        ContentValues values = new ContentValues();

        values.put("song_id" , id);

        db.insert(Base.TABLE_CHOSEN, null, values);

        db.close();
    }

    public void deleteChosen(String id){

        SQLiteDatabase db = mBase.getWritableDatabase();

        db.delete(Base.TABLE_CHOSEN  , "song_id=?"  , new String[]{id});

        db.close();
    }

    public Cursor getChosenSong(){

        SQLiteDatabase db = mBase.getReadableDatabase();

        Cursor c = db.rawQuery("select ch.song_id as id, songs.title from " + Base.TABLE_CHOSEN + " as ch inner join " + Base.TABLE_SONGS + " as songs on ch.song_id=songs.id", null);

        return c;
    }


    /************** FIREBASE DATABASE ************/

    public void loadSong(Song song, String groupKey){

        // TODO: доделать

//        DatabaseReference newSongNode = root.child(NODE_SONGS).push();
//
//        HashMap<String, String> data = new HashMap<>();
//
//        data.put("title" , song.title);
//        data.put("text"  , song.text);
//
//        newSongNode.setValue(data);
//
//        DatabaseReference newGroupItemNode = root.child(NODE_GROUPS+"/"+groupKey+"/content").push();
//
//        newGroupItemNode.setValue(newSongNode.getKey());
    }

}
