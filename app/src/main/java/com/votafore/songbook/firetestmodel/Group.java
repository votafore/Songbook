package com.votafore.songbook.firetestmodel;

import android.util.Log;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

/**
 * Created by User on 23.05.2017.
 *
 * Class represents a group of songs
 */

public class Group {

    public int id;
    public String title;
    public String key;

    public Group(){}

    public Group(int id, String title, String key) {
        this.id = id;
        this.title = title;
        this.key = key;
    }

    public void setNode(DatabaseReference node){

        node.child("content").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.v("Group", "onChildAdded");
                // TODO: handling of song adding
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Log.v("Group", "onChildRemoved");
                // TODO: handling of song removing
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Log.v("Group", "onChildChanged");
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                Log.v("Group", "onChildMoved");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.v("Group", "onCancelled");
            }
        });
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
