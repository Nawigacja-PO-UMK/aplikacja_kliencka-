package com.lokalizator;

import android.content.Context;
import android.location.Location;

import org.osmdroid.views.overlay.mylocation.IMyLocationConsumer;
import org.osmdroid.views.overlay.mylocation.IMyLocationProvider;

public class WifiMYLocationProvider implements IMyLocationProvider {


    WifiMYLocationProvider(Context kontekst)
    {

    }

    @Override
    public boolean startLocationProvider(IMyLocationConsumer myLocationConsumer) {
        return false;
    }

    @Override
    public void stopLocationProvider() {

    }

    @Override
    public Location getLastKnownLocation() {
        return null;
    }

    @Override
    public void destroy() {

    }
}
