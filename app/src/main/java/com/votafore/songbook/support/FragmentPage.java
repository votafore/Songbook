package com.votafore.songbook.support;

import android.content.Intent;
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

import com.votafore.songbook.ActivitySong;
import com.votafore.songbook.App;
import com.votafore.songbook.R;
import com.votafore.songbook.database.Base;
import com.votafore.songbook.database.Fetcher;

public class FragmentPage extends Fragment {

    public static FragmentPage getInstance(String title, int id){

        FragmentPage page = new FragmentPage();

        Bundle args = new Bundle();
        args.putString("title", title);
        args.putInt("id", id);

        page.setArguments(args);

        return page;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {

        View            v       = inflater.inflate(R.layout.fragment_page_list, null);
        TextView        title   = (TextView)       v.findViewById(R.id.page_title);
        RecyclerView    list    = (RecyclerView)   v.findViewById(R.id.page_list);

        Bundle          args    = getArguments();

        Fetcher             params      = new Fetcher();
        DefaultItemAnimator animator    = new DefaultItemAnimator();
        LinearLayoutManager manager     = new LinearLayoutManager(container.getContext());
        RecyclerAdapter     adapter     = new RecyclerAdapter(params);

        adapter.setItemClickListener(new RecyclerAdapter.onItemClickListener() {
            @Override
            public boolean onLongClick(ListItem item) {
                //App.getInstance().addSong(item);
                return true;
            }

            @Override
            public void onClick(ListItem item) {

                Intent intent = new Intent(container.getContext(), ActivitySong.class);
                intent.putExtra("ID", item.id);
                startActivity(intent);
            }
        });

        title.setText(args.getString("title",""));

        params.tableName    = "Songs";
        params.fields       = new String[]{"id", "title"};
        params.filter       = "group_id=?";
        params.filterArgs   = new String[]{String.valueOf(args.getInt("id", 0))};

        adapter.updateCursor();

        list.setItemAnimator(animator);
        list.setLayoutManager(manager);

        list.setAdapter(adapter);

        return v;
    }
}