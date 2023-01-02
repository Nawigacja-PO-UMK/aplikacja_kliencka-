package com.example.nawigacja_po_umk;

import android.content.Context;
import android.graphics.Color;
import android.widget.Toast;

import org.osmdroid.bonuspack.routing.OSRMRoadManager;
import org.osmdroid.bonuspack.routing.Road;
import org.osmdroid.bonuspack.routing.RoadManager;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.overlay.Polyline;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class Traking {

    OSRMRoadManager tracking;
    Context context;
    Stack<GeoPoint> location;
    Color color;
    public Traking(Context context)
    {
        this.context=context;
        tracking=new OSRMRoadManager(context,"test");
        tracking.setMean("routed-foot/route/v1/driving/");
        location=new Stack<GeoPoint>();
        color.blue(255);
        color.alpha(255);
        color.red(0);
        color.green(255);
    }


    public Polyline tracking(GeoPoint next)
    {
        if (location.size()>0)
        return tracking(location.peek(),next);
        return null;
    }

public Polyline tracking(GeoPoint begin, GeoPoint end)
 {
     ArrayList<GeoPoint> punkty=new ArrayList<GeoPoint>();
     punkty.add(begin);
     punkty.add(end);
     Road road =tracking.getRoad(punkty);
     ArrayList<GeoPoint> points=road.mRouteHigh;
     Polyline roadOverlay = RoadManager.buildRoadOverlay(road,0x800000FF,7.0f);
     String opis=road.getLengthDurationText(context,0);
     Toast.makeText(context,opis, Toast.LENGTH_SHORT).show();
     this.location.push(end);
     return roadOverlay;
 }


}
