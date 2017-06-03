package com.votafore.songbook;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.votafore.songbook.support.PagerAdapter;

public class ActivityList extends AppCompatActivity {

    private ViewPager pager;

    PagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        pager = (ViewPager) findViewById(R.id.list_pager);

        adapter = new PagerAdapter(getSupportFragmentManager());

        pager.setAdapter(adapter);
    }
}
