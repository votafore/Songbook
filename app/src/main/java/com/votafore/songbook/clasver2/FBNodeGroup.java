package com.votafore.songbook.clasver2;

import android.util.Log;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.votafore.songbook.firetestmodel.Group;

/**
 * Created by User on 25.05.2017.
 */

public class FBNodeGroup extends FBNode2 {

    public FBNodeGroup(DatabaseReference ref) {
        super(ref);
    }

    @Override
    protected ChildEventListener defineListener() {

        ChildEventListener listener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                subnodes.add(mNode.child(dataSnapshot.getKey()));

                mNode.child(dataSnapshot.getKey()).child("content").addChildEventListener(nodeListener);

                Log.v("FBNodeGroup", "onChildAdded");
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Log.v("FBNodeGroup", "onChildChanged");
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Log.v("FBNodeGroup", "onChildRemoved");
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        return listener;
    }

    @Override
    protected void defineChildListener() {

        nodeListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.v("FBNodeGroup", "nodeListener: onChildAdded");
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Log.v("FBNodeGroup", "nodeListener: onChildChanged");
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Log.v("FBNodeGroup", "nodeListener: onChildRemoved");
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                Log.v("FBNodeGroup", "nodeListener: onChildMoved");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.v("FBNodeGroup", "nodeListener: onCancelled");
            }
        };
    }

    @Override
    protected void storeData() {
        Log.v("FBNodeGroup", String.format("storeData: we have got %d nodes", subnodes.size()));
    }


}
