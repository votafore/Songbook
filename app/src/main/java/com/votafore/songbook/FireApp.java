package com.votafore.songbook;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Process;
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
import com.votafore.songbook.model.Group;
import com.votafore.songbook.model.Song;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class FireApp extends Application {

    private static FireApp mThis;

    String TAG_THREAD = "MyThread";

    public static FireApp getInstance(){
        return mThis;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        FirebaseApp.initializeApp(getApplicationContext());

        mThis = this;

        mBase = new Base(getApplicationContext());

        root = FirebaseDatabase.getInstance().getReference();

        setListeners();

        startWatching();
    }


    /*********** LOCAL SQLITE DATABASE ***************/

    Base mBase;

    public Cursor getData(Fetcher params){

        SQLiteDatabase db = mBase.getReadableDatabase();

        Cursor c = db.query(params.tableName, params.fields, params.filter, params.filterArgs, null, null, params.orderBy);

        return c;
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

                Log.v(ActivityMain.TAG, "FireApp: onDataChange (complete)");

                if(completeListener != null)
                    completeListener.onUpdateComplete();

                isStarted = true;

                completeListener = null;

                if(receiver != null){
                    unregisterReceiver(receiver);
                    receiver = null;
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
















    /*********** раздел тестируется ***********/

    private onSongDownloadCompleteListener completeListener;

    public void setOnDownloadCompleteListener(onSongDownloadCompleteListener completeListener){

        Log.v(ActivityMain.TAG, "FireApp: setOnDownloadCompleteListener");

        if(isStarted)
            return;

        this.completeListener = completeListener;

        if(isConnectionAvailable)
            this.completeListener.onUpdateStart();
    }

    public interface onSongDownloadCompleteListener{
        void onUpdateComplete();
        void onUpdateStart();
    }





    private boolean isStarted = false;












    private NetworkChangeReceiver receiver;
    private boolean isConnectionAvailable = false;

    private void checkConnection(){

        Log.v(ActivityMain.TAG, "FireApp: checkConnection");

        isConnectionAvailable = false;

        final ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        final NetworkInfo networkInfo = cm.getActiveNetworkInfo();

        isConnectionAvailable = networkInfo != null && networkInfo.isConnected();

        if(completeListener != null && !isStarted && isConnectionAvailable)
            completeListener.onUpdateStart();
    }

    private void startWatching(){

        Log.v(ActivityMain.TAG, "FireApp: startWatching");

        receiver = new NetworkChangeReceiver();

        IntentFilter filter = new IntentFilter();
        filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        registerReceiver(receiver, filter);
    }

    public class NetworkChangeReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(final Context context, final Intent intent) {
            checkConnection();
        }
    }

}
