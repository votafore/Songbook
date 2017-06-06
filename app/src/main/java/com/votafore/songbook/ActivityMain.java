package com.votafore.songbook;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.votafore.songbook.customview.AnimatedExpandableListView;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;


public class ActivityMain extends AppCompatActivity {

    private AnimatedExpandableListView listView;
    private ExampleAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_main_activity_ex);


//        Button find = (Button) findViewById(R.id.find_songs);
//        Button view = (Button) findViewById(R.id.view_songs);
//        Button add  = (Button) findViewById(R.id.add_song);
//
//        find.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(ActivityMain.this, ActivityList.class);
//                startActivity(intent);
//            }
//        });
//
//        view.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(ActivityMain.this, ActivityChosen.class);
//                startActivity(intent);
//            }
//        });
//
//        add.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(ActivityMain.this, ActivityAdd.class);
//                startActivity(intent);
//            }
//        });


//        final ExpandableListView listView = (ExpandableListView) findViewById(R.id.ex_list);
//
//        ExAdapter adapter = new ExAdapter();
//
//        listView.setAdapter(adapter);

        List<GroupItem> items = new ArrayList<GroupItem>();

        // Populate our list with groups and it's children
        for(int i = 1; i < 5; i++) {
            GroupItem item = new GroupItem();

            item.title = "Group " + i;

            for(int j = 0; j < i; j++) {
                ChildItem child = new ChildItem();
                child.title = "Awesome item " + j;
                child.hint = "Too awesome";

                item.items.add(child);
            }

            items.add(item);
        }

        adapter = new ExampleAdapter(this);
        adapter.setData(items);

        listView = (AnimatedExpandableListView) findViewById(R.id.ex_list);
        listView.setAdapter(adapter);


        // In order to show animations, we need to use a custom click handler
        // for our ExpandableListView.
        listView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {

            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                // We call collapseGroupWithAnimation(int) and
                // expandGroupWithAnimation(int) to animate group
                // expansion/collapse.
                if (listView.isGroupExpanded(groupPosition)) {
                    listView.collapseGroupWithAnimation(groupPosition);
                } else {
                    listView.expandGroupWithAnimation(groupPosition);
                }
                return true;
            }

        });
    }





    public class ExAdapter extends BaseExpandableListAdapter{

        ArrayList<ArrayList<String>> mGroups;

        public ExAdapter(){

            mGroups = new ArrayList<>();

            ArrayList<String> group1 = new ArrayList<>();

            group1.add("song 1");
            group1.add("song 2");
            group1.add("song 3");
            group1.add("song 4");
            group1.add("song 5");
            group1.add("song 6");

            mGroups.add(group1);

            ArrayList<String> group2 = new ArrayList<>();

            group2.add("song 1");
            group2.add("song 2");
            group2.add("song 3");

            mGroups.add(group2);
        }

        @Override
        public int getGroupCount() {
            return mGroups.size();
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            return mGroups.get(groupPosition).size();
        }

        @Override
        public Object getGroup(int groupPosition) {
            return mGroups.get(groupPosition);
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            return mGroups.get(groupPosition).get(childPosition);
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

            View v;

            if(convertView == null){
                v = getLayoutInflater().inflate(R.layout.list_item, parent, false);
            }else{
                v = convertView;
            }

            TextView title = (TextView) v.findViewById(R.id.track_title);
            TextView desc = (TextView) v.findViewById(R.id.track_description);

            title.setText("TEST");
            desc.setText("some txt");

            return v;
        }

        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

            View v;

            if(convertView == null){
                v = getLayoutInflater().inflate(R.layout.list_item, parent, false);
            }else{
                v = convertView;
            }

            TextView title = (TextView) v.findViewById(R.id.track_title);
            TextView desc = (TextView) v.findViewById(R.id.track_description);

            title.setText(mGroups.get(groupPosition).get(childPosition));
            desc.setText("some song");

            return v;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }
    }




    /*********** NEW *************/


    private static class GroupItem {
        String title;
        List<ChildItem> items = new ArrayList<ChildItem>();
    }

    private static class ChildItem {
        String title;
        String hint;
    }

    private static class ChildHolder {
        TextView title;
        TextView hint;
    }

    private static class GroupHolder {
        TextView title;
    }

    /**
     * Adapter for our list of {@link GroupItem}s.
     */
    private class ExampleAdapter extends AnimatedExpandableListView.AnimatedExpandableListAdapter{
        private LayoutInflater inflater;

        private List<GroupItem> items;

        public ExampleAdapter(Context context) {
            inflater = LayoutInflater.from(context);
        }

        public void setData(List<GroupItem> items) {
            this.items = items;
        }

        @Override
        public ChildItem getChild(int groupPosition, int childPosition) {
            return items.get(groupPosition).items.get(childPosition);
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public View getRealChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
            ChildHolder holder;
            ChildItem item = getChild(groupPosition, childPosition);
            if (convertView == null) {
                holder = new ChildHolder();
                convertView = inflater.inflate(R.layout.test_list_item, parent, false);
                holder.title = (TextView) convertView.findViewById(R.id.test_title);
                holder.hint = (TextView) convertView.findViewById(R.id.test_descr);
                convertView.setTag(holder);
            } else {
                holder = (ChildHolder) convertView.getTag();
            }

            holder.title.setText(item.title);
            holder.hint.setText(item.hint);

            return convertView;
        }

        @Override
        public int getRealChildrenCount(int groupPosition) {
            return items.get(groupPosition).items.size();
        }

        @Override
        public GroupItem getGroup(int groupPosition) {
            return items.get(groupPosition);
        }

        @Override
        public int getGroupCount() {
            return items.size();
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
            GroupHolder holder;
            GroupItem item = getGroup(groupPosition);
            if (convertView == null) {
                holder = new GroupHolder();
                convertView = inflater.inflate(R.layout.test_list_item, parent, false);
                holder.title = (TextView) convertView.findViewById(R.id.test_title);
                convertView.setTag(holder);
            } else {
                holder = (GroupHolder) convertView.getTag();
            }

            holder.title.setText(item.title);

            return convertView;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

        @Override
        public boolean isChildSelectable(int arg0, int arg1) {
            return true;
        }

    }
}
