package com.ashiswin.morbidity.ui.bucketlist;

import android.support.v7.widget.RecyclerView;

/**
 * Created by ashis on 8/19/2018.
 */

public interface OnStartDragListener {

    /**
     * Called when a view is requesting a start of a drag.
     *
     * @param viewHolder The holder of the view to drag.
     */
    void onStartDrag(RecyclerView.ViewHolder viewHolder);
}