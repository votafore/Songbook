package com.votafore.songbook.support;

import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.votafore.songbook.App;
import com.votafore.songbook.R;
import com.votafore.songbook.database.Fetcher;

import java.util.ArrayList;
import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    Cursor mData;
    Fetcher mParams;

    private int mSelected = -1;

    public RecyclerAdapter(Fetcher params) {

        mParams = params;
    }

    public void updateCursor(){
        mData = App.getInstance().getData(mParams);
        notifyDataSetChanged();
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

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public TextView title;

        public ViewHolder(View itemView) {
            super(itemView);

            title = (TextView) itemView.findViewById(R.id.track_title);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            mSelected = getAdapterPosition();
        }
    }
}
