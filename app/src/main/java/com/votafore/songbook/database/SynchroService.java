package com.votafore.songbook.database;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Messenger;
import android.os.Process;
import android.util.Log;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.votafore.songbook.FIreApp;
import com.votafore.songbook.firetestmodel.Group;
import com.votafore.songbook.firetestmodel.Song;

import java.util.ArrayList;
import java.util.List;

public class SynchroService extends Service {

    // TODO: выяснить необходимость постоянного наличия отдельного потока (учитывая редкость операций)
    // разобраться с этим вопросом. Возможно необходимость в сервисе отпадет.

    private ServiceHandler mHandler;

    private Messenger messenger;

    public SynchroService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return messenger.getBinder();
    }

    @Override
    public void onCreate() {

        Log.v("SynchroService", "onCreate");

        HandlerThread mThread = new HandlerThread("SynchroService", Process.THREAD_PRIORITY_BACKGROUND);

        mThread.start();

        mHandler = new ServiceHandler(mThread.getLooper());

        messenger = new Messenger(mHandler);
    }

    @Override
    public int onStartCommand(Intent intent,  int flags, int startId) {

        Log.v("SynchroService", "onStartCommand");

        // отправляем пустое сообщение (просто для запуска синхронизации)
        Message msg = mHandler.obtainMessage();
        msg.obj = new Song();
        mHandler.sendMessage(msg);

        return Service.START_NOT_STICKY;
        //IntDef(value = {Service.START_FLAG_REDELIVERY, Service.START_FLAG_RETRY}, flag = true)
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }





    /***************** BACKGROUND **********/

    private class ServiceHandler extends Handler{

        private String NODE_SONGS           = "songs";
        private String NODE_GROUPS          = "groups";

        DatabaseReference root;

        private List<Group> mGroupsToAdd;
        private List<Group> mGroupsToRemove;
        private List<Group> mGroupsToUpdate;


        private List<Song> mSongsToAdd;
        private List<Song> mSongsToRemove;
        private List<Song> mSongsToUpdate;

        private ServiceHandler(Looper looper){
            super(looper);

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

        @Override
        public void handleMessage(Message msg) {

            root = FirebaseDatabase.getInstance().getReference();

            root.child(NODE_GROUPS).addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                    Log.v("SynchroService", "Group onChildAdded");
                    Group g = new Group(
                            dataSnapshot.getKey(),
                            dataSnapshot.child("title").getValue(String.class)
                    );
                    g.setNode(root.child(NODE_GROUPS).child(dataSnapshot.getKey()));

                    mGroupsToAdd.add(g);
                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {

                    Log.v("SynchroService", "Group onChildRemoved");

                    mGroupsToRemove.add(new Group(
                            dataSnapshot.getKey(),
                            dataSnapshot.child("title").getValue(String.class)
                    ));
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                    Log.v("SynchroService", "Group onChildChanged");

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

                    for(Group group: mGroupsToAdd){
                        FIreApp.getInstance().addGroup(group);
                    }

                    for(Group group: mGroupsToUpdate){
                        FIreApp.getInstance().updateGroup(group);
                    }

                    for(Group group: mGroupsToRemove){
                        FIreApp.getInstance().removeGroup(group);
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
                        FIreApp.getInstance().addSong(song);
                    }

                    for(Song song: mSongsToUpdate){
                        FIreApp.getInstance().updateSong(song);
                    }

                    for(Song song: mSongsToRemove){
                        FIreApp.getInstance().removeSong(song);
                    }

                    mSongsToAdd.clear();
                    mSongsToRemove.clear();
                    mSongsToUpdate.clear();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });



            super.handleMessage(msg);
        }
    }
}
