package com.ashiswin.morbidity.components;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

public class NotificationComponent implements CountdownComponent.Schedulable {
    private static NotificationComponent mInstance;
    private Context context;

    private NotificationComponent(Context context) {
        this.context = context;
    }

    public static NotificationComponent getInstance(Context context) {
        if(mInstance == null) {
            mInstance = new NotificationComponent(context);
        }

        return mInstance;
    }

    public void startService() {
        Intent serviceIntent = new Intent(context, NotificationService.class);
        context.startService(serviceIntent);
    }
    @Override
    public PendingIntent getAlarmIntent(Context context) {
        Intent intent = new Intent(context, NotificationService.class);
        PendingIntent pi = PendingIntent.getService(context, 0, intent, 0);
        return pi;
    }
}
