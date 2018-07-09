package com.ashiswin.morbidity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
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

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class HomeActivity extends AppCompatActivity {
    private static final String TAG = "HomeActivity";

    SharedPreferences preferences;

    Date birthday;
    String sex;
    String country;
    int sexIndex;

    TextView txtTimeLeft;

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

        try {
            birthday = Constants.BIRTHDAY_FORMAT.parse(preferences.getString(Constants.PREF_BIRTHDAY, ""));
            Log.d(TAG, birthday.getMonth() + "");
        } catch (ParseException e) {
            e.printStackTrace();
        }

        txtTimeLeft = findViewById(R.id.txtTimeLeft);

        sex = preferences.getString(Constants.PREF_SEX, "");
        country = preferences.getString(Constants.PREF_COUNTRY, "");

        sexIndex = (sex.equals("male")) ? 0 : 1;

        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if(birthday == null) return;

                c = Calendar.getInstance();
                long difference = (birthday.getTime() - c.getTimeInMillis() + (Constants.LIFE_EXPECTANCY[sexIndex] * 31536000L * 1000L)) / 1000;

                final long days = difference / (24L * 60L * 60L);
                difference = difference % (24 * 60 * 60);

                final long hours = difference / (60 * 60);
                difference = difference % (60 * 60);

                final long minutes = difference / 60;
                difference = difference % 60;

                final long seconds = difference;

                final String timeLeft = days + "d " + hours + "h " + minutes + "m " + seconds + "s";
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        txtTimeLeft.setText(timeLeft);
                    }
                });
            }
        }, 0, 1000);
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
                startActivity(settingsIntent);
            case R.id.bucket:
                //add the function to perform here
                return(true);
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
}
