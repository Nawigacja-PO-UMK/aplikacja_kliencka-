package com.Tracking.DowlandTracking;

import static com.google.android.material.internal.ContextUtils.getActivity;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.Toast;

import com.Tracking.activity_Tracking;

import org.osmdroid.bonuspack.routing.GraphHopperRoadManager;
import org.osmdroid.bonuspack.routing.Road;
import org.osmdroid.util.GeoPoint;

import java.util.Stack;

public abstract class Tracking {

    Context context;
    Stack<GeoPoint> location;
    int lost_level;
    Dialog dialog;

    public Tracking(Context context)
    {
        newInstance(context);
    }

    public void newInstance(Context context) {
        this.context=context;
        location=new Stack<GeoPoint>();

    }
    public void  add_tracking(GeoPoint next,Road road,activity_Tracking activity_tracking)
    {
        if (location.size()>0)
        tracking(location.peek(),next,road,activity_tracking);
    }

    public void  Aktulaizuj_location(int level,GeoPoint newlocation,Road road,activity_Tracking activity_tracking)
    {
        Dowland_Tracking(newlocation,location.get(0),road, activity_tracking );
    }

    public void tracking(GeoPoint begin, GeoPoint end,Road road,activity_Tracking activity_tracking)
    {
        this.location.push(end);
        Dowland_Tracking(begin,end,road,activity_tracking);
    }

    public void Remove_Tracking(int index)
    {
        location.remove(index);
    }
    public void Remove_Tracking()
    {
        location.removeAllElements();
    }

    protected abstract void Dowland_Tracking(GeoPoint begin, GeoPoint end,Road road,activity_Tracking activity_tracking);

}
