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

import java.util.ArrayList;
import java.util.List;


public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    List<Song> mSongs;

    Cursor mData;

    private onItemClickListener mListener;

    //private int mSelected = -1;

    public RecyclerAdapter() {

       // mParams = params;

        //mSongs = new ArrayList<>();
    }

    public void updateCursor(){
        //mData = FIreApp.getInstance().getSongsByGroup(groupID);

        Fetcher params = new Fetcher();
        params.tableName = Base.TABLE_SONGS;
        params.orderBy   = "title";

        mData = FireApp.getInstance().getData(params);

//        if(!data.moveToFirst())
//            return;
//
//        do{
//
//            mSongs.add(new Song(
//                    data.getString(data.getColumnIndex("id")),
//                    data.getString(data.getColumnIndex("title")),
//                    data.getString(data.getColumnIndex("content"))));
//
//        }while (data.moveToNext());

        notifyDataSetChanged();
    }

    public void setData(){

    }

    public void setSpecCursor(){
//        mData = FireApp.getInstance().getChosenSong();
//        notifyDataSetChanged();
    }

    public void setItemClickListener(onItemClickListener listener){
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = View.inflate(parent.getContext(), R.layout.rec_test_list_item, null);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        mData.moveToPosition(position);

        //holder.itemView.setSelected(position == mSelected);

        holder.title.setText(mData.getString(mData.getColumnIndex("title")));
        //holder.title.setText(mSongs.get(position).title);
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
