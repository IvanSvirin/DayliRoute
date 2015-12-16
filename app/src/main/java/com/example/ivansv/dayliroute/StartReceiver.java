package com.example.ivansv.dayliroute;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

/**
 * Created by ivansv on 13.12.2015.
 */
public class StartReceiver extends BroadcastReceiver {
    private AlarmManager alarmStartMgr;
    private PendingIntent alarmStartIntent;

    @Override
    public void onReceive(Context context, Intent intent) {
        GregorianCalendar newCal = new GregorianCalendar();
        int day = newCal.get(Calendar.DAY_OF_WEEK);

        if (MainActivity.serviceRunningFlag) {
            context.stopService(new Intent(context, LocationTrackingService.class));
            MainActivity.hour = 8;
            startTrackingAlarm(context, MainActivity.hour, MainActivity.minute);
            MainActivity.serviceRunningFlag = false;
        } else {
            if (day != 1 && day != 7) {
                context.startService(new Intent(context, LocationTrackingService.class));
                MainActivity.hour = 17;
                startTrackingAlarm(context, MainActivity.hour, MainActivity.minute);
                MainActivity.serviceRunningFlag = true;
            }
        }

    }

    void startTrackingAlarm(Context context, int hour, int minute) {
        if (alarmStartMgr != null) {
            alarmStartMgr.cancel(alarmStartIntent);
        }
        alarmStartMgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent startIntent = new Intent(context, StartReceiver.class);
        alarmStartIntent = PendingIntent.getBroadcast(context, 0, startIntent, 0);

        Calendar startCalendar = Calendar.getInstance();
        startCalendar.setTimeZone(TimeZone.getDefault());
        startCalendar.setTimeInMillis(System.currentTimeMillis());
        startCalendar.set(Calendar.HOUR_OF_DAY, hour);
        startCalendar.set(Calendar.MINUTE, minute);
        alarmStartMgr.set(AlarmManager.RTC, startCalendar.getTimeInMillis(), alarmStartIntent);
    }

}
