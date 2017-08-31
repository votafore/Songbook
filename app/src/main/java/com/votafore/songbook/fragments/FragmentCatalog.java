package com.votafore.songbook.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.h6ah4i.android.widget.advrecyclerview.decoration.SimpleListDividerDecorator;
import com.votafore.songbook.FireApp;
import com.votafore.songbook.R;
import com.votafore.songbook.model.Song;
import com.votafore.songbook.support.CatalogAdapter;

import java.util.ArrayList;
import java.util.List;


public class FragmentCatalog extends Fragment {

    private RecyclerView mRecyclerView;

    public FragmentCatalog(){}

    static public FragmentCatalog newInstance(){

        return new FragmentCatalog();


    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_list, container, false);

        mRecyclerView = v.findViewById(R.id.recycler_view);

        CatalogAdapter adapter = new CatalogAdapter(getActivity());

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setHasFixedSize(true);

        mRecyclerView.addItemDecoration(new SimpleListDividerDecorator(ContextCompat.getDrawable(getContext(), R.drawable.list_divider_h), true));

        connectToFirebase(adapter);

        return v;
    }

    private void connectToFirebase(final CatalogAdapter adapter){

        DatabaseReference root = FirebaseDatabase.getInstance().getReference();
        String NODE_SONGS  = "songs";
        final List<Song> mSongsToAdd = new ArrayList<>();

        final FireApp app = FireApp.getInstance();

        root.child(NODE_SONGS).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                Song song;

                try {
                    song = dataSnapshot.getValue(Song.class);
                    song.id = dataSnapshot.getKey();

                    if(!app.songIsLoaded(song))
                        mSongsToAdd.add(song);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

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
                adapter.setData(mSongsToAdd);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
