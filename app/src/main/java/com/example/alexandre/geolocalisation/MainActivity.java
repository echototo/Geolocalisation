package com.example.alexandre.geolocalisation;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity{

    TextView currentText_long,currentText_lat,addressText;
    Button changeLocation,getAddress;
    LocationManager manager;
    MockLocationProvider mock;
    Geocoder geo;
    LocationListener lis;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        currentText_long = (TextView) findViewById(R.id.current_long);
        currentText_lat = (TextView) findViewById(R.id.current_lat);
        addressText = (TextView) findViewById(R.id.address);
        changeLocation = (Button) findViewById(R.id.button);
        getAddress = (Button) findViewById(R.id.button2);
        geo = new Geocoder(this, Locale.getDefault());
        manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION}, 2);
        }
        Location last = manager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        currentText_long.setText("Long : " + last.getLongitude() + "");
        currentText_lat.setText("Lat : " + last.getLatitude() + "");
        System.out.println("miel");
        lis = new LocationListener() {
            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            @Override
            public void onProviderEnabled(String provider) {
            }

            @Override
            public void onProviderDisabled(String provider) {
            }

            @Override
            public void onLocationChanged(Location location) {
                currentText_long.setText("Long : " + location.getLongitude() + "");
                currentText_lat.setText("Lat : " + location.getLatitude() + "");
            }
        };
       mock = new MockLocationProvider(LocationManager.NETWORK_PROVIDER, this);

        manager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 1, lis);
        System.out.println("peter");

        changeLocation.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mock.pushLocation(12.34, 23.45);
            }
        });

        getAddress.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Location loc = null;
                List<Address> list = null;
                try {
                    loc = manager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                    list = geo.getFromLocation(loc.getLatitude(), loc.getLongitude(), 1);
                } catch (SecurityException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                addressText.setText("Pays: " + list.get(0).getCountryName());
            }
        });

    }
    protected void onDestroy(){
        try {
            manager.removeUpdates(lis);
        }catch(SecurityException e){
            e.printStackTrace();
        }
        mock.shutdown();
    }
}