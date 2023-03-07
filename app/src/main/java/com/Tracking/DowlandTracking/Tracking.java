package com.Tracking.DowlandTracking;

import android.content.Context;
import android.os.Bundle;

import org.osmdroid.bonuspack.routing.GraphHopperRoadManager;
import org.osmdroid.bonuspack.routing.Road;
import org.osmdroid.util.GeoPoint;

import java.util.Stack;

public abstract class Tracking {

    Context context;
    Stack<GeoPoint> location;
    int level;
    int lost_level;


    public Tracking(Context context, int level)
    {
        newInstance(context, level);
    }

    public void newInstance(Context context, int level) {
        this.context=context;
        location=new Stack<GeoPoint>();
        this.level=level;
    }
    public Road add_tracking(int level,GeoPoint next)
    {
        if (location.size()>0)
        return tracking(lost_level,location.peek(),next,level);
        return null;
    }

    public Road  Aktulaizuj_location(int level,GeoPoint newlocation)
    {
            return Dowland_Tracking(newlocation,location.get(0));
    }

    public Road tracking(int level_begin,GeoPoint begin, GeoPoint end,int level_end)
    {
        this.location.push(end);
        this.lost_level=level_end;
            return Dowland_Tracking(begin,end);
    }

    public void Remove_Tracking(int index)
    {
        location.remove(index);
    }
    public void Remove_Tracking()
    {
        location.removeAllElements();
    }

    protected abstract Road Dowland_Tracking(GeoPoint begin, GeoPoint end);

}
