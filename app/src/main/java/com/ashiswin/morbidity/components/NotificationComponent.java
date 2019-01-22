package com.ashiswin.morbidity.components;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import com.ashiswin.morbidity.R;
import com.ashiswin.morbidity.datasources.BucketListDataSource;
import com.ashiswin.morbidity.ui.HomeActivity;

import java.util.List;

public class NotificationComponent implements CountdownComponent.Updatable {
    private static NotificationComponent mInstance = null;
    private Context context;

    private NotificationCompat.Builder mBuilder = null;
    private NotificationManagerCompat notificationManager = null;
    private List<String> bucketListItems = null;

    private NotificationComponent(Context context) {
        this.context = context;

        BucketListDataSource ds = new BucketListDataSource(context);
        bucketListItems = ds.getBucketList();

        mBuilder = new NotificationCompat.Builder(context, "MorbidityChannel")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setOngoing(false)
                .setOnlyAlertOnce(true)
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

        notificationManager = NotificationManagerCompat.from(context);
    }

    public static NotificationComponent getInstance(Context context) {
        if(mInstance == null) {
            mInstance = new NotificationComponent(context);
        }

        return mInstance;
    }

    @Override
    public void update(String timeLeft, long percentage) {
        if (bucketListItems.size() > 0) {
            mBuilder.setContentTitle(timeLeft)
                    .setContentText("Why don't you " + bucketListItems.get(0) + " today?");
            notificationManager.notify(1, mBuilder.build());
        }
    }
}
