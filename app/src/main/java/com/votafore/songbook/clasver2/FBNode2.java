package com.votafore.songbook.clasver2;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 25.05.2017.
 *
 * Class represents one of root nodes
 */

public abstract class FBNode2 {

    protected DatabaseReference mNode;
    protected ChildEventListener nodeListener;

    public FBNode2(DatabaseReference ref){

        mNode = ref;

        mNode.addChildEventListener(defineListener());

        /**
         * для отслеживания окончания загрузки изменений
         */
        mNode.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                storeData();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        subnodes = new ArrayList<>();

        defineChildListener();
    }

    protected abstract ChildEventListener defineListener();
    protected abstract void defineChildListener();
    protected abstract void storeData();

    /**
     * предполагается что в каждом теге будет список
     * поэтому нужен список для объектов нода
     */

    protected List<DatabaseReference> subnodes;
}
