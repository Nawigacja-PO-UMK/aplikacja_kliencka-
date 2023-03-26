package com.Tracking.DowlandTracking;

import android.app.Dialog;
import android.content.Context;

import org.osmdroid.bonuspack.routing.GraphHopperRoadManager;
import org.osmdroid.bonuspack.routing.Road;
import org.osmdroid.util.GeoPoint;

import java.util.ArrayList;

public class OSRM_Tracking  extends Tracking {

    GraphHopperRoadManager tracking;
    Dialog dialog;
    public OSRM_Tracking(Context context, int level) {
        super(context, level);
        tracking= new GraphHopperRoadManager("2d356bfa-e7cb-4b6b-b3d8-0271b88627b6",false);
        tracking.setElevation(true);
        tracking.addRequestOption("&locale=pl");
        tracking.addRequestOption("&profile=foot");
    }

    @Override
    protected Road Dowland_Tracking(GeoPoint begin, GeoPoint end)
    {
        ArrayList<GeoPoint> punkty=new ArrayList<GeoPoint>();
        punkty.add(begin);
        punkty.add(end);
        Road road =tracking.getRoad(punkty);
        return road;
    }
}
