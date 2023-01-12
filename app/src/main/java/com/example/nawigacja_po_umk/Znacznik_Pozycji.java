package com.example.nawigacja_po_umk;

import android.content.Context;
import android.graphics.drawable.Drawable;

import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

public class Znacznik_Pozycji extends Marker {
    MapView mapView;
    int level;
    public Znacznik_Pozycji(MapView mapView, Context context, int level, GeoPoint position) {
        super(mapView);
        GeoPoint punktstartowy;
        Drawable Icon = context.getResources().getDrawable(R.drawable.ic_launcher);
        punktstartowy=position;
        setPosition(punktstartowy);
        setAnchor(ANCHOR_CENTER,ANCHOR_BOTTOM);
        setDraggable(true);
        setIcon(Icon);
        this.mapView=mapView;
        updata_pozytion(level);
    }
    public void updata_pozytion(int level)
    {
        this.level=level;
        GeoPoint pozycja = getPosition();
        setTitle("X="+pozycja.getLatitude()+" Y="+pozycja.getLongitude()+" Z="+level);
    }

    public void przesunięcię_wskaznika( GeoPoint point)
    {
        setPosition(point);
        mapView.invalidate();
    }

}
