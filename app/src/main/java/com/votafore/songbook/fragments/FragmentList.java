package com.votafore.songbook.fragments;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.Slide;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.h6ah4i.android.widget.advrecyclerview.animator.GeneralItemAnimator;
import com.h6ah4i.android.widget.advrecyclerview.animator.RefactoredDefaultItemAnimator;
import com.h6ah4i.android.widget.advrecyclerview.decoration.SimpleListDividerDecorator;
import com.h6ah4i.android.widget.advrecyclerview.expandable.RecyclerViewExpandableItemManager;
import com.h6ah4i.android.widget.advrecyclerview.utils.WrapperAdapterUtils;
import com.votafore.songbook.FireApp;
import com.votafore.songbook.R;
import com.votafore.songbook.model.Song;
import com.votafore.songbook.support.ListItem;
import com.votafore.songbook.support.RecyclerAdapter;


public class FragmentList extends Fragment {

    private RecyclerView                        mRecyclerView;

    public FragmentList() {
        // Required empty public constructor
    }

    public static FragmentList newInstance() {
        return new FragmentList();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_list, container, false);

        mRecyclerView = v.findViewById(R.id.recycler_view);

        RecyclerAdapter adapter = new RecyclerAdapter();
        adapter.updateCursor();

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setHasFixedSize(true);

        adapter.setItemClickListener(new RecyclerAdapter.onItemClickListener() {
            @Override
            public void onClick(Song song) {

                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.container_main, FragmentSong.getInstance(song.id))
                        .addToBackStack("STACK")
                        .commit();
            }

            @Override
            public boolean onLongClick(Song song) {
                return false;
            }
        });

        mRecyclerView.addItemDecoration(new SimpleListDividerDecorator(ContextCompat.getDrawable(getContext(), R.drawable.list_divider_h), true));

        return v;
    }

}
