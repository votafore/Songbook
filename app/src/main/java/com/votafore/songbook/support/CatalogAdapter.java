package com.votafore.songbook.support;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.votafore.songbook.FireApp;
import com.votafore.songbook.R;
import com.votafore.songbook.model.Song;

import java.util.ArrayList;
import java.util.List;


public class CatalogAdapter extends RecyclerView.Adapter<CatalogAdapter.ViewHolder> {

    List<Song> mData;

    Context mContext;

    public CatalogAdapter(Context context) {
        mData = new ArrayList<>();
        mContext = context;
    }

    public void setData(List<Song> list){

        mData = list;

        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = View.inflate(parent.getContext(), R.layout.list_item, null);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.title.setText(mData.get(position).title);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public TextView title;
        public FrameLayout container;

        public ViewHolder(View itemView) {
            super(itemView);

            title     = itemView.findViewById(R.id.test_title);
            container = itemView.findViewById(R.id.container);

            container.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            FireApp app = FireApp.getInstance();

            Song current = mData.get(getAdapterPosition());

            if(app.songIsLoaded(current))
                return;

            app.addSong(current);

            mData.remove(getAdapterPosition());
            notifyItemRemoved(getAdapterPosition());
        }
    }
}
