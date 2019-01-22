package com.ashiswin.morbidity.components;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

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
 * updated by this countdown. They need to adhere to the Updatable interface to specify
 * their update logic.
 */

public class CountdownComponent {
    private static CountdownComponent mInstance = null;

    private Timer countdownTimer = null;
    private TimerTask countdownTimerTask = null;
    private Context context;

    private ArrayList<Updatable> subscribers = new ArrayList<>();

    private int sexIndex;
    private Date birthday;

    private CountdownComponent(Context context) {
        this.context = context;

        refreshPreferences();

        countdownTimerTask = new TimerTask() {
            @Override
            public void run() {
                if(birthday == null) return;

                Calendar c = Calendar.getInstance();
                long difference = (birthday.getTime() - c.getTimeInMillis() + (Constants.LIFE_EXPECTANCY[sexIndex] * 31536000L * 1000L)) / 1000;
                long percentage = 100 - (difference * 100 / (Constants.LIFE_EXPECTANCY[sexIndex] * 31536000L));

                long days = difference / (24L * 60L * 60L);
                difference = difference % (24 * 60 * 60);

                long hours = difference / (60 * 60);
                difference = difference % (60 * 60);

                long minutes = difference / 60;
                difference = difference % 60;

                long seconds = difference;

                String timeLeft = days + "d  " + hours + "h  " + minutes + "m  " + seconds + "s";

                for(Updatable u : subscribers) {
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

    public void refreshPreferences() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);

        birthday = null;
        try {
            birthday = Constants.BIRTHDAY_FORMAT.parse(preferences.getString(Constants.PREF_BIRTHDAY, ""));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String sex = preferences.getString(Constants.PREF_SEX, "");
        sexIndex = (sex.equals("Male")) ? 0 : 1;
    }
    public void register(Updatable u) {
        subscribers.add(u);

        if(countdownTimer == null) {
            countdownTimer = new Timer();
            countdownTimer.scheduleAtFixedRate(countdownTimerTask, 0, 1000);

        }
    }

    public void deregister(Updatable u) {
        subscribers.remove(u);

        if(subscribers.size() == 0) {
            countdownTimer.cancel();
            countdownTimer = null;
        }
    }

    public interface Updatable {
        void update(String timeLeft, long percentage);
    }
}
