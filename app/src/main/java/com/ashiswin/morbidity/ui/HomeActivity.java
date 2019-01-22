package com.ashiswin.morbidity.ui;

import android.animation.ValueAnimator;
import android.app.Dialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.ashiswin.morbidity.R;
import com.ashiswin.morbidity.components.CountdownComponent;
import com.ashiswin.morbidity.components.NotificationComponent;
import com.ashiswin.morbidity.datasources.BucketListDataSource;
import com.ashiswin.morbidity.utils.Constants;
import com.dinuscxj.progressbar.CircleProgressBar;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class HomeActivity extends AppCompatActivity {
    private static final String TAG = "HomeActivity";
    private static final int INTENT_SETTINGS = 0;

    TextView txtTimeLeft, txtPercentage;
    TextViewWrapper wrpTimeLeft, wrpPercentage;
    CircleProgressBar lineProgress;

    CountdownComponent countdown;
    NotificationComponent notification;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        setToolbar();

        txtTimeLeft = findViewById(R.id.txtTimeLeft);
        txtPercentage = findViewById(R.id.txtPercentage);
        lineProgress = findViewById(R.id.line_progress);

        lineProgress.setProgressFormatter(new MyProgressFormatter());

        wrpTimeLeft = new TextViewWrapper(txtTimeLeft, true);
        wrpPercentage = new TextViewWrapper(txtPercentage, false);

        countdown = CountdownComponent.getInstance(HomeActivity.this);
        notification = NotificationComponent.getInstance(HomeActivity.this);

        countdown.register(wrpTimeLeft);
        countdown.register(wrpPercentage);
        countdown.register(notification);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // Stop UI updates once activity is destroyed
        countdown.deregister(wrpTimeLeft);
        countdown.deregister(wrpPercentage);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.settings:
                Intent settingsIntent = new Intent(HomeActivity.this, SettingsActivity.class);
                startActivityForResult(settingsIntent, INTENT_SETTINGS);
                return true;
            case R.id.bucket:
                Intent bucketListIntent = new Intent(HomeActivity.this, BucketListActivity.class);
                startActivity(bucketListIntent);
                return true;
        }

        return(super.onOptionsItemSelected(item));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == INTENT_SETTINGS) {
            countdown.refreshPreferences();
        }
    }
    /**
     * Set toolbar parameters in this method
     */
    private void setToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        TextView title = findViewById(R.id.toolbar_title);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        title.setText(R.string.timer_title);
    }

    /**
     * This method animates the progress circle
     * @param progress Number between 0 to 100
     */
    private void animateProgress(int progress) {
        ValueAnimator animator = ValueAnimator.ofInt(0, progress);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int progress = (int) animation.getAnimatedValue();
                lineProgress.setProgress(progress);
            }
        });
        animator.setDuration(1000);
        animator.start();
    }

    private void showMyDialog(Context context) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_bucket_list);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(true);

        RecyclerView lstBucketList = dialog.findViewById(R.id.lstBucketList);
        FloatingActionButton btnClose = dialog.findViewById(R.id.btnAdd);

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        /**
         * if you want the dialog to be specific size, do the following
         * this will cover 85% of the screen (85% width and 85% height)
         */
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        int dialogWidth = (int)(displayMetrics.widthPixels * 0.85);
        int dialogHeight = (int)(displayMetrics.heightPixels * 0.85);
        dialog.getWindow().setLayout(dialogWidth, dialogHeight);

        dialog.show();
    }

    class TextViewWrapper implements CountdownComponent.Updatable {
        TextView v;
        boolean timeLeft;
        boolean firstAnimation = true;

        public TextViewWrapper(TextView v, boolean timeLeft) {
            this.v = v;
            this.timeLeft = timeLeft;
        }

        @Override
        public void update(final String timeLeft, final long percentage) {
            if(this.timeLeft) {
                v.post(new Runnable() {
                    @Override
                    public void run() {
                        v.setText(timeLeft);
                    }
                });
            }
            else {
                v.post(new Runnable() {
                    @Override
                    public void run() {
                        v.setText(percentage + "%");
                        if (percentage != 0 && firstAnimation) {
                            animateProgress(Math.round(percentage));
                            firstAnimation = false;
                        }
                    }
                });
            }
        }
    }
}


final class MyProgressFormatter implements CircleProgressBar.ProgressFormatter {
    private static final String DEFAULT_PATTERN = "%d%%";

    @Override
    public CharSequence format(int progress, int max) {
        return String.format(DEFAULT_PATTERN, (int) ((float) progress / (float) max * 100));
    }
}