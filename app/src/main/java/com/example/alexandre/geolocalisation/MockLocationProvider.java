package com.example.alexandre.geolocalisation;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;

/**
 * Created by Alexandre on 09/11/2016.
 */

public class MockLocationProvider {
    String providerName;
    Context ctx;

    public MockLocationProvider(String name, Context ctx) {
        this.providerName = name;
        this.ctx = ctx;

        LocationManager lm = (LocationManager) ctx.getSystemService(
                Context.LOCATION_SERVICE);
        System.out.println("pote");
        try {
            lm.addTestProvider(providerName, true, false, true, false, false,
                    true, true, 0, 5);
        }catch(Exception e){
            e.printStackTrace();
        }
        System.out.println("pa");
        lm.setTestProviderEnabled(providerName, true);
    }

    public void pushLocation(double lat, double lon) {
        LocationManager lm = (LocationManager) ctx.getSystemService(
                Context.LOCATION_SERVICE);

        Location mockLocation = new Location(providerName);
        mockLocation.setLatitude(lat);
        mockLocation.setLongitude(lon);
        mockLocation.setAltitude(0);
        mockLocation.setTime(System.currentTimeMillis());

        lm.setTestProviderLocation(providerName, mockLocation);

    }

    public void shutdown() {
        LocationManager lm = (LocationManager) ctx.getSystemService(
                Context.LOCATION_SERVICE);
        lm.removeTestProvider(providerName);
    }
}