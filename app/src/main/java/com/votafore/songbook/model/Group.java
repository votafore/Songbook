package com.votafore.songbook.model;
import android.util.Log;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.votafore.songbook.FireApp;

/**
 * Created by User on 23.05.2017.
 *
 * Class represents a group of songs
 */

public class Group {

    public String id;
    public String title;

    private FireApp app;

    public Group(){}

    public Group(String id, String title) {
        this.id = id;
        this.title = title;

        app = FireApp.getInstance();
    }

    public void setNode(DatabaseReference node){

        node.child("content").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                app.addGroupItem(id, dataSnapshot.getValue(String.class));
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                app.removeGroupItem(id, dataSnapshot.getValue(String.class));
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

}
