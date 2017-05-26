package com.votafore.songbook.clasver2;

import android.support.v4.view.ViewPager;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

/**
 * Created by User on 25.05.2017.
 *
 * Class represents one of root nodes
 */

public class FBNode {

    private DatabaseReference mNode;

    public FBNode(DatabaseReference ref){

        mNode = ref;

        ChildEventListener listener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                if(mAddListener != null)
                    mAddListener.onChildAdded();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                if(mChangeListener != null)
                    mChangeListener.onChildChanged();
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

                if(mRemoveListener != null)
                    mRemoveListener.onChildRemoved();
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        mNode.addChildEventListener(listener);
    }




    /************* Interfaces *****************/

    private OnChildAddListener mAddListener;

    public void setAddListener(OnChildAddListener addListener){
        mAddListener = addListener;
    }

    public interface OnChildAddListener{
        void onChildAdded();
    }



    private OnChildChangedListener mChangeListener;

    public void setChangeListener(OnChildChangedListener changeListener){
        mChangeListener = changeListener;
    }

    public interface OnChildChangedListener{
        void onChildChanged();
    }



    private OnChildRemovedListener mRemoveListener;

    public void setChangeListener(OnChildRemovedListener removedListener){
        mRemoveListener = removedListener;
    }

    public interface OnChildRemovedListener{
        void onChildRemoved();
    }
}
