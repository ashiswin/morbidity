package com.ashiswin.morbidity;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by ashis on 8/19/2018.
 */

public class BucketListViewHolder extends RecyclerView.ViewHolder {

    public final TextView textView;
    public final ImageView handleView;
    public final CheckBox checked;

    public BucketListViewHolder(View itemView) {
        super(itemView);
        textView = itemView.findViewById(R.id.text);
        handleView = itemView.findViewById(R.id.handle);
        checked = itemView.findViewById(R.id.checked);
    }

}