package com.example.ivansv.dayliroute;

import android.Manifest;
import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by ivansv on 12.12.2015.
 */
public class LocationTrackingServiceOld extends Service{

    LocationManager locationManager;
    String provider;
    MyLocationListener myLocationListener;
    Criteria criteria;
    ArrayList<Position> dayTrack;
    Activity activity;

    public LocationTrackingServiceOld() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        dayTrack = new ArrayList<>();
        Toast.makeText(this, "Служба создана", Toast.LENGTH_SHORT).show();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        startTracking();
        Toast.makeText(this, "Служба запущена", Toast.LENGTH_SHORT).show();
        return Service.START_STICKY;
    }

    private void startTracking() {
        // Get the location manager
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        // Define the criteria how to select the location provider
        criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_COARSE);
        criteria.setCostAllowed(false);
        // get the best provider depending on the criteria
        provider = locationManager.getBestProvider(criteria, false);
        // the last known location of this provider
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            activity.requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION}, 0);
        }
        Location location = locationManager.getLastKnownLocation(provider);
        myLocationListener = new MyLocationListener();
        if (location != null) {
            myLocationListener.onLocationChanged(location);
        } else {
            // leads to the settings because there is no last known location
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            getApplication().startActivity(intent);
        }
        // location updates: at least 1 meter and 200 ms change
        locationManager.requestLocationUpdates(provider, 200, 1, myLocationListener);

    }

    @Override
    public void onDestroy() {
        Toast.makeText(this, "Служба остановлена", Toast.LENGTH_SHORT).show();
    }

    private class MyLocationListener implements LocationListener {
        @Override
        public void onLocationChanged(Location location) {
            Toast.makeText(LocationTrackingServiceOld.this, "Latitude: " + String.valueOf(location.getLatitude()), Toast.LENGTH_SHORT).show();
            Toast.makeText(LocationTrackingServiceOld.this, "Longitude: " + String.valueOf(location.getLongitude()), Toast.LENGTH_SHORT).show();
            Toast.makeText(LocationTrackingServiceOld.this, provider + " provider has been selected.", Toast.LENGTH_SHORT).show();
            Toast.makeText(LocationTrackingServiceOld.this, "Location changed!", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            Toast.makeText(LocationTrackingServiceOld.this, provider + "'s status changed to " + status + "!", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onProviderEnabled(String provider) {
            Toast.makeText(LocationTrackingServiceOld.this, "Provider " + provider + " enabled!", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onProviderDisabled(String provider) {
            Toast.makeText(LocationTrackingServiceOld.this, "Provider " + provider + " disabled!", Toast.LENGTH_SHORT).show();
        }
    }

}
