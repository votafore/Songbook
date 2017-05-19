package com.votafore.songbook.support;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import com.votafore.songbook.ActivitySong;
import com.votafore.songbook.App;
import com.votafore.songbook.R;
import com.votafore.songbook.database.Fetcher;

import java.util.ArrayList;
import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    Cursor mData;
    Fetcher mParams;

    private onItemClickListener mListener;

    private int mSelected = -1;

    public RecyclerAdapter(Fetcher params) {

        mParams = params;
    }

    public void updateCursor(){
        mData = App.getInstance().getData(mParams);
        notifyDataSetChanged();
    }

    public void setSpecCursor(){
        mData = App.getInstance().getChosenSong();
        notifyDataSetChanged();
    }

    public void setItemClickListener(onItemClickListener listener){
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = View.inflate(parent.getContext(), R.layout.list_item, null);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        mData.moveToPosition(position);

        holder.itemView.setSelected(position == mSelected);

        holder.title.setText(mData.getString(mData.getColumnIndex("title")));
    }

    @Override
    public int getItemCount() {

        return mData.getCount();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener{

        public TextView title;

        public ViewHolder(View itemView) {
            super(itemView);

            title = (TextView) itemView.findViewById(R.id.track_title);

            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {

            mData.moveToPosition(getAdapterPosition());

            ListItem item = new ListItem();
            item.title  = mData.getString(mData.getColumnIndex("title"));
            item.id     = mData.getInt(mData.getColumnIndex("id"));

            mListener.onClick(item);
        }

        @Override
        public boolean onLongClick(View v) {

            mData.moveToPosition(getAdapterPosition());

            ListItem item = new ListItem();
            item.title  = mData.getString(mData.getColumnIndex("title"));
            item.id     = mData.getInt(mData.getColumnIndex("id"));

            return mListener.onLongClick(item);
        }
    }

    public interface onItemClickListener{
        void onClick(ListItem item);
        boolean onLongClick(ListItem item);
    }
}
