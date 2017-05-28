package com.votafore.songbook.support;

import android.database.Cursor;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.votafore.songbook.FIreApp;
import com.votafore.songbook.R;
import com.votafore.songbook.database.Base;
import com.votafore.songbook.database.Fetcher;
import com.votafore.songbook.firetestmodel.Group;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

/**
 * Created by User on 28.05.2017.
 *
 *
 */

public class DialogListAdapter extends BaseAdapter {

    private List<Group> groups;

    public DialogListAdapter(){

        groups = new ArrayList<>();

        Fetcher params = new Fetcher();
        params.tableName = Base.TABLE_GROUPS;

        Cursor cursor = FIreApp.getInstance().getData(params);

        if(cursor.getCount() < 0)
            return;

        cursor.moveToFirst();

        do{
            Group g = new Group(
                    cursor.getInt(cursor.getColumnIndex("id")),
                    cursor.getString(cursor.getColumnIndex("title")),
                    ""
            );

            groups.add(g);

        }while (cursor.moveToNext());
    }

    @Override
    public int getCount() {
        return groups.size();
    }

    @Override
    public Group getItem(int position) {
        return groups.get(position);
    }

    @Override
    public long getItemId(int position) {
        return groups.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if(v == null)
            v = View.inflate(parent.getContext(), R.layout.dialog_group_item, null);

        TextView title = (TextView) v.findViewById(R.id.dialog_group_title);

        title.setText(groups.get(position).title);

        return v;
    }
}
