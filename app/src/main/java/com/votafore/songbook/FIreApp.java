package com.votafore.songbook;

import android.app.Application;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.votafore.songbook.database.Base;
import com.votafore.songbook.firetestmodel.Song;

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

        root = FirebaseDatabase.getInstance().getReference();

        node_song       = root.child(NODE_SONGS);
        node_tags       = root.child(NODE_TAGS);
        node_groups     = root.child(NODE_GROUPS);
        mLastInsertedID = root.child(NODE_LASTINSERTEDID);

        node_song.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.v("LogMessage", "node_song: onChildAdded");
                //addSong(dataSnapshot.getValue(Song.class));
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Log.v("LogMessage", "node_song: onChildChanged");
                // TODO: сделать сохранение изменений (хотя не понятно какие изменения могут быть)
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Log.v("LogMessage", "node_song: onChildRemoved");
                // TODO: удалить запись из локальной базы данных
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        node_groups.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.v("LogMessage", "node_groups: onChildAdded");
                // TODO: add a new group to local database
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Log.v("LogMessage", "node_groups: onChildChanged");
                // TODO: update group in local database
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Log.v("LogMessage", "node_groups: onChildRemoved");
                // TODO: remove group from local base
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        node_tags.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.v("LogMessage", "node_tags: onChildAdded");
                // TODO: add tag in database
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Log.v("LogMessage", "node_tags: onChildChanged");
                // TODO: update tag info
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Log.v("LogMessage", "node_tags: onChildRemoved");
                // TODO: remove tag from database
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        // get new value when lastID is changed
        mLastInsertedID.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.v("LogMessage", "mLastInsertedID: onDataChange");
                mLastID = dataSnapshot.getValue(Integer.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mBase = new Base(getApplicationContext());

        // TODO: возможно имеет смысл все эти слушатели подключить по нажатию кнопки
        // т.к. это можно использовать как спосбо чтения данных
        // т.е. нажал, прочитал, синхронизировал и все.
        // если надо еще раз синхронизировать, то еще раз нажал
    }



    /*********** FIRE DATABASE REFS ***************/

    private String NODE_SONGS           = "songs";
    private String NODE_TAGS            = "tags";
    private String NODE_GROUPS          = "groups";
    private String NODE_LASTINSERTEDID  = "lastID";

    DatabaseReference root;
    DatabaseReference node_song;
    DatabaseReference node_tags;
    DatabaseReference node_groups;

    DatabaseReference mLastInsertedID;

    private int mLastID;


    /*********** LOCAL SQLITE DATABASE ***************/

    Base mBase;

    public void addSong(Song song){

        // at first make sure that song had not been added to the database
        SQLiteDatabase db = mBase.getReadableDatabase();

        Cursor cursor = db.query("Songs", null, "id=?", new String[]{String.valueOf(song.id)}, null, null, null);

        if(cursor.getCount() > 0){
            db.close();
            return;
        }

        db = mBase.getWritableDatabase();

        ContentValues values = new ContentValues();

        // TODO: сделать вставку ИДа, для этого надо убрать primarykey в базе данных
        values.put("title", song.title);
        values.put("content", song.content);

        db.insert("Songs", null, values);

        db.close();
    }
}
