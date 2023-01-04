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

import kotlin.random.Random;
import kotlin.random.URandomKt;

public class Traking {

    OSRMRoadManager tracking;
    Context context;
    Stack<GeoPoint> location;
    int level;
    int lost_level;
    public Traking(Context context,int level)
    {
        this.context=context;
        tracking=new OSRMRoadManager(context,"test");
        tracking.setMean("routed-foot/route/v1/driving/");
        location=new Stack<GeoPoint>();
        this.level=level;

    }

    public Road tracking(int level,GeoPoint next)
    {
        if (location.size()>0)
        return tracking(lost_level,location.peek(),next,level);
        return null;
    }

public Road tracking(int level_begin,GeoPoint begin, GeoPoint end,int level_end)
 {
    this.lost_level=level_end;
    this.location.push(end);
        if(level_begin==level_end)
            return leveltraking(begin,end);
        else
            return null;
 }

 private Road leveltraking(GeoPoint begin, GeoPoint end)
 {
     ArrayList<GeoPoint> punkty=new ArrayList<GeoPoint>();
     punkty.add(begin);
     punkty.add(end);
     Road road =tracking.getRoad(punkty);
     ArrayList<GeoPoint> points=road.mRouteHigh;
     return road;
 }


}
