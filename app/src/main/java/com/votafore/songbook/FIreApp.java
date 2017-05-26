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
import com.votafore.songbook.database.Fetcher;
import com.votafore.songbook.firetestmodel.Group;
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

        setUpFireListeners();

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
    private String NODE_CONTENT         = "content";

    DatabaseReference root;
    DatabaseReference node_songs;
    DatabaseReference node_tags;

    private void setUpFireListeners(){

        root = FirebaseDatabase.getInstance().getReference();

        node_songs          = root.child(NODE_SONGS);
        node_tags           = root.child(NODE_TAGS);

        root.child(NODE_GROUPS).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for(DataSnapshot groupItem: dataSnapshot.getChildren()){

                    Group g = groupItem.getValue(Group.class);

                    g.key = groupItem.getKey();
                    g.setNode(root.child(NODE_GROUPS).child(groupItem.getKey()));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
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

        Cursor cursor = db.query("Songs", null, "id=?", new String[]{String.valueOf(song.id)}, null, null, null);

        if(cursor.getCount() > 0){
            db.close();
            return;
        }

        ContentValues values = new ContentValues();

        values.put("id"     , song.id);
        values.put("title"  , song.title);
        values.put("content", song.content);

        db.insert("Songs", null, values);

        db.close();
    }

    private void updateSong(Song song){

        SQLiteDatabase db = mBase.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put("title"  , song.title);
        values.put("content", song.content);

        db.update(Base.TABLE_SONGS, values, "id=?", new String[]{String.valueOf(song.id)});

        db.close();
    }

    private void removeSong(Song song){

        SQLiteDatabase db = mBase.getWritableDatabase();

        db.delete(Base.TABLE_GROUP_CONTENT  , "song_id=?"  , new String[]{String.valueOf(song.id)});
        db.delete(Base.TABLE_SONGS          , "id=?"        , new String[]{String.valueOf(song.id)});

        db.close();
    }



    private void addGroup(Group group){

        // at first make sure that song had not been added to the database
        SQLiteDatabase db = mBase.getWritableDatabase();

        Cursor cursor = db.query(Base.TABLE_GROUPS, null, "id=?", new String[]{String.valueOf(group.id)}, null, null, null);

        if(cursor.getCount() > 0){
            db.close();
            return;
        }

        ContentValues values = new ContentValues();

        values.put("id", group.id);
        values.put("title", group.title);

        db.insert(Base.TABLE_GROUPS, null, values);

        db.close();
    }

    private void removeGroup(int groupID){

        SQLiteDatabase db = mBase.getWritableDatabase();

        db.delete(Base.TABLE_GROUP_CONTENT  , "group_id=?"  , new String[]{String.valueOf(groupID)});
        db.delete(Base.TABLE_GROUPS         , "id=?"        , new String[]{String.valueOf(groupID)});

        db.close();
    }



    private void addGroupItem(int groupID, int songID){

        // at first make sure that song had not been added to the database
        SQLiteDatabase db = mBase.getWritableDatabase();

        Cursor cursor = db.query(Base.TABLE_GROUP_CONTENT, null, "group_id=? and song_id=?", new String[]{String.valueOf(groupID), String.valueOf(songID)}, null, null, null);

        if(cursor.getCount() > 0){
            db.close();
            return;
        }

        ContentValues values = new ContentValues();

        values.put("group_id", groupID);
        values.put("song_id", songID);

        db.insert(Base.TABLE_GROUP_CONTENT, null, values);

        db.close();
    }

    private void updateGroupItem(int groupID, int songID){

//        SQLiteDatabase db = mBase.getWritableDatabase();
//
//        Cursor cursor = db.query(Base.TABLE_GROUP_CONTENT, null, "group_id=? and song_id=?", new String[]{String.valueOf(groupID), String.valueOf(songID)}, null, null, null);
//
//        ContentValues values = new ContentValues();
//
//        if(cursor.getCount() > 0){
//
//            values.put("song_id", songID);
//
//            db.update(Base.TABLE_GROUP_CONTENT, values, "group_id=?", new String[]{String.valueOf(groupID)});
//
//            db.close();
//
//            return;
//        }
//
//        values.put("group_id", groupID);
//
//        db.insert(Base.TABLE_GROUP_CONTENT, null, values);
//
//        db.close();

        SQLiteDatabase db = mBase.getWritableDatabase();


    }



    public Cursor getSongsByGroup(int groupID){

        SQLiteDatabase db = mBase.getReadableDatabase();

        Cursor cursor = db.rawQuery("select " +
                                    " * " +
                                    "from " + Base.TABLE_SONGS + " " +
                                    "where " +
                                    "id in (select content.song_id from " + Base.TABLE_GROUP_CONTENT + " as content where content.group_id=?)"
                , new String[]{String.valueOf(groupID)});

        return cursor;
    }

}
