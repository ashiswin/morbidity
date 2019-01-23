package com.ashiswin.morbidity.components;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

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

    private AlarmManager am = null;
    private Timer countdownTimer = null;
    private Context context;

    private ArrayList<Updatable> subscribers = new ArrayList<>();

    private int sexIndex;
    private Date birthday;

    private CountdownComponent(Context context) {
        this.context = context;

        refreshPreferences();

        am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
    }

    public static CountdownComponent getInstance(Context context) {
        if(mInstance == null) {
            mInstance = new CountdownComponent(context);
        }

        return mInstance;
    }

    public TimerTask getCountdownTimerTask() {
        return new TimerTask() {
            @Override
            public void run() {
                if(birthday == null) return;

                CountdownData data = getTimeLeft();

                String timeLeft = data.days + "d  " + data.hours + "h  " + data.minutes + "m  " + data.seconds + "s";

                for(Updatable u : subscribers) {
                    u.update(timeLeft, data.percentage);
                }
            }
        };
    }

    public CountdownData getTimeLeft() {
        Calendar c = Calendar.getInstance();
        CountdownData data = new CountdownData();

        long difference = (birthday.getTime() - c.getTimeInMillis() + (Constants.LIFE_EXPECTANCY[sexIndex] * 31536000L * 1000L)) / 1000;
        data.percentage = 100 - (difference * 100 / (Constants.LIFE_EXPECTANCY[sexIndex] * 31536000L));

        data.days = difference / (24L * 60L * 60L);
        difference = difference % (24 * 60 * 60);

        data.hours = difference / (60 * 60);
        difference = difference % (60 * 60);

        data.minutes = difference / 60;
        difference = difference % 60;

        data.seconds = difference;

        return data;
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
            countdownTimer.scheduleAtFixedRate(getCountdownTimerTask(), 0, 1000);
        }
    }

    public void deregister(Updatable u) {
        subscribers.remove(u);

        if(subscribers.size() == 0) {
            countdownTimer.cancel();
            countdownTimer = null;
        }
    }

    public void schedule(Schedulable s) {
        PendingIntent pi = s.getAlarmIntent(this.context);
        am.cancel(pi);
        am.setInexactRepeating(AlarmManager.RTC, System.currentTimeMillis(), 60 * 1000, pi);
    }

    public void cancel(Schedulable s) {
        PendingIntent pi = s.getAlarmIntent(this.context);
        am.cancel(pi);
    }

    public interface Schedulable {
        PendingIntent getAlarmIntent(Context context);
    }

    public interface Updatable {
        void update(String timeLeft, long percentage);
    }

    public class CountdownData {
        public long percentage;
        public long days;
        public long hours;
        public long minutes;
        public long seconds;
    }
}
