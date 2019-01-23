package com.ashiswin.morbidity.components;

import android.app.IntentService;
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

import java.util.List;


/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions and extra parameters.
 */
public class NotificationService extends IntentService {

    private static final int LAUNCH_ACTIVITY_PENDING_INTENT = 0;

    private NotificationCompat.Builder mBuilder;
    private NotificationManagerCompat notificationManager;
    private List<String> bucketListItems;

    public NotificationService() {
        super("NotificationComponent");
    }

    @Override
    public void onCreate() {
        super.onCreate();

        BucketListDataSource ds = new BucketListDataSource(getBaseContext());
        bucketListItems = ds.getBucketList();

        Intent launchIntent = new Intent(getBaseContext(), HomeActivity.class);
        PendingIntent launchPendingIntent = PendingIntent.getActivity(getBaseContext(), LAUNCH_ACTIVITY_PENDING_INTENT, launchIntent, PendingIntent.FLAG_CANCEL_CURRENT);

        // TODO: Prevent opening activity if activity already open

        mBuilder = new NotificationCompat.Builder(getBaseContext(), "MorbidityChannel")
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
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

        notificationManager = NotificationManagerCompat.from(getBaseContext());
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        CountdownComponent countdown = CountdownComponent.getInstance(getBaseContext());
        CountdownComponent.CountdownData data = countdown.getTimeLeft();

        if (bucketListItems.size() > 0) {
            String timeLeft = data.days + "d  " + data.hours + "h  " + data.minutes + "m";
            mBuilder.setContentTitle(timeLeft)
                    .setContentText("Why don't you " + bucketListItems.get(0) + " today?");
            notificationManager.notify(1, mBuilder.build());
        }
    }

}
