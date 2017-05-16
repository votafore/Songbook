package com.votafore.songbook.support;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.votafore.songbook.R;

import java.util.ArrayList;
import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    private List<ListItem> items;
    private OnClickListener mListener;

    private int mSelected = -1;

    public RecyclerAdapter() {

        items = new ArrayList<>();
    }

    public void addItem(ListItem item){
        items.add(item);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = View.inflate(parent.getContext(), R.layout.list_item, null);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        ListItem item = items.get(position);

        holder.itemView.setSelected(position == mSelected);

        holder.title.setText(item.title);
    }

    @Override
    public int getItemCount() {
        return items.size();
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

            mListener.onClick(items.get(getAdapterPosition()), getAdapterPosition());
        }
    }



    public void setListener(OnClickListener listener){
        mListener = listener;
    }

    public interface OnClickListener{
        void onClick(ListItem item, int position);
    }
}
