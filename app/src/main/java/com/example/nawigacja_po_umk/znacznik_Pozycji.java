package com.example.nawigacja_po_umk;

import android.content.Context;
import android.graphics.drawable.Drawable;

import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

public class znacznik_Pozycji extends Marker {
    MapView mapView;

    public znacznik_Pozycji(MapView mapView, Context kontekst,int level) {
        super(mapView);
        GeoPoint punktstartowy;
        Drawable Icon = kontekst.getResources().getDrawable(R.drawable.ic_launcher);
        punktstartowy=new GeoPoint(mapView.getMapCenter().getLatitude(),mapView.getMapCenter().getLongitude());
        setPosition(punktstartowy);
        setAnchor(ANCHOR_CENTER,ANCHOR_BOTTOM);
        setDraggable(true);
        setIcon(Icon);
        this.mapView=mapView;
        GeoPoint pozycja = getPosition();
        setTitle("X="+pozycja.getLatitude()+" Y="+pozycja.getLongitude()+" Z="+level);
    }

    public void przesunięcię_wskaznika( GeoPoint point)
    {
        setPosition(point);
        mapView.invalidate();
    }

}
