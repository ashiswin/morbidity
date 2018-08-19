package com.ashiswin.morbidity;

import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

public class BucketListActivity extends AppCompatActivity implements OnStartDragListener {
    RecyclerView lstBucketList;
    ItemTouchHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bucket_list);
        getSupportActionBar().setElevation(0);
        centerTitle();
        getSupportActionBar().setTitle("Bucket List");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        lstBucketList = findViewById(R.id.lstBucketList);
        lstBucketList.setHasFixedSize(true);
        lstBucketList.setLayoutManager(new LinearLayoutManager(BucketListActivity.this));

        BucketListAdapter adapter = new BucketListAdapter(BucketListActivity.this);
        lstBucketList.setAdapter(adapter);

        ItemTouchHelper.Callback callback = new BucketListTouchHelperCallback(adapter);
        helper = new ItemTouchHelper(callback);
        helper.attachToRecyclerView(lstBucketList);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case android.R.id.home:
                finish();
        }

        return(super.onOptionsItemSelected(item));
    }

    private void centerTitle() {
        ArrayList<View> textViews = new ArrayList<>();

        getWindow().getDecorView().findViewsWithText(textViews, getTitle(), View.FIND_VIEWS_WITH_TEXT);

        if(textViews.size() > 0) {
            AppCompatTextView appCompatTextView = null;
            if(textViews.size() == 1) {
                appCompatTextView = (AppCompatTextView) textViews.get(0);
            } else {
                for(View v : textViews) {
                    if(v.getParent() instanceof Toolbar) {
                        appCompatTextView = (AppCompatTextView) v;
                        break;
                    }
                }
            }

            if(appCompatTextView != null) {
                ViewGroup.LayoutParams params = appCompatTextView.getLayoutParams();
                params.width = ViewGroup.LayoutParams.MATCH_PARENT;
                appCompatTextView.setLayoutParams(params);
                appCompatTextView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                appCompatTextView.setTypeface(Typeface.create("sans-serif-light", Typeface.NORMAL));
                appCompatTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
            }
        }
    }

    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
        helper.startDrag(viewHolder);
    }
}