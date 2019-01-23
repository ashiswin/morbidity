package com.ashiswin.morbidity.components;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import com.ashiswin.morbidity.R;
import com.ashiswin.morbidity.datasources.BucketListDataSource;
import com.ashiswin.morbidity.ui.HomeActivity;

import java.util.ArrayList;
import java.util.List;

public class NotificationComponent implements CountdownComponent.Schedulable {
    private static NotificationComponent mInstance;
    private Context context;


    private static final int LAUNCH_ACTIVITY_PENDING_INTENT = 0;

    private NotificationCompat.Builder mBuilder;
    private NotificationManagerCompat notificationManager;
    private List<String> incompleteBucketListItems;

    private NotificationComponent(Context context) {
        this.context = context;

        BucketListDataSource ds = new BucketListDataSource(context);
        incompleteBucketListItems = new ArrayList<>();

        for(int i = 0; i < ds.getBucketList().size(); i++) {
            if(!ds.getChecked().get(i)) {
                incompleteBucketListItems.add(ds.getBucketList().get(i));
            }
        }

        Intent launchIntent = new Intent(context, HomeActivity.class);
        PendingIntent launchPendingIntent = PendingIntent.getActivity(context, LAUNCH_ACTIVITY_PENDING_INTENT, launchIntent, PendingIntent.FLAG_CANCEL_CURRENT);

        // TODO: Prevent opening activity if activity already open

        mBuilder = new NotificationCompat.Builder(context, "MorbidityChannel")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setOngoing(false)
                .setOnlyAlertOnce(true)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(launchPendingIntent);

        // Android O specific notification channel stuff
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

        notificationManager = NotificationManagerCompat.from(context);
    }

    public static NotificationComponent getInstance(Context context) {
        if(mInstance == null) {
            mInstance = new NotificationComponent(context);
        }

        return mInstance;
    }

    public void showNotification() {
        CountdownComponent countdown = CountdownComponent.getInstance(context);
        CountdownComponent.CountdownData data = countdown.getTimeLeft();

        if (incompleteBucketListItems.size() > 0) {
            String timeLeft = data.days + "d  " + data.hours + "h  " + data.minutes + "m";
            mBuilder.setContentTitle(timeLeft + " remaining")
                    .setContentText("Why don't you " + incompleteBucketListItems.get(0) + " today?");
            notificationManager.notify(1, mBuilder.build());
        }
    }

    @Override
    public PendingIntent getAlarmIntent(Context context) {
        Intent intent = new Intent(context, NotificationReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(context, 0, intent, 0);
        return pi;
    }
}
