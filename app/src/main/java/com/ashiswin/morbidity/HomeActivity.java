package com.ashiswin.morbidity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.ashiswin.morbidity.utils.Constants;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;

public class HomeActivity extends AppCompatActivity {
    private static final String TAG = "HomeActivity";

    SharedPreferences preferences;

    Date birthday;
    String sex;
    String country;
    int sexIndex;

    TextView txtDays, txtHours, txtMinutes, txtSeconds;
    Calendar c;
    Timer timer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        getSupportActionBar().setElevation(0);

        preferences = PreferenceManager.getDefaultSharedPreferences(HomeActivity.this);

        try {
            birthday = Constants.BIRTHDAY_FORMAT.parse(preferences.getString(Constants.PREF_BIRTHDAY, ""));
            Log.d(TAG, birthday.getMonth() + "");
        } catch (ParseException e) {
            e.printStackTrace();
        }

        sex = preferences.getString(Constants.PREF_SEX, "");
        country = preferences.getString(Constants.PREF_COUNTRY, "");

        sexIndex = (sex.equals("male")) ? 0 : 1;

        txtDays = findViewById(R.id.txtDays);
        txtHours = findViewById(R.id.txtHours);
        txtMinutes = findViewById(R.id.txtMinutes);
        txtSeconds = findViewById(R.id.txtSeconds);

        timer = new Timer();
        /*timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                c = Calendar.getInstance();
                long difference = (birthday.getTime() - c.getTimeInMillis() + (Constants.LIFE_EXPECTANCY[sexIndex] * 31536000L * 1000L)) / 1000;

                final long days = difference / (24L * 60L * 60L);
                difference = difference % (24 * 60 * 60);

                final long hours = difference / (60 * 60);
                difference = difference % (60 * 60);

                final long minutes = difference / 60;
                difference = difference % 60;

                final long seconds = difference;

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        txtDays.setText(days + "");
                        txtHours.setText(hours + "");
                        txtMinutes.setText(minutes + "");
                        txtSeconds.setText(seconds + "");
                    }
                });
            }
        }, 0, 1000);*/
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

}
