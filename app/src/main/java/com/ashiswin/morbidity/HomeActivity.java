package com.ashiswin.morbidity;

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

    SharedPreferences preferences;

    Date birthday;
    String sex;
    String country;
    int sexIndex;
    long percentage = 0;
    long days = 0;
    long hours = 0;
    long minutes = 0;
    long seconds = 0;
    long difference = 0;

    TextView txtTimeLeft, txtPercentage;
    CircleProgressBar lineProgress;

    Calendar c;
    Timer timer;
    String timeLeft = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        setToolbar();

        preferences = PreferenceManager.getDefaultSharedPreferences(HomeActivity.this);

        loadPreferences();

        txtTimeLeft = findViewById(R.id.txtTimeLeft);
        txtPercentage = findViewById(R.id.txtPercentage);
        lineProgress = findViewById(R.id.line_progress);

        lineProgress.setProgressFormatter(new MyProgressFormatter());

        BucketListDataSource ds = new BucketListDataSource(getBaseContext());
        List<String> items = ds.getBucketList();

        final NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(HomeActivity.this, "MorbidityChannel")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(timeLeft)
                .setOngoing(false)
                .setOnlyAlertOnce(true)
                .setContentText("Why don't you " + items.get(0) + " today?")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Morbidity Channel";
            String description = "Morbidity's Channel";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("MorbidityChannel", name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

        final NotificationManagerCompat notificationManager = NotificationManagerCompat.from(HomeActivity.this);
        notificationManager.notify(0, mBuilder.build());

        // Update countdown clock every second
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if(birthday == null) return;

                c = Calendar.getInstance();
                difference = (birthday.getTime() - c.getTimeInMillis() + (Constants.LIFE_EXPECTANCY[sexIndex] * 31536000L * 1000L)) / 1000;
                percentage = 100 - (difference * 100 / (Constants.LIFE_EXPECTANCY[sexIndex] * 31536000L));

                days = difference / (24L * 60L * 60L);
                difference = difference % (24 * 60 * 60);

                hours = difference / (60 * 60);
                difference = difference % (60 * 60);

                minutes = difference / 60;
                difference = difference % 60;

                seconds = difference;

                timeLeft = days + "d  " + hours + "h  " + minutes + "m  " + seconds + "s";

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        txtTimeLeft.setText(timeLeft);
                        txtPercentage.setText(percentage + "%");
                        lineProgress.setProgress(Math.round(percentage));

                        mBuilder.setContentTitle(timeLeft);
                        notificationManager.notify(0, mBuilder.build());
                    }
                });
            }
        }, 0, 1000);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (percentage != 0) {
            animateProgress(Math.round(percentage));
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        timer.cancel();
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
            loadPreferences();
        }
    }

    private void loadPreferences() {
        try {
            birthday = Constants.BIRTHDAY_FORMAT.parse(preferences.getString(Constants.PREF_BIRTHDAY, ""));
            Log.d(TAG, birthday.getMonth() + "");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        sex = preferences.getString(Constants.PREF_SEX, "");
        country = preferences.getString(Constants.PREF_COUNTRY, "");
        sexIndex = (sex.equals("Male")) ? 0 : 1;
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
}


final class MyProgressFormatter implements CircleProgressBar.ProgressFormatter {
    private static final String DEFAULT_PATTERN = "%d%%";

    @Override
    public CharSequence format(int progress, int max) {
        return String.format(DEFAULT_PATTERN, (int) ((float) progress / (float) max * 100));
    }
}
