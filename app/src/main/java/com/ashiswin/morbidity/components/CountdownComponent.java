package com.ashiswin.morbidity.components;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.ashiswin.morbidity.ui.HomeActivity;
import com.ashiswin.morbidity.utils.Constants;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * CountdownComponent class
 *
 * This class will be a singleton that handles the countdown. Views can register to be
 * updated by this countdown. They need to adhere to the Updateable interface to specify
 * their update logic.
 */
public class CountdownComponent {
    private static CountdownComponent mInstance = null;

    private Timer countdownTimer = null;
    private TimerTask countdownTimerTask = null;
    private Context context;

    private ArrayList<Updateable> subscribers = new ArrayList<>();

    private CountdownComponent(Context context) {
        this.context = context;

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        Date birthday = null;
        try {
            birthday = Constants.BIRTHDAY_FORMAT.parse(preferences.getString(Constants.PREF_BIRTHDAY, ""));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String sex = preferences.getString(Constants.PREF_SEX, "");
        final int sexIndex = (sex.equals("Male")) ? 0 : 1;
        final Date finalBirthday = birthday;

        countdownTimerTask = new TimerTask() {
            @Override
            public void run() {
                if(finalBirthday == null) return;

                Calendar c = Calendar.getInstance();
                long difference = (finalBirthday.getTime() - c.getTimeInMillis() + (Constants.LIFE_EXPECTANCY[sexIndex] * 31536000L * 1000L)) / 1000;
                long percentage = 100 - (difference * 100 / (Constants.LIFE_EXPECTANCY[sexIndex] * 31536000L));

                long days = difference / (24L * 60L * 60L);
                difference = difference % (24 * 60 * 60);

                long hours = difference / (60 * 60);
                difference = difference % (60 * 60);

                long minutes = difference / 60;
                difference = difference % 60;

                long seconds = difference;

                String timeLeft = days + "d  " + hours + "h  " + minutes + "m  " + seconds + "s";

                for(Updateable u : subscribers) {
                    u.update(timeLeft, percentage);
                }
            }
        };
    }

    public static CountdownComponent getInstance(Context context) {
        if(mInstance == null) {
            mInstance = new CountdownComponent(context);
        }

        return mInstance;
    }

    public void register(Updateable u) {
        subscribers.add(u);

        if(countdownTimer == null) {
            countdownTimer = new Timer();
            countdownTimer.scheduleAtFixedRate(countdownTimerTask, 0, 1000);

        }
    }

    public void deregister(Updateable u) {
        subscribers.remove(u);

        if(subscribers.size() == 0) {
            countdownTimer.cancel();
            countdownTimer = null;
        }
    }

    public interface Updateable {
        void update(String timeLeft, long percentage);
    }
}
