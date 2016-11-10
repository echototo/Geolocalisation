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

public class MainActivity extends AppCompatActivity implements LocationListener {

    TextView currentText_long,currentText_lat,addressText;
    Button changeLocation, getAddress, testProximity;
    LocationManager manager;
    MockLocationProvider mock;
    Geocoder geo;
    List<Address> list;
    Location currentLocation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Verifier permission
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

        // Récupérer le service système
        manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        //Recherche de la derniere localisation dans tous les providers
        ArrayList<LocationProvider> providers = new ArrayList<LocationProvider>();
        List<String> names = manager.getProviders(true);

        /*A decommenter pour activer la partie derniere localisation
        for (String name : names) {
            currentLocation = manager.getLastKnownLocation(manager.getProvider(name).getName());
            if (currentLocation != null) {
                currentText_long.setText("Long : " + currentLocation.getLongitude() + "");
                currentText_lat.setText("Lat : " + currentLocation.getLatitude() + "");
                System.out.println(currentLocation.getLatitude() + "");
                break;
            }
        }
        */

        /*A decommenter pour activer la partie récupérer les informations
        manager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 1, this);
         */

        /**
         A decommenter pour activer la partie mock
         Pensez à regarder la classe MockLocationProvider
         mock = new MockLocationProvider(LocationManager.NETWORK_PROVIDER, this);
         changeLocation.setOnClickListener(new View.OnClickListener() {
         public void onClick(View v) {
         mock.pushLocation(12.34, 23.45);
         }
         });
         */

        /* A decommenter pour la partie Convertir les données
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
        */

        /*A decommenter pour la partie proximité
        Intent intent = new Intent(this, AlertReceiver.class);
        PendingIntent pending = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        manager.addProximityAlert(50.00, 50.00, 10000, -1, pending);

        testProximity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mock.pushLocation(49.99, 50);
            }
        });
        */
    }

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

    public void onPause() {
        super.onPause();
        try {
            manager.removeUpdates(this);
        } catch (SecurityException e) {

        }
    }

    public void onResume() {
        super.onResume();
        try {
            manager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 1, this);
        } catch (SecurityException e) {

        }
    }

    public void onDestroy() {
        try {
            manager.removeUpdates(this);
        } catch (SecurityException e) {

        }
        mock.shutdown();
    }
}