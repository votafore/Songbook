package com.votafore.songbook.support;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.votafore.songbook.R;
import com.votafore.songbook.model.Song;

import java.util.ArrayList;
import java.util.List;


public class CatalogAdapter extends RecyclerView.Adapter<CatalogAdapter.ViewHolder> {

    List<Song> mData;
    boolean[] mChoosen;

    Context mContext;

    public CatalogAdapter(Context context) {
        mData = new ArrayList<>();
        mContext = context;
    }

    public void setData(List<Song> list){

        mData = list;
        mChoosen = new boolean[mData.size()];

        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = View.inflate(parent.getContext(), R.layout.rec_test_list_item, null);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.title.setText(mData.get(position).title);
        setBackground(holder.container, mChoosen[position]);
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
            mChoosen[getAdapterPosition()] = ! mChoosen[getAdapterPosition()];
            setBackground(v, mChoosen[getAdapterPosition()]);
        }
    }

    private void setBackground(View v, boolean isChecked){

        if(isChecked){
            v.setBackgroundColor(Color.GREEN);
        }else{
            v.setBackground(ContextCompat.getDrawable(mContext, R.drawable.test));
        }
    }
}
