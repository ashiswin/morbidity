package com.ashiswin.morbidity.ui.widgets;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.ashiswin.morbidity.BootReceiver;
import com.ashiswin.morbidity.R;
import com.ashiswin.morbidity.Schedulable;
import com.ashiswin.morbidity.Scheduler;

import java.util.Calendar;

/**
 * Implementation of App Widget functionality.
 */
public class CountDownWidgetProvider extends AppWidgetProvider implements Schedulable {

    public static final String ACTION_UPDATE = "update please";

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

        String countDownDay = "20839";
        Calendar c = Calendar.getInstance();
        String countDownHour = Integer.toString(c.get(Calendar.HOUR_OF_DAY));
        String countDownMinute = Integer.toString(c.get(Calendar.MINUTE));
        updateAppWidgets(context, appWidgetManager, appWidgetIds,
                         countDownDay, countDownHour, countDownMinute);
    }

    @Override
    public void onEnabled(Context context) {
        // Tell scheduler to schedule widget for update
        BootReceiver.register(this);
        scheduleUpdates(context);
    }

    @Override
    public void onDisabled(Context context) {
        // Remove updating schedule from scheduler
        BootReceiver.unregister(this);
        Scheduler.clearUpdate(context, getAlarmIntent(context));
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (ACTION_UPDATE.equals(intent.getAction())) {
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            ComponentName thisComponentName = new ComponentName(context.getPackageName(),
                                                                getClass().getName());
            int[] appWidgetIds = appWidgetManager.getAppWidgetIds(thisComponentName);
            onUpdate(context, appWidgetManager, appWidgetIds);

        } else super.onReceive(context, intent);
    }

    public void scheduleUpdates(Context context) {
        long intervalMillis = 60 * 1000;
        Scheduler.scheduleUpdate(context, getAlarmIntent(context), intervalMillis);
    }

    private void updateAppWidgets(Context context, AppWidgetManager appWidgetManager,
                                  int[] appWidgetIds, String countDownDay,
                                  String countDownHour, String countDownMin) {

        // Construct the RemoteViews object and update them
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.count_down_widget);
        views.setTextViewText(R.id.count_down_day, countDownDay);
        views.setTextViewText(R.id.count_down_hour, countDownHour);
        views.setTextViewText(R.id.count_down_minute, countDownMin);

        // Instruct the widget manager to update widgets
        for (int appWidgetId: appWidgetIds) {
            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
    }

    private PendingIntent getAlarmIntent(Context context) {
        Intent intent = new Intent(context, CountDownWidgetProvider.class);
        intent.setAction(CountDownWidgetProvider.ACTION_UPDATE);
        PendingIntent pi = PendingIntent.getBroadcast(context, 0, intent, 0);
        return pi;
    }
}

