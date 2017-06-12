package com.votafore.songbook;

import android.app.Application;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Process;
import android.util.Config;
import android.util.Log;

import com.google.firebase.FirebaseApp;
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
import com.votafore.songbook.testrecview.AbstractExpandableDataProvider;
import com.votafore.songbook.testrecview.ExpandableDataProvider;


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

    String TAG_THREAD = "MyThread";

    public static FIreApp getInstance(){
        return mThis;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        FirebaseApp.initializeApp(getApplicationContext());

        mThis = this;

        mBase = new Base(getApplicationContext());

        root = FirebaseDatabase.getInstance().getReference();

        createDataProvider();

        setListeners();
    }


    /*********** LOCAL SQLITE DATABASE ***************/

    Base mBase;

    public Cursor getData(Fetcher params){

        SQLiteDatabase db = mBase.getReadableDatabase();

        Cursor c = db.query(params.tableName, params.fields, params.filter, params.filterArgs, null, null, params.orderBy);

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

        Log.v("Group", "addGroup");

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

        Log.v("Group", "removeGroup");

        SQLiteDatabase db = mBase.getWritableDatabase();

        db.delete(Base.TABLE_GROUP_CONTENT  , "group_id=?"  , new String[]{group.id});
        db.delete(Base.TABLE_GROUPS         , "id=?"        , new String[]{group.id});

        db.close();
    }

    public void updateGroup(Group group){

        Log.v("Group", "updateGroup");

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

    DatabaseReference root;

    String NODE_SONGS           = "songs";
    String NODE_GROUPS          = "groups";

    private List<Group> mGroupsToAdd;
    private List<Group> mGroupsToRemove;
    private List<Group> mGroupsToUpdate;


    private List<Song> mSongsToAdd;
    private List<Song> mSongsToRemove;
    private List<Song> mSongsToUpdate;

    public void loadSong(Song song, String groupKey){

        DatabaseReference newSongNode = root.child("songs").push();

        HashMap<String, String> data = new HashMap<>();

        data.put("title" , song.title);
        data.put("text"  , song.text);

        newSongNode.setValue(data);

        DatabaseReference newGroupItemNode = root.child("groups/"+groupKey+"/content").push();

        newGroupItemNode.setValue(newSongNode.getKey());
    }

    private void setListeners(){

        mGroupsToAdd    = new ArrayList<>();
        mGroupsToRemove = new ArrayList<>();
        mGroupsToUpdate = new ArrayList<>();

        mSongsToAdd     = new ArrayList<>();
        mSongsToRemove  = new ArrayList<>();
        mSongsToUpdate  = new ArrayList<>();



        root = FirebaseDatabase.getInstance().getReference();

        root.child(NODE_GROUPS).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                Log.v("FireAppBack", "Group onChildAdded");
                Group g = new Group(
                        dataSnapshot.getKey(),
                        dataSnapshot.child("title").getValue(String.class)
                );
                g.setNode(root.child(NODE_GROUPS).child(dataSnapshot.getKey()));

                mGroupsToAdd.add(g);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

                Log.v("FireAppBack", "Group onChildRemoved");

                mGroupsToRemove.add(new Group(
                        dataSnapshot.getKey(),
                        dataSnapshot.child("title").getValue(String.class)
                ));
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                Log.v("FireAppBack", "Group onChildChanged");

                Log.v(TAG_THREAD, String.format("onChildChanged: current thread id: %d",Process.myTid()));

                mGroupsToUpdate.add(new Group(
                        dataSnapshot.getKey(),
                        dataSnapshot.child("title").getValue(String.class))
                );
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        root.child(NODE_GROUPS).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                    SQLiteDatabase db = mBase.getWritableDatabase();

                    db.beginTransaction();

                    for(Group group: mGroupsToAdd){

                        Cursor cursor = db.query(Base.TABLE_GROUPS, null, "id=?", new String[]{group.id}, null, null, null);

                        if(cursor.getCount() > 0){
                            cursor.close();
                            mGroupsToUpdate.add(group);
                            continue;
                        }

                        ContentValues values = new ContentValues();

                        values.put("id", group.id);
                        values.put("title", group.title);

                        db.insert(Base.TABLE_GROUPS, null, values);

                    }

                    for(Group group: mGroupsToUpdate){

                        ContentValues values = new ContentValues();

                        values.put("title"  , group.title);

                        db.update(Base.TABLE_GROUPS, values, "id=?", new String[]{group.id});

                    }

                    for(Group group: mGroupsToRemove){

                        db.delete(Base.TABLE_GROUP_CONTENT  , "group_id=?"  , new String[]{group.id});
                        db.delete(Base.TABLE_GROUPS         , "id=?"        , new String[]{group.id});

                    }

                    try {
                        db.setTransactionSuccessful();
                    } finally {
                        db.endTransaction();
                    }

                    db.close();

                mGroupsToAdd.clear();
                mGroupsToRemove.clear();
                mGroupsToUpdate.clear();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



        root.child(NODE_SONGS).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                Log.v("FireAppBack", "Song onChildAdded");

                Song song;

                try {
                    song = dataSnapshot.getValue(Song.class);
                    song.id = dataSnapshot.getKey();

                    mSongsToAdd.add(song);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Log.v("FireAppBack", "Song onChildChanged");

                Song song;

                try {
                    song = dataSnapshot.getValue(Song.class);
                    song.id = dataSnapshot.getKey();

                    mSongsToUpdate.add(song);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Log.v("FireAppBack", "Song onChildRemoved");

                Song song;

                try {
                    song = dataSnapshot.getValue(Song.class);
                    song.id = dataSnapshot.getKey();

                    mSongsToRemove.add(song);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        root.child(NODE_SONGS).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                    SQLiteDatabase db = mBase.getWritableDatabase();

                    db.beginTransaction();

                    for(Song song: mSongsToAdd){

                        Cursor cursor = db.query(Base.TABLE_SONGS, null, "id=?", new String[]{song.id}, null, null, null);

                        if(cursor.getCount() > 0){
                            cursor.close();
                            mSongsToUpdate.add(song);
                            continue;
                        }

                        ContentValues values = new ContentValues();

                        values.put("id"     , song.id);
                        values.put("title"  , song.title);
                        values.put("content", song.text);

                        db.insert(Base.TABLE_SONGS, null, values);
                    }

                    for(Song song: mSongsToUpdate){

                        ContentValues values = new ContentValues();

                        values.put("title"  , song.title);
                        values.put("content", song.text);

                        db.update(Base.TABLE_SONGS, values, "id=?", new String[]{song.id});
                    }

                    for(Song song: mSongsToRemove){

                        db.delete(Base.TABLE_GROUP_CONTENT  , "song_id=?"  , new String[]{song.id});
                        db.delete(Base.TABLE_SONGS          , "id=?"        , new String[]{song.id});
                    }

                    try{
                        db.setTransactionSuccessful();
                    }finally {
                        db.endTransaction();
                    }

                    db.close();

                mSongsToAdd.clear();
                mSongsToUpdate.clear();
                mSongsToRemove.clear();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }










    /****** раздел еще тестируется *******/



    private AbstractExpandableDataProvider mProvider;

    public AbstractExpandableDataProvider getDataProvider(){
        return mProvider;
    }

    public void createDataProvider(){
        mProvider = new ExpandableDataProvider();
    }
}
