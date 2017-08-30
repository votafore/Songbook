package com.votafore.songbook.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.h6ah4i.android.widget.advrecyclerview.animator.RefactoredDefaultItemAnimator;
import com.h6ah4i.android.widget.advrecyclerview.decoration.SimpleListDividerDecorator;
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
        mRecyclerView.setItemAnimator(new RefactoredDefaultItemAnimator());
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setHasFixedSize(true);

        mRecyclerView.addItemDecoration(new SimpleListDividerDecorator(ContextCompat.getDrawable(getContext(), R.drawable.list_divider_h), true));

        List<Song> songs = new ArrayList<>();
        songs.add(new Song("1", "title 1", "content 1"));
        songs.add(new Song("2", "title 2", "content 2"));

        adapter.setData(songs);

        return v;
    }
}
