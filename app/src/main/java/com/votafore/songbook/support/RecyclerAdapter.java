package com.votafore.songbook.support;

import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.votafore.songbook.FireApp;
import com.votafore.songbook.R;
import com.votafore.songbook.database.Base;
import com.votafore.songbook.database.Fetcher;
import com.votafore.songbook.model.Song;


public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    Cursor mData;

    private onItemClickListener mListener;

    public RecyclerAdapter() {

    }

    public void updateCursor(){

        Fetcher params = new Fetcher();
        params.tableName = Base.TABLE_SONGS;
        params.orderBy   = "title";

        mData = FireApp.getInstance().getData(params);

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

            title = itemView.findViewById(R.id.test_title);

            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {

            mData.moveToPosition(getAdapterPosition());

            Song song = new Song(
                    mData.getString(mData.getColumnIndex("id")),
                    mData.getString(mData.getColumnIndex("title")),
                    mData.getString(mData.getColumnIndex("content"))
            );

            mListener.onClick(song);
        }

        @Override
        public boolean onLongClick(View v) {

            mData.moveToPosition(getAdapterPosition());

            Song song = new Song(
                    mData.getString(mData.getColumnIndex("id")),
                    mData.getString(mData.getColumnIndex("title")),
                    mData.getString(mData.getColumnIndex("content"))
            );

            return mListener.onLongClick(song);
        }
    }

    public interface onItemClickListener{
        void onClick(Song song);
        boolean onLongClick(Song song);
    }
}
