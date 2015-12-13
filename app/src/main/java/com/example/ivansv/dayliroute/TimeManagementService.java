package com.example.ivansv.dayliroute;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

import java.util.Calendar;
import java.util.TimeZone;

/**
 * Created by ivansv on 13.12.2015.
 */
public class TimeManagementService extends Service {
    public static final String START_SERVICE = "START_SERVICE";
    public static final String STOP_SERVICE = "STOP_SERVICE";
    public static final int START_KEY = 1;
    public static final int STOP_KEY = 2;
    private AlarmManager alarmStartMgr;
    private AlarmManager alarmStopMgr;
    private PendingIntent alarmStartIntent;
    private PendingIntent alarmStopIntent;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        alarmStartMgr = (AlarmManager)getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        Intent startIntent = new Intent(getApplicationContext(), StartReceiver.class);
        startIntent.putExtra(START_SERVICE, START_KEY);
        alarmStartIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, startIntent, 0);

        alarmStopMgr = (AlarmManager)getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        Intent stopIntent = new Intent(getApplicationContext(), StartReceiver.class);
        stopIntent.putExtra(STOP_SERVICE, STOP_KEY);
        alarmStopIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, stopIntent, 0);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Calendar startCalendar = Calendar.getInstance();
        startCalendar.setTimeZone(TimeZone.getDefault());
        startCalendar.setTimeInMillis(System.currentTimeMillis());
        startCalendar.set(Calendar.HOUR_OF_DAY, 7);
        startCalendar.set(Calendar.MINUTE, 0);
        alarmStartMgr.setInexactRepeating(AlarmManager.RTC_WAKEUP, startCalendar.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY, alarmStartIntent);

        Calendar stopCalendar = Calendar.getInstance();
        startCalendar.setTimeZone(TimeZone.getDefault());
        stopCalendar.setTimeInMillis(System.currentTimeMillis());
        stopCalendar.set(Calendar.HOUR_OF_DAY, 17);
        startCalendar.set(Calendar.MINUTE, 0);
        alarmStopMgr.setInexactRepeating(AlarmManager.RTC_WAKEUP, stopCalendar.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY, alarmStopIntent);

        return Service.START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
