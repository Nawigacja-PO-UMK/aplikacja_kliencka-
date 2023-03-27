package com.loader_Map_Building.Style;

import android.graphics.Canvas;
import android.graphics.drawable.Drawable;

import org.osmdroid.api.IGeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Overlay;

import java.util.ArrayList;

public class Etykiety extends Overlay {

    private final ArrayList<Overlay> markers;
    private final Drawable icon;

    public Etykiety(Drawable icon)
    {
        super();
        this.icon = icon;
        markers = new ArrayList<>();
    }

    public void addMarker(MapView mapView, IGeoPoint punkt)
    {
        ScaledMarker scaledMarker = new ScaledMarker(mapView,punkt, icon);
        markers.add(scaledMarker);
    }

    public void addMarker(IGeoPoint punkt, String name, int iconHeight)
    {
        MarkerText markerText = new MarkerText(name,punkt,iconHeight);
        markers.add(markerText);
    }

    @Override
    public void draw(Canvas canvas, MapView mapView, boolean shadow) {
        super.draw(canvas,mapView,shadow);

        for (Overlay marker : markers) {
            marker.draw(canvas, mapView, shadow);
        }
    }
}