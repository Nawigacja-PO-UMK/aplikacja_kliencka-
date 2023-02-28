package com.Tracking;

import android.content.Context;

import org.osmdroid.bonuspack.routing.GraphHopperRoadManager;
import org.osmdroid.bonuspack.routing.OSRMRoadManager;
import org.osmdroid.bonuspack.routing.Road;
import org.osmdroid.bonuspack.routing.RoadManager;
import org.osmdroid.util.GeoPoint;

import java.util.ArrayList;
import java.util.Stack;

public class Traking  {

    GraphHopperRoadManager tracking;
    Context context;
    Stack<GeoPoint> location;
    boolean building;
    int level;
    int lost_level;
    Tracking_buliding tracking_buliding;


    public Traking(Context context, int level, boolean building)
    {
        this.building=building;
        this.context=context;
        tracking= new GraphHopperRoadManager("2d356bfa-e7cb-4b6b-b3d8-0271b88627b6",false);
        tracking.setElevation(true);
        tracking.addRequestOption("&locale=pl");
        tracking.addRequestOption("&profile=foot");
        location=new Stack<GeoPoint>();
        this.level=level;
        tracking_buliding=new Tracking_buliding();
    }


    public Road add_tracking(int level,GeoPoint next)
    {
        if (location.size()>0)
        return tracking(lost_level,location.peek(),next,level);
        return null;
    }

    public Road  Aktulaizuj_location(int level,GeoPoint newlocation)
    {
        if(building)
            return  tracking_buliding.Tracking(level,newlocation,location.get(0),lost_level);
        else
            return Tracking_OSRM(newlocation,location.get(0));
    }

    public Road tracking(int level_begin,GeoPoint begin, GeoPoint end,int level_end)
    {
        this.location.push(end);
        this.lost_level=level_end;
         if(building)
            return tracking_buliding.Tracking(level_begin,begin,end,level_end);
         else
            return Tracking_OSRM(begin,end);
    }

    public void Remove_Tracking(int index)
    {
        location.remove(index);
    }
    private Road Tracking_OSRM(GeoPoint begin, GeoPoint end)
    {
         ArrayList<GeoPoint> punkty=new ArrayList<GeoPoint>();
        punkty.add(begin);
        punkty.add(end);
        Road road =tracking.getRoad(punkty);
        return road;
    }







}
