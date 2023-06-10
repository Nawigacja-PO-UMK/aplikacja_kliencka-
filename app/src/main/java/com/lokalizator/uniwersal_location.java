package com.lokalizator;

import android.content.Context;
import android.location.Location;
import android.widget.Toast;

import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.IMyLocationConsumer;
import org.osmdroid.views.overlay.mylocation.IMyLocationProvider;

public class uniwersal_location implements IMyLocationProvider {

    private GpsMyLocationProvider GPS;
    private WifiMYLocationProvider wifi;
    IMyLocationConsumer myLocationConsumer;
    private boolean location_builging;

    public uniwersal_location(Context kontekst)
    {
        GPS= new GpsMyLocationProvider(kontekst);
        wifi = new WifiMYLocationProvider(kontekst,this);
        location_builging=false;
    }

    public boolean isLocation_builging() {
        return location_builging;
    }

    public void setLocation_builging(boolean location_builging) {

        if(location_builging!=this.location_builging && myLocationConsumer!=null) {
            this.location_builging = location_builging;
            startLocationProvider(myLocationConsumer);
            if (location_builging) {
                Location location = GPS.getLastKnownLocation();
                location.set(wifi.location);
                GPS.stopLocationProvider();
            }
            else {
                Location location = wifi.getLastKnownLocation();
                location.set(GPS.getLastKnownLocation());
                wifi.stopLocationProvider();
            }
            startLocationProvider(myLocationConsumer);
        }
    }

    @Override
    public boolean startLocationProvider(IMyLocationConsumer myLocationConsumer) {

        if(myLocationConsumer!=null) {
            this.myLocationConsumer = myLocationConsumer;
            if (location_builging)
                return wifi.startLocationProvider(myLocationConsumer);

            else
                return GPS.startLocationProvider(myLocationConsumer);
        }
        return true;
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
            return GPS.getLastKnownLocation();

    }
    @Override
    public void destroy() {
        if(location_builging)
            wifi.destroy();
        else
            GPS.destroy();

    }
}
