package com.votafore.songbook;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.votafore.songbook.support.ListItem;
import com.votafore.songbook.support.RecyclerAdapter;

public class ActivityChosen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chosen);

        RecyclerView list = (RecyclerView) findViewById(R.id.chosen_list);

        final RecyclerAdapter     adapter  = new RecyclerAdapter();
        LinearLayoutManager manager  = new LinearLayoutManager(this);
        DefaultItemAnimator animator = new DefaultItemAnimator();

        adapter.setSpecCursor();
        adapter.setItemClickListener(new RecyclerAdapter.onItemClickListener() {
            @Override
            public void onClick(ListItem item) {

                Intent intent = new Intent(ActivityChosen.this, ActivitySong.class);

                intent.putExtra("ID", item.id);

                startActivity(intent);
            }

            @Override
            public boolean onLongClick(ListItem item) {

                FIreApp.getInstance().deleteChosen(item.id);
                adapter.setSpecCursor();

                return true;
            }
        });

        list.setAdapter(adapter);
        list.setLayoutManager(manager);
        list.setItemAnimator(animator);
    }
}
