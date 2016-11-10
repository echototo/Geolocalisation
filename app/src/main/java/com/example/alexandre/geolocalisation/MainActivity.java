package com.example.alexandre.geolocalisation;

import android.Manifest;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    TextView currentText_long,currentText_lat,addressText;
    Button changeLocation, getAddress, testProximity;
    LocationManager manager;
    MockLocationProvider mock;
    Geocoder geo;
    LocationListener lis;
    List<Address> list;
    Location currentLocation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION}, 2);
        }


        currentText_long = (TextView) findViewById(R.id.current_long);
        currentText_lat = (TextView) findViewById(R.id.current_lat);
        addressText = (TextView) findViewById(R.id.address);
        changeLocation = (Button) findViewById(R.id.button);
        getAddress = (Button) findViewById(R.id.button2);
        testProximity = (Button) findViewById(R.id.proximity);
        geo = new Geocoder(this, Locale.getDefault());
        manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        ArrayList<LocationProvider> providers = new ArrayList<LocationProvider>();
        List<String> names = manager.getProviders(true);

        for (String name : names) {
            currentLocation = manager.getLastKnownLocation(manager.getProvider(name).getName());
            if (currentLocation != null) {
                currentText_long.setText("Long : " + currentLocation.getLongitude() + "");
                currentText_lat.setText("Lat : " + currentLocation.getLatitude() + "");
                System.out.println(currentLocation.getLatitude() + "");
                break;
        }
        }

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
                Toast.makeText(MainActivity.this, "Location changed", Toast.LENGTH_SHORT).show();
                currentLocation = location;
                currentText_long.setText("Long : " + location.getLongitude() + "");
                currentText_lat.setText("Lat : " + location.getLatitude() + "");
            }
        };

        mock = new MockLocationProvider(LocationManager.NETWORK_PROVIDER, this);

        manager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 1, lis);

        Intent intent = new Intent(this, AlertReceiver.class);
        PendingIntent pending = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        manager.addProximityAlert(20.01, 19.99, 150, -1, pending);


        changeLocation.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mock.pushLocation(12.34, 23.45);
            }
        });

        getAddress.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (currentLocation != null) {
                    try {
                        list = geo.getFromLocation(
                                currentLocation.getLatitude(),
                                currentLocation.getLongitude(), 1);
                        addressText.setText(
                                "Pays:  " + list.get(0).getCountryName() + "\n" +
                                        "Ville: " + list.get(0).getLocality() + "\n" +
                                        "Rue:   " + list.get(0).getAddressLine(0));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    System.out.println("pas encore");
                }
            }
        });

        testProximity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mock.pushLocation(20, 20);

        }
        });

    }
}