package com.votafore.songbook.fragments;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.Slide;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.h6ah4i.android.widget.advrecyclerview.animator.GeneralItemAnimator;
import com.h6ah4i.android.widget.advrecyclerview.animator.RefactoredDefaultItemAnimator;
import com.h6ah4i.android.widget.advrecyclerview.decoration.SimpleListDividerDecorator;
import com.h6ah4i.android.widget.advrecyclerview.expandable.RecyclerViewExpandableItemManager;
import com.h6ah4i.android.widget.advrecyclerview.utils.WrapperAdapterUtils;
import com.votafore.songbook.FIreApp;
import com.votafore.songbook.R;
import com.votafore.songbook.support.ListItem;
import com.votafore.songbook.support.RecyclerAdapter;
import com.votafore.songbook.testrecview.ExpandableAdapter;


public class FragmentList extends Fragment implements RecyclerViewExpandableItemManager.OnGroupCollapseListener,
                                            RecyclerViewExpandableItemManager.OnGroupExpandListener{



    private static final String SAVED_STATE_EXPANDABLE_ITEM_MANAGER = "RecyclerViewExpandableItemManager";

    private RecyclerView                        mRecyclerView;
    private RecyclerView.LayoutManager          mLayoutManager;
    private RecyclerView.Adapter                mWrappedAdapter;
    private RecyclerViewExpandableItemManager   mRecyclerViewExpandableItemManager;

    public FragmentList() {
        // Required empty public constructor
    }

    public static FragmentList newInstance() {
        return new FragmentList();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v = inflater.inflate(R.layout.fragment_list, container, false);

        //        //noinspection ConstantConditions
        mRecyclerView = (RecyclerView) v.findViewById(R.id.recycler_view);

        RecyclerAdapter adapter = new RecyclerAdapter();
        adapter.updateCursor();

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        //mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(adapter);
        //mRecyclerView.setHasFixedSize(false);

        adapter.setItemClickListener(new RecyclerAdapter.onItemClickListener() {
            @Override
            public void onClick(ListItem item) {

                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.container_main, FragmentSong.getInstance(item.id))
                        .addToBackStack("STACK")
                        .commit();
            }

            @Override
            public boolean onLongClick(ListItem item) {
                return false;
            }
        });

//        mLayoutManager = new LinearLayoutManager(getActivity());
//
//        final Parcelable eimSavedState = (savedInstanceState != null) ? savedInstanceState.getParcelable(SAVED_STATE_EXPANDABLE_ITEM_MANAGER) : null;
//        mRecyclerViewExpandableItemManager = new RecyclerViewExpandableItemManager(eimSavedState);
//        mRecyclerViewExpandableItemManager.setOnGroupExpandListener(this);
//        mRecyclerViewExpandableItemManager.setOnGroupCollapseListener(this);
//
//        //adapter
//        final ExpandableAdapter myItemAdapter = new ExpandableAdapter(FIreApp.getInstance().getDataProvider());
//
//        myItemAdapter.setCustomListener(new ExpandableAdapter.OnItemClickListener() {
//            @Override
//            public void onClick(String innerID) {
//
//                getActivity().getSupportFragmentManager()
//                        .beginTransaction()
//                        .replace(R.id.container_main, FragmentSong.getInstance(innerID))
//                        .addToBackStack("STACK")
//                        .commit();
//            }
//        });
//
//        mWrappedAdapter = mRecyclerViewExpandableItemManager.createWrappedAdapter(myItemAdapter);       // wrap for expanding
//
//        final GeneralItemAnimator animator = new RefactoredDefaultItemAnimator();
//
//        // Change animations are enabled by default since support-v7-recyclerview v22.
//        // Need to disable them when using animation indicator.
//        animator.setSupportsChangeAnimations(false);
//
//        mRecyclerView.setLayoutManager(mLayoutManager);
//        mRecyclerView.setAdapter(mWrappedAdapter);  // requires *wrapped* adapter
//        mRecyclerView.setItemAnimator(animator);
//        mRecyclerView.setHasFixedSize(false);
//
////        // additional decorations
////        //noinspection StatementWithEmptyBody
////        if (supportsViewElevation()) {
////            // Lollipop or later has native drop shadow feature. ItemShadowDecorator is not required.
////        } else {
////            mRecyclerView.addItemDecoration(new ItemShadowDecorator((NinePatchDrawable) ContextCompat.getDrawable(this, R.drawable.material_shadow_z1)));
////        }
        mRecyclerView.addItemDecoration(new SimpleListDividerDecorator(ContextCompat.getDrawable(getContext(), R.drawable.list_divider_h), true));

//        mRecyclerViewExpandableItemManager.attachRecyclerView(mRecyclerView);

        return v;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {

        // save current state to support screen rotation, etc...
        if (mRecyclerViewExpandableItemManager != null) {
            outState.putParcelable(
                    SAVED_STATE_EXPANDABLE_ITEM_MANAGER,
                    mRecyclerViewExpandableItemManager.getSavedState());
        }

        super.onSaveInstanceState(outState);
    }

    @Override
    public void onDestroy() {

        if (mRecyclerViewExpandableItemManager != null) {
            mRecyclerViewExpandableItemManager.release();
            mRecyclerViewExpandableItemManager = null;
        }

        if (mRecyclerView != null) {
            mRecyclerView.setItemAnimator(null);
            mRecyclerView.setAdapter(null);
            mRecyclerView = null;
        }

        if (mWrappedAdapter != null) {
            WrapperAdapterUtils.releaseAll(mWrappedAdapter);
            mWrappedAdapter = null;
        }
        mLayoutManager = null;

        super.onDestroy();
    }

    @Override
    public void onGroupCollapse(int groupPosition, boolean fromUser, Object payload) {
    }

    @Override
    public void onGroupExpand(int groupPosition, boolean fromUser, Object payload) {
        if (fromUser) {
            adjustScrollPositionOnGroupExpanded(groupPosition);
        }
    }

    private void adjustScrollPositionOnGroupExpanded(int groupPosition) {
        int childItemHeight = getResources().getDimensionPixelSize(R.dimen.list_item_height);
        int topMargin = (int) (getResources().getDisplayMetrics().density * 16); // top-spacing: 16dp
        int bottomMargin = topMargin; // bottom-spacing: 16dp

        mRecyclerViewExpandableItemManager.scrollToGroup(groupPosition, childItemHeight, topMargin, bottomMargin);
    }

    private boolean supportsViewElevation() {
        return (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP);
    }

}
