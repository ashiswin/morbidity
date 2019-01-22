package com.ashiswin.morbidity.ui;

import android.content.DialogInterface;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.ashiswin.morbidity.ui.bucketlist.BucketListAdapter;
import com.ashiswin.morbidity.ui.bucketlist.BucketListTouchHelperCallback;
import com.ashiswin.morbidity.ui.bucketlist.OnStartDragListener;
import com.ashiswin.morbidity.R;

public class BucketListActivity extends AppCompatActivity implements OnStartDragListener {
    RecyclerView lstBucketList;
    FloatingActionButton btnAdd;

    ItemTouchHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bucket_list);
        setToolbar();

        lstBucketList = findViewById(R.id.lstBucketList);
        btnAdd = findViewById(R.id.btnAdd);

        lstBucketList.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(BucketListActivity.this);
        lstBucketList.setLayoutManager(layoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(lstBucketList.getContext(), layoutManager.getOrientation());
        lstBucketList.addItemDecoration(dividerItemDecoration);

        final BucketListAdapter adapter = new BucketListAdapter(BucketListActivity.this, BucketListActivity.this);
        lstBucketList.setAdapter(adapter);

        ItemTouchHelper.Callback callback = new BucketListTouchHelperCallback(adapter);
        helper = new ItemTouchHelper(callback);
        helper.attachToRecyclerView(lstBucketList);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // get prompts.xml view
                LayoutInflater li = LayoutInflater.from(BucketListActivity.this);
                View promptsView = li.inflate(R.layout.dialog_add_bucket_list, null);

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(BucketListActivity.this);
                alertDialogBuilder.setView(promptsView);

                final EditText userInput = promptsView.findViewById(R.id.edtItemName);

                // set dialog message
                alertDialogBuilder
                        .setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int id) {
                                        adapter.addItem(userInput.getText().toString());
                                    }
                                })
                        .setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int id) {
                                        dialog.cancel();
                                    }
                                });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case android.R.id.home:
                finish();
        }

        return(super.onOptionsItemSelected(item));
    }

    /**
     * Set toolbar parameters in this method
     */
    private void setToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        TextView title = findViewById(R.id.toolbar_title);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        title.setText(R.string.bucket_title);
    }

    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
        helper.startDrag(viewHolder);
    }
}
