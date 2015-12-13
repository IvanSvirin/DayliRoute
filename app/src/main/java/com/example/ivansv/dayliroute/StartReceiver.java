package com.example.ivansv.dayliroute;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by ivansv on 13.12.2015.
 */
public class StartReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        int key = intent.getIntExtra(TimeManagementService.START_SERVICE, 0);
        GregorianCalendar newCal = new GregorianCalendar();
        int day = newCal.get(Calendar.DAY_OF_WEEK);
        Toast.makeText(context, String.valueOf(day), Toast.LENGTH_SHORT).show();
        switch (key) {
            case TimeManagementService.START_KEY:
                if (day != 1 && day != 7) {
                    context.startService(new Intent(context, LocationTrackingService.class));
                }
                break;
            case TimeManagementService.STOP_KEY:
                context.stopService(new Intent(context, LocationTrackingService.class));
                break;
        }
    }
}
