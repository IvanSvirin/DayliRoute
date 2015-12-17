package com.example.ivansv.dayliroute;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class MainActivity extends AppCompatActivity {
    Button btnStart;
    Button btnStop;
    StartReceiver startReceiver = new StartReceiver();
    public static boolean isServiceRunning = false;
    public static int dayOfWeek;
    public static int hour;
    public static int minute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnStart = (Button)findViewById(R.id.btn_start);
        btnStop = (Button)findViewById(R.id.btn_stop);

        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GregorianCalendar newCal = new GregorianCalendar();
                dayOfWeek = newCal.get(Calendar.DAY_OF_WEEK);
                hour = 8;
                minute = 0;
                startReceiver.startTrackingAlarm(MainActivity.this, hour, minute);
            }
        });

        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopService(new Intent(MainActivity.this, LocationTrackingService.class));
            }
        });
    }
}
