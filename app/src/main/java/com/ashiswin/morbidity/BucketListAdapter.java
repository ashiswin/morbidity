package com.ashiswin.morbidity;

import android.content.Context;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by ashis on 8/19/2018.
 */

public class BucketListAdapter extends RecyclerView.Adapter<BucketListViewHolder> implements ItemTouchHelperAdapter {
    private final BucketListDataSource dataSource;
    private final OnStartDragListener mDragStartListener;

    private final List<String> mItems;

    public BucketListAdapter(OnStartDragListener dragStartListener, Context context) {
        mDragStartListener = dragStartListener;
        dataSource = new BucketListDataSource(context);
        mItems = dataSource.getBucketList();
    }

    @Override
    public BucketListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_bucket_list, parent, false);
        return new BucketListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final BucketListViewHolder holder, int position) {
        holder.textView.setText(mItems.get(position));

        holder.handleView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (MotionEventCompat.getActionMasked(event) ==
                        MotionEvent.ACTION_DOWN) {
                    mDragStartListener.onStartDrag(holder);
                }
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    @Override
    public void onItemDismiss(int position) {
        mItems.remove(position);
        notifyItemRemoved(position);
        dataSource.saveBucketList(mItems);
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(mItems, i, i + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(mItems, i, i - 1);
            }
        }
        notifyItemMoved(fromPosition, toPosition);
        dataSource.saveBucketList(mItems);
        return true;
    }

    public void addItem(String listItem) {
        mItems.add(listItem);
        notifyDataSetChanged();
        dataSource.saveBucketList(mItems);
    }
}
