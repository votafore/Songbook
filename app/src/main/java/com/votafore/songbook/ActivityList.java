package com.votafore.songbook;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.votafore.songbook.support.FragmentPage12;
import com.votafore.songbook.support.FragmentPage34;
import com.votafore.songbook.support.FragmentPage56;
import com.votafore.songbook.support.PagerAdapter;

public class ActivityList extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        final ViewPager pager = (ViewPager) findViewById(R.id.list_pager);

        pager.setAdapter(new PagerAdapter(getSupportFragmentManager()));
    }
}
