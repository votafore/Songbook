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

public class FragmentPage12 extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_page_list, null);

        TextView title = (TextView) v.findViewById(R.id.page_title);
        title.setText("1 - 2");

        RecyclerView list = (RecyclerView) v.findViewById(R.id.page_list);

        list.setItemAnimator(new DefaultItemAnimator());
        list.setLayoutManager(new LinearLayoutManager(container.getContext()));

        RecyclerAdapter adapter = new RecyclerAdapter();

        adapter.addItem(new ListItem("track 1"));
        adapter.addItem(new ListItem("track 2"));
        adapter.addItem(new ListItem("track 3"));
        adapter.addItem(new ListItem("track 4"));
        adapter.addItem(new ListItem("track 5"));
        adapter.addItem(new ListItem("track 6"));
        adapter.addItem(new ListItem("track 7"));
        adapter.addItem(new ListItem("track 8"));
        adapter.addItem(new ListItem("track 9"));
        adapter.addItem(new ListItem("track 10"));
        adapter.addItem(new ListItem("track 11"));
        adapter.addItem(new ListItem("track 12"));
        adapter.addItem(new ListItem("track 13"));

        list.setAdapter(adapter);

        return v;
    }
}
