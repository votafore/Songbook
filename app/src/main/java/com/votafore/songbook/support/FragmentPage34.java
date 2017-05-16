package com.votafore.songbook.support;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.votafore.songbook.R;

public class FragmentPage34 extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_page_list, null);

        TextView title = (TextView) v.findViewById(R.id.page_title);
        title.setText("3 - 4");

        RecyclerView list = (RecyclerView) v.findViewById(R.id.page_list);

        list.setItemAnimator(new DefaultItemAnimator());
        list.setLayoutManager(new LinearLayoutManager(container.getContext()));

        RecyclerAdapter adapter = new RecyclerAdapter();

        adapter.addItem(new ListItem("track 21"));
        adapter.addItem(new ListItem("track 22"));
        adapter.addItem(new ListItem("track 23"));
        adapter.addItem(new ListItem("track 24"));
        adapter.addItem(new ListItem("track 25"));
        adapter.addItem(new ListItem("track 26"));
        adapter.addItem(new ListItem("track 27"));
        adapter.addItem(new ListItem("track 28"));
        adapter.addItem(new ListItem("track 29"));
        adapter.addItem(new ListItem("track 30"));
        adapter.addItem(new ListItem("track 31"));
        adapter.addItem(new ListItem("track 32"));
        adapter.addItem(new ListItem("track 33"));

        list.setAdapter(adapter);

        return v;
    }
}