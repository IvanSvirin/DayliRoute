package com.example.ivansv.dayliroute;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

/**
 * Created by ivansv on 13.12.2015.
 */
public class StartReceiver extends BroadcastReceiver {
    public static final String MANAGE_SERVICE = "MANAGE_SERVICE";
    public static final int START_KEY = 1;
    public static final int STOP_KEY = 2;
    private AlarmManager alarmStartMgr;
    private AlarmManager alarmStopMgr;
    private PendingIntent alarmStartIntent;
    private PendingIntent alarmStopIntent;

    @Override
    public void onReceive(Context context, Intent intent) {
        int key = intent.getIntExtra(MANAGE_SERVICE, 0);
        Toast.makeText(context, String.valueOf("KEY" + key), Toast.LENGTH_SHORT).show();
        GregorianCalendar newCal = new GregorianCalendar();
        int day = newCal.get(Calendar.DAY_OF_WEEK);
        switch (key) {
            case START_KEY:
                if (day != 1 && day != 7) {
                    context.startService(new Intent(context, LocationTrackingService.class));
                    stopTrackingAlarm(context);
                }
                break;
            case STOP_KEY:
                context.stopService(new Intent(context, LocationTrackingService.class));
                startTrackingAlarm(context);
                break;
        }
    }

    void startTrackingAlarm(Context context) {
        alarmStartMgr = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent startIntent = new Intent(context, StartReceiver.class);
        startIntent.putExtra(MANAGE_SERVICE, START_KEY);
        alarmStartIntent = PendingIntent.getBroadcast(context, 0, startIntent, 0);

        Calendar startCalendar = Calendar.getInstance();
        startCalendar.setTimeZone(TimeZone.getDefault());
        startCalendar.setTimeInMillis(System.currentTimeMillis());
        startCalendar.set(Calendar.HOUR_OF_DAY, 8);
        startCalendar.set(Calendar.MINUTE, 0);
        alarmStartMgr.set(AlarmManager.RTC, startCalendar.getTimeInMillis(), alarmStartIntent);
    }

    void stopTrackingAlarm(Context context) {
        alarmStopMgr = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent stopIntent = new Intent(context, StartReceiver.class);
        stopIntent.putExtra(MANAGE_SERVICE, STOP_KEY);
        alarmStopIntent = PendingIntent.getBroadcast(context, 0, stopIntent, 0);

        Calendar stopCalendar = Calendar.getInstance();
        stopCalendar.setTimeZone(TimeZone.getDefault());
        stopCalendar.setTimeInMillis(System.currentTimeMillis());
        stopCalendar.set(Calendar.HOUR_OF_DAY, 17);
        stopCalendar.set(Calendar.MINUTE, 0);
        alarmStopMgr.set(AlarmManager.RTC, stopCalendar.getTimeInMillis(), alarmStopIntent);
    }
}
