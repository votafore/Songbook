package com.votafore.songbook;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.votafore.songbook.firetestmodel.Song;

import java.util.ArrayList;
import java.util.List;

public class FireActivityMain extends AppCompatActivity {

    private DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    private DatabaseReference songList = mRootRef.child("songs");

    private List<Song> mList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        mList = new ArrayList<>();
    }

    @Override
    protected void onStart() {
        super.onStart();

        songList.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.v("message", "onChildAdded");

                Song sn = dataSnapshot.getValue(Song.class);

                if(s == null)
                    return;

                Log.v("message", s);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Log.v("message", "onChildChanged");
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Log.v("message", "onChildRemoved");
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                Log.v("message", "onChildMoved");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.v("message", "onCancelled");
            }
        });

//        songList.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//
//                List<Song> list = new ArrayList<>();
//
//                for(DataSnapshot song: dataSnapshot.getChildren()){
//
//                    Log.v("message", "cheers, we've got a song");
//
//                    Song s = new Song();
//                    s.setId(song.child("-2sdf442we14").getValue(Song.class).getId());
//                    s.setTitle(song.child("-2sdf442we14").getValue(Song.class).getTitle());
//                    s.setContent(song.child("-2sdf442we14").getValue(Song.class).getContent());
//
//                    list.add(s);
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
    }
}
