package com.ashiswin.morbidity;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import java.util.ArrayList;
import java.util.List;

public class BootReceiver extends BroadcastReceiver {

    private static ArrayList<Schedulable> registered = new ArrayList<>();

    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, "MorbidityChannel")
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
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(0, mBuilder.build());


        // Schedule widgets to update once we boot
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        for (Schedulable schedulable: registered) {
            int[] ids = appWidgetManager.getAppWidgetIds(new ComponentName(context,
                                                                           schedulable.getClass()));
            if (ids.length > 0) {
                schedulable.scheduleUpdates(context);
            }
        }
    }

    public static void register(Schedulable schedulable) {
        registered.add(schedulable);
    }

    public static void unregister(Schedulable schedulable) {
        registered.remove(schedulable);
    }
}
