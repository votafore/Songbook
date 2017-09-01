package com.votafore.songbook.test;

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

    DataSource<Cursor> mDataSource;

    private onItemClickListener mListener;

    public RecyclerAdapter() {

    }

    public void updateCursor(){

        Fetcher params = new Fetcher();
        params.tableName = Base.TABLE_SONGS;
        params.orderBy   = "title";

        mDataSource.setSource(FireApp.getInstance().getData(params));

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

        Cursor с = mDataSource.getElement(position);
        holder.title.setText(с.getString(с.getColumnIndex("title")));
    }

    @Override
    public int getItemCount() {

        return mDataSource.getCount();
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

            Cursor c = mDataSource.getElement(getAdapterPosition());

            Song song = new Song(
                    c.getString(c.getColumnIndex("id")),
                    c.getString(c.getColumnIndex("title")),
                    c.getString(c.getColumnIndex("content"))
            );

            mListener.onClick(song);
        }

        @Override
        public boolean onLongClick(View v) {

            Cursor c = mDataSource.getElement(getAdapterPosition());

            Song song = new Song(
                    c.getString(c.getColumnIndex("id")),
                    c.getString(c.getColumnIndex("title")),
                    c.getString(c.getColumnIndex("content"))
            );

            return mListener.onLongClick(song);
        }
    }

    public interface onItemClickListener{
        void onClick(Song song);
        boolean onLongClick(Song song);
    }


    /**
     * класс описывает интерфейс для работы с источником данных списка
     * т.к. это может быть как список (массив) так и курсор базы данных
     * то этот клас будет "адаптером"
     */
    public static abstract class DataSource<T>{

        protected T mDataSource;

        /**
         * @return возвращает количество элементов для списка
         */
        public abstract int getCount();

        /**
         * устанавливает источник данных списка
         */
        public abstract void setSource(T data);

        /**
         * вызывает обновление источника данных списка
         */
        public abstract void updateSource();

        /**
         * @param position позиция элемента
         * @return элемент/запись в указанной позиции
         */
        public abstract T getElement(int position);

    }
}
