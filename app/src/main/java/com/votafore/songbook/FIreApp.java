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
import com.votafore.songbook.firetestmodel.GroupAbs;
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

        setUpFireListeners();

        mBase = new Base(getApplicationContext());

        // TODO: возможно имеет смысл все эти слушатели подключить по нажатию кнопки
        // т.к. это можно использовать как спосбо чтения данных
        // т.е. нажал, прочитал, синхронизировал и все.
        // если надо еще раз синхронизировать, то еще раз нажал

        mGroupsToAdd    = new ArrayList<>();
        mGroupsToRemove = new ArrayList<>();
        mGroupsToUpdate = new ArrayList<>();


        // TODO: надо определится в каком порядке выполяются операции добавления, редактирования, удаления.
        mSongsToAdd     = new ArrayList<>();
        mSongsToRemove  = new ArrayList<>();
        mSongsToUpdate  = new ArrayList<>();
    }

    /*********** FIRE DATABASE REFS ***************/

    private String NODE_SONGS           = "songs";
    private String NODE_GROUPS          = "groups";

    DatabaseReference root;

    private List<GroupAbs> mGroupsToAdd;
    private List<GroupAbs> mGroupsToRemove;
    private List<GroupAbs> mGroupsToUpdate;


    private List<Song> mSongsToAdd;
    private List<Song> mSongsToRemove;
    private List<Song> mSongsToUpdate;

    private void setUpFireListeners(){

        root = FirebaseDatabase.getInstance().getReference();



        root.child(NODE_GROUPS).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                GroupAbs g = new GroupAbs() {
                    @Override
                    public void setNode(DatabaseReference node) {

                        node.child("content").addChildEventListener(new ChildEventListener() {
                            @Override
                            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                                Log.v("GroupABS", "onChildAdded");
                                // TODO: handling of song adding

                                addGroupItem(id, dataSnapshot.getKey());
                            }

                            @Override
                            public void onChildRemoved(DataSnapshot dataSnapshot) {
                                Log.v("GroupABS", "onChildRemoved");
                                // TODO: handling of song removing

                                removeGroupItem(id, dataSnapshot.getKey());
                            }

                            @Override
                            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                                Log.v("GroupABS", "onChildChanged");

                                //updateGroupItem(id, dataSnapshot.getChildren().iterator().next().getValue(Integer.class));
                            }

                            @Override
                            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                                Log.v("GroupABS", "onChildMoved");
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                Log.v("GroupABS", "onCancelled");
                            }
                        });
                    }
                };

                g.setId(dataSnapshot.getKey());

                for(DataSnapshot field: dataSnapshot.getChildren()){

                    if(field.getKey().equals("title"))
                        g.setTitle(field.getValue(String.class));
                }

                g.setNode(root.child(NODE_GROUPS).child(dataSnapshot.getKey()));

                mGroupsToAdd.add(g);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

                GroupAbs g = new GroupAbs() {
                    @Override
                    public void setNode(DatabaseReference node) {

                    }
                };

                g.setId(dataSnapshot.getKey());
                g.setTitle(dataSnapshot.getValue(Group.class).getTitle());

                mGroupsToRemove.add(g);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                GroupAbs g = new GroupAbs() {
                    @Override
                    public void setNode(DatabaseReference node) {

                    }
                };

                g.setId(dataSnapshot.getKey());
                g.setTitle(dataSnapshot.getValue(Group.class).getTitle());

                mGroupsToUpdate.add(g);
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

                for(GroupAbs group: mGroupsToAdd){
                    addGroup(group);
                }

                for(GroupAbs group: mGroupsToUpdate){
                    updateGroup(group);
                }

                for(GroupAbs group: mGroupsToRemove){
                    removeGroup(group);
                }

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
                Log.v("GroupABS", "song added");

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
                Log.v("GroupABS", "song changed");

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
                Log.v("GroupABS", "song removed");

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

                for(Song song: mSongsToAdd){
                    addSong(song);
                }

                for(Song song: mSongsToUpdate){
                    updateSong(song);
                }

                for(Song song: mSongsToRemove){
                    removeSong(song);
                }

                mSongsToAdd.clear();
                mSongsToRemove.clear();
                mSongsToUpdate.clear();
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

        Cursor cursor = db.query("Songs", null, "id=?", new String[]{song.id}, null, null, null);

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

    private void updateSong(Song song){

        SQLiteDatabase db = mBase.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put("title"  , song.title);
        values.put("content", song.text);

        db.update(Base.TABLE_SONGS, values, "id=?", new String[]{song.id});

        db.close();
    }

    private void removeSong(Song song){

        SQLiteDatabase db = mBase.getWritableDatabase();

        db.delete(Base.TABLE_GROUP_CONTENT  , "song_id=?"  , new String[]{song.id});
        db.delete(Base.TABLE_SONGS          , "id=?"        , new String[]{song.id});

        db.close();
    }



    private void addGroup(GroupAbs group){

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

    private void removeGroup(GroupAbs group){

        SQLiteDatabase db = mBase.getWritableDatabase();

        db.delete(Base.TABLE_GROUP_CONTENT  , "group_id=?"  , new String[]{group.id});
        db.delete(Base.TABLE_GROUPS         , "id=?"        , new String[]{group.id});

        db.close();
    }

    private void updateGroup(GroupAbs group){

        SQLiteDatabase db = mBase.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put("title"  , group.title);

        db.update(Base.TABLE_GROUPS, values, "id=?", new String[]{group.id});

        db.close();
    }


    private void addGroupItem(String groupID, String songID){

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

    private void removeGroupItem(String group_id, String song_id){

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



    /************** FIREBASE DATABASE ************/

    public void loadSong(Song song, String groupKey){

        DatabaseReference newSongNode = root.child(NODE_SONGS).push();

        HashMap<String, String> data = new HashMap<>();

        data.put("title"    , song.title);
        data.put("content"  , song.text);

        newSongNode.setValue(data);
    }

}
