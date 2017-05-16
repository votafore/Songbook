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

import com.votafore.songbook.App;
import com.votafore.songbook.R;

public class FragmentPage extends Fragment {

    public static FragmentPage getInstance(String title){

        FragmentPage page = new FragmentPage();

        Bundle args = new Bundle();
        args.putString("title", title);

        page.setArguments(args);

        return page;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View            v       = inflater.inflate(R.layout.fragment_page_list, null);
        TextView        title   = (TextView)       v.findViewById(R.id.page_title);
        RecyclerView    list    = (RecyclerView)   v.findViewById(R.id.page_list);

        Bundle          args    = getArguments();

        DefaultItemAnimator animator    = new DefaultItemAnimator();
        LinearLayoutManager manager     = new LinearLayoutManager(container.getContext());
        RecyclerAdapter     adapter     = new RecyclerAdapter();

        title.setText(args.getString("title",""));

        list.setItemAnimator(animator);
        list.setLayoutManager(manager);

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

        adapter.setListener(new RecyclerAdapter.OnClickListener() {
            @Override
            public void onClick(ListItem item, int position) {
                App.getInstance().addSong(item);
            }
        });

        return v;
    }
}
