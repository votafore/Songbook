package com.votafore.songbook;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.votafore.songbook.support.FragmentPage;
import com.votafore.songbook.support.PagerAdapter;

public class ActivityList extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        ViewPager pager = (ViewPager) findViewById(R.id.list_pager);

        PagerAdapter adapter = new PagerAdapter(getSupportFragmentManager());

        adapter.addPage(FragmentPage.getInstance("1 - 2"));
        adapter.addPage(FragmentPage.getInstance("3 - 4"));
        adapter.addPage(FragmentPage.getInstance("5 - 6"));

        pager.setAdapter(adapter);
    }
}