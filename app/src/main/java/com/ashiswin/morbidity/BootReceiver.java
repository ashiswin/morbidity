package com.ashiswin.morbidity;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import java.util.ArrayList;
import java.util.List;

public class BootReceiver extends BroadcastReceiver {

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
    }
}
