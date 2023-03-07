package com.lokalizator;

import android.content.Context;
import android.location.Location;

import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.IMyLocationConsumer;
import org.osmdroid.views.overlay.mylocation.IMyLocationProvider;

public class uniwersal_location implements IMyLocationProvider {

    private GpsMyLocationProvider GPS;
    private WifiMYLocationProvider wifi;
    boolean location_builging;

    public uniwersal_location(Context kontekst)
    {
        GPS= new GpsMyLocationProvider(kontekst);
        wifi = new WifiMYLocationProvider(kontekst);
        location_builging=false;
    }

    public boolean isLocation_builging() {
        return location_builging;
    }

    public void setLocation_builging(boolean location_builging) {
        this.location_builging = location_builging;
    }

    @Override
    public boolean startLocationProvider(IMyLocationConsumer myLocationConsumer) {

        if(location_builging)
           return wifi.startLocationProvider(myLocationConsumer);
        else
         return GPS.startLocationProvider(myLocationConsumer);
    }

    @Override
    public void stopLocationProvider() {

        if(location_builging)
            wifi.stopLocationProvider();
        else
            GPS.stopLocationProvider();
    }

    @Override
    public Location getLastKnownLocation() {
        if(location_builging)
            return wifi.getLastKnownLocation();
        else
            return    GPS.getLastKnownLocation();

    }
    @Override
    public void destroy() {
        if(location_builging)
            wifi.destroy();
        else
            GPS.destroy();

    }
}
