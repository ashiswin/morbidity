package com.ashiswin.morbidity;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.ashiswin.morbidity.utils.Constants;

import java.text.ParseException;
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

    TextView txtDays, txtHours, txtMinutes, txtSeconds;
    Calendar c;
    Timer timer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        preferences = PreferenceManager.getDefaultSharedPreferences(HomeActivity.this);

        try {
            birthday = Constants.BIRTHDAY_FORMAT.parse(preferences.getString(Constants.PREF_BIRTHDAY, ""));
            Log.d(TAG, birthday.getMonth() + "");
        } catch (ParseException e) {
            e.printStackTrace();
        }

        sex = preferences.getString(Constants.PREF_SEX, "");
        country = preferences.getString(Constants.PREF_COUNTRY, "");

        txtDays = findViewById(R.id.txtDays);
        txtHours = findViewById(R.id.txtHours);
        txtMinutes = findViewById(R.id.txtMinutes);
        txtSeconds = findViewById(R.id.txtSeconds);

        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                c = Calendar.getInstance();
                long difference = (birthday.getTime() - c.getTimeInMillis() + (80L * 31536000L * 1000L)) / 1000;

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
        }, 0, 1000);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        timer.cancel();
    }
}
