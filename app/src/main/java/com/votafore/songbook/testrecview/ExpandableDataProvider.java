package com.votafore.songbook.testrecview;



import android.database.Cursor;
import android.support.v4.util.Pair;

import com.h6ah4i.android.widget.advrecyclerview.expandable.RecyclerViewExpandableItemManager;
import com.votafore.songbook.FIreApp;
import com.votafore.songbook.database.Base;
import com.votafore.songbook.database.Fetcher;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ExpandableDataProvider extends AbstractExpandableDataProvider {
    private List<Pair<GroupData, List<ChildData>>> mData;

    // for undo group item
    private Pair<GroupData, List<ChildData>> mLastRemovedGroup;
    private int mLastRemovedGroupPosition = -1;

    // for undo child item
    private ChildData mLastRemovedChild;
    private long mLastRemovedChildParentGroupId = -1;
    private int mLastRemovedChildPosition = -1;

    public ExpandableDataProvider() {

        mData = new LinkedList<>();

        populateData();
    }

    public void populateData(){

        Fetcher ft_groups = new Fetcher();
        ft_groups.tableName = Base.TABLE_GROUPS;

        Fetcher ft_songs = new Fetcher();
        ft_songs.tableName = Base.TABLE_SONGS;

        FIreApp app = FIreApp.getInstance();

        Cursor crs_groups = app.getData(ft_groups);

        if(!crs_groups.moveToFirst())
            return;

        long gID = 0;

        do{

            String groupID = crs_groups.getString(crs_groups.getColumnIndex("id"));

            ConcreteGroupData group = new ConcreteGroupData(
                    gID,
                    crs_groups.getString(crs_groups.getColumnIndex("title")),
                    crs_groups.getString(crs_groups.getColumnIndex("id")));

            List<ChildData> children = new ArrayList<>();

            Cursor songs = app.getSongsByGroup(groupID);

            if(songs.moveToFirst()){

                do{
                    children.add(new ConcreteChildData(
                            group.generateNewChildId(),
                            songs.getString(songs.getColumnIndex("title")),
                            songs.getString(songs.getColumnIndex("id"))
                    ));

                }while(songs.moveToNext());

            }

            mData.add(new Pair<GroupData, List<ChildData>>(group, children));

            gID++;

        }while(crs_groups.moveToNext());
    }

    @Override
    public int getGroupCount() {
        return mData.size();
    }

    @Override
    public int getChildCount(int groupPosition) {
        return mData.get(groupPosition).second.size();
    }

    @Override
    public GroupData getGroupItem(int groupPosition) {
        if (groupPosition < 0 || groupPosition >= getGroupCount()) {
            throw new IndexOutOfBoundsException("groupPosition = " + groupPosition);
        }

        return mData.get(groupPosition).first;
    }

    @Override
    public ChildData getChildItem(int groupPosition, int childPosition) {
        if (groupPosition < 0 || groupPosition >= getGroupCount()) {
            throw new IndexOutOfBoundsException("groupPosition = " + groupPosition);
        }

        final List<ChildData> children = mData.get(groupPosition).second;

        if (childPosition < 0 || childPosition >= children.size()) {
            throw new IndexOutOfBoundsException("childPosition = " + childPosition);
        }

        return children.get(childPosition);
    }

    @Override
    public void moveGroupItem(int fromGroupPosition, int toGroupPosition) {
        if (fromGroupPosition == toGroupPosition) {
            return;
        }

        final Pair<GroupData, List<ChildData>> item = mData.remove(fromGroupPosition);
        mData.add(toGroupPosition, item);
    }

    @Override
    public void moveChildItem(int fromGroupPosition, int fromChildPosition, int toGroupPosition, int toChildPosition) {
        if ((fromGroupPosition == toGroupPosition) && (fromChildPosition == toChildPosition)) {
            return;
        }

        final Pair<GroupData, List<ChildData>> fromGroup = mData.get(fromGroupPosition);
        final Pair<GroupData, List<ChildData>> toGroup = mData.get(toGroupPosition);

        final ConcreteChildData item = (ConcreteChildData) fromGroup.second.remove(fromChildPosition);

        if (toGroupPosition != fromGroupPosition) {
            // assign a new ID
            final long newId = ((ConcreteGroupData) toGroup.first).generateNewChildId();
            //item.setChildId(newId);
        }

        toGroup.second.add(toChildPosition, item);
    }

    @Override
    public void removeGroupItem(int groupPosition) {
        mLastRemovedGroup = mData.remove(groupPosition);
        mLastRemovedGroupPosition = groupPosition;

        mLastRemovedChild = null;
        mLastRemovedChildParentGroupId = -1;
        mLastRemovedChildPosition = -1;
    }

    @Override
    public void removeChildItem(int groupPosition, int childPosition) {
        mLastRemovedChild = mData.get(groupPosition).second.remove(childPosition);
        //mLastRemovedChildParentGroupId = mData.get(groupPosition).first.getGroupId();
        mLastRemovedChildPosition = childPosition;

        mLastRemovedGroup = null;
        mLastRemovedGroupPosition = -1;
    }


    @Override
    public long undoLastRemoval() {
        if (mLastRemovedGroup != null) {
            return undoGroupRemoval();
//        } else if (mLastRemovedChild != null) {
//            return undoChildRemoval();
        } else {
            return RecyclerViewExpandableItemManager.NO_EXPANDABLE_POSITION;
        }
    }

    private long undoGroupRemoval() {
        int insertedPosition;
        if (mLastRemovedGroupPosition >= 0 && mLastRemovedGroupPosition < mData.size()) {
            insertedPosition = mLastRemovedGroupPosition;
        } else {
            insertedPosition = mData.size();
        }

        mData.add(insertedPosition, mLastRemovedGroup);

        mLastRemovedGroup = null;
        mLastRemovedGroupPosition = -1;

        return RecyclerViewExpandableItemManager.getPackedPositionForGroup(insertedPosition);
    }

//    private long undoChildRemoval() {
//        Pair<GroupData, List<ChildData>> group = null;
//        int groupPosition = -1;
//
//        // find the group
//        for (int i = 0; i < mData.size(); i++) {
//            if (mData.get(i).first.getGroupId() == mLastRemovedChildParentGroupId) {
//                group = mData.get(i);
//                groupPosition = i;
//                break;
//            }
//        }
//
//        if (group == null) {
//            return RecyclerViewExpandableItemManager.NO_EXPANDABLE_POSITION;
//        }
//
//        int insertedPosition;
//        if (mLastRemovedChildPosition >= 0 && mLastRemovedChildPosition < group.second.size()) {
//            insertedPosition = mLastRemovedChildPosition;
//        } else {
//            insertedPosition = group.second.size();
//        }
//
//        group.second.add(insertedPosition, mLastRemovedChild);
//
//        mLastRemovedChildParentGroupId = -1;
//        mLastRemovedChildPosition = -1;
//        mLastRemovedChild = null;
//
//        return RecyclerViewExpandableItemManager.getPackedPositionForChild(groupPosition, insertedPosition);
//    }

    public static final class ConcreteGroupData extends GroupData {

        private final long mId;
        private final String mText;
        private boolean mPinned;
        private long mNextChildId;
        private String innerID;

        ConcreteGroupData(long id, String text, String inID) {
            mId = id;
            mText = text;
            mNextChildId = 0;
            innerID = inID;
        }

        @Override
        public long getGroupId() {
            return mId;
        }

        @Override
        public boolean isSectionHeader() {
            return false;
        }

        @Override
        public String getText() {
            return mText;
        }

        @Override
        public void setPinned(boolean pinnedToSwipeLeft) {
            mPinned = pinnedToSwipeLeft;
        }

        @Override
        public boolean isPinned() {
            return mPinned;
        }

        public long generateNewChildId() {
            final long id = mNextChildId;
            mNextChildId += 1;
            return id;
        }
    }

    public static final class ConcreteChildData extends ChildData {

        private long mId;
        private final String mText;
        private boolean mPinned;
        private String innerID;

        ConcreteChildData(long id, String text, String inID) {
            mId = id;
            mText = text;
            innerID = inID;
        }

        @Override
        public long getChildId() {
            return mId;
        }

        @Override
        public String getText() {
            return mText;
        }

        @Override
        public void setPinned(boolean pinned) {
            mPinned = pinned;
        }

        @Override
        public boolean isPinned() {
            return mPinned;
        }

        public void setChildId(long id) {
            this.mId = id;
        }

        public String getInnerID(){
            return innerID;
        }
    }
}