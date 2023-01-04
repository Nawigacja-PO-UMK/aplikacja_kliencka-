package com.example.nawigacja_po_umk;

import android.content.Context;

import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.overlay.Marker;

public class nasłuchiwanie_znacznika_pozycji implements Marker.OnMarkerDragListener {
    private Context kontekst;
    private int level;

    nasłuchiwanie_znacznika_pozycji(Context kontekst,int level)
    {
        this.kontekst=kontekst;
        this.level=level;
    }
    @Override
    public void onMarkerDrag(Marker marker) {

    }

    @Override
    public void onMarkerDragEnd(Marker marker) {
        double X,Y;
        GeoPoint pozycja = marker.getPosition();
        X=pozycja.getLatitude();
        Y=pozycja.getLongitude();
        marker.setTitle("X="+X+" Y="+Y+" Z="+level);

    }

    @Override
    public void onMarkerDragStart(Marker marker) {

    }
}
