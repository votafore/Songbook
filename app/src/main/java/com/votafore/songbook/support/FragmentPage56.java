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

public class FragmentPage56 extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_page_list, null);

        TextView title = (TextView) v.findViewById(R.id.page_title);
        title.setText("5 - 6");

        RecyclerView list = (RecyclerView) v.findViewById(R.id.page_list);

        list.setItemAnimator(new DefaultItemAnimator());
        list.setLayoutManager(new LinearLayoutManager(container.getContext()));

        RecyclerAdapter adapter = new RecyclerAdapter();

        adapter.addItem(new ListItem("track 41"));
        adapter.addItem(new ListItem("track 42"));
        adapter.addItem(new ListItem("track 43"));
        adapter.addItem(new ListItem("track 44"));
        adapter.addItem(new ListItem("track 45"));
        adapter.addItem(new ListItem("track 46"));
        adapter.addItem(new ListItem("track 47"));
        adapter.addItem(new ListItem("track 48"));
        adapter.addItem(new ListItem("track 49"));
        adapter.addItem(new ListItem("track 50"));
        adapter.addItem(new ListItem("track 51"));
        adapter.addItem(new ListItem("track 52"));
        adapter.addItem(new ListItem("track 53"));

        list.setAdapter(adapter);

        return v;
    }
}
