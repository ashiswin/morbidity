package com.ashiswin.morbidity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;

public class Scheduler {

    public static void scheduleUpdate(Context context, PendingIntent pi, long intervalMillis) {
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        am.cancel(pi);
        am.setInexactRepeating(AlarmManager.RTC, System.currentTimeMillis(), intervalMillis, pi);
    }

    public static void clearUpdate(Context context, PendingIntent pi) {
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        am.cancel(pi);
    }
}
