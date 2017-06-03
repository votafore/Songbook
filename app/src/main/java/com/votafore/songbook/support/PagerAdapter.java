package com.votafore.songbook.support;

import android.database.Cursor;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

import com.votafore.songbook.FIreApp;
import com.votafore.songbook.database.Base;
import com.votafore.songbook.database.Fetcher;

import java.util.ArrayList;
import java.util.List;

/**
 * Class give possibilities to list groups of songs
 */

public class PagerAdapter extends FragmentPagerAdapter {

    private List<Fragment> pages;

    private Fetcher params;

    public PagerAdapter(FragmentManager fm) {
        super(fm);

        pages = new ArrayList<>();

        params = new Fetcher();
        params.tableName = Base.TABLE_GROUPS;

        Log.v("Group", "adapter created");

        updateAdapter();
    }

    public void updateAdapter(){

        pages.clear();

        Cursor data = FIreApp.getInstance().getData(params);

        if(!data.moveToFirst())
            return;

        do{
            FragmentPage page = FragmentPage.getInstance(
                    data.getString(data.getColumnIndex("title")),
                    data.getString(data.getColumnIndex("id"))
            );

            pages.add(page);

        }while (data.moveToNext());

        Log.v("Group", "adapter updated");

        notifyDataSetChanged();
    }

    @Override
    public Fragment getItem(int position) {
        return pages.get(position);
    }

    @Override
    public int getCount() {
        return pages.size();
    }
}
