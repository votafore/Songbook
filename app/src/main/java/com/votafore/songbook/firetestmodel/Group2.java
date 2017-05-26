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

public class Group2 extends GroupAbs{

    public int id;
    public String title;
    public String key;

    public Group2(){}

    public Group2(int id, String title) {
        this.id = id;
        this.title = title;
    }

    public void setNode(DatabaseReference node){

        node.child("content").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.v("GroupABS", "onChildAdded");
                // TODO: handling of song adding
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Log.v("GroupABS", "onChildRemoved");
                // TODO: handling of song removing
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Log.v("GroupABS", "onChildChanged");
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
}
