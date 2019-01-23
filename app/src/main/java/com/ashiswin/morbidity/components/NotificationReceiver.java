package com.ashiswin.morbidity.components;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class NotificationReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationComponent notification = NotificationComponent.getInstance(context);
        notification.showNotification();
    }
}
