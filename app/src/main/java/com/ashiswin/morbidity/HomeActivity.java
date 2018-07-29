package com.ashiswin.morbidity;

import android.animation.ValueAnimator;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ashiswin.morbidity.utils.Constants;
import com.dinuscxj.progressbar.CircleProgressBar;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        getSupportActionBar().setElevation(0);
        centerTitle();
        getSupportActionBar().setTitle("TIMER");

        preferences = PreferenceManager.getDefaultSharedPreferences(HomeActivity.this);

        loadPreferences();

        txtTimeLeft = findViewById(R.id.txtTimeLeft);
        txtPercentage = findViewById(R.id.txtPercentage);
        lineProgress = findViewById(R.id.line_progress);

        lineProgress.setProgressFormatter(new MyProgressFormatter());

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

                final String timeLeft = days + "d  " + hours + "h  " + minutes + "m  " + seconds + "s";
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        txtTimeLeft.setText(timeLeft);
                        txtPercentage.setText(percentage + "%");
                        lineProgress.setProgress(Math.round(percentage));
                    }
                });
            }
        }, 0, 1000);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, "MorbidityChannel")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Morbidity")
                .setContentText("Sup")
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

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(0, mBuilder.build());
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (percentage != 0) {
            simulateProgress(Math.round(percentage));
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
            case R.id.bucket:
                //add the function to perform here
                return(true);
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

    private void simulateProgress(int progress) {
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
}

final class MyProgressFormatter implements CircleProgressBar.ProgressFormatter {
    private static final String DEFAULT_PATTERN = "%d%%";

    @Override
    public CharSequence format(int progress, int max) {
        return String.format(DEFAULT_PATTERN, (int) ((float) progress / (float) max * 100));
    }
}
