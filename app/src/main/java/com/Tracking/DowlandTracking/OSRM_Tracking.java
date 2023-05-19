package com.Tracking.DowlandTracking;

import android.app.Dialog;
import android.content.Context;
import android.widget.Toast;

import com.Tracking.activity_Tracking;

import org.osmdroid.bonuspack.routing.GraphHopperRoadManager;
import org.osmdroid.bonuspack.routing.Road;
import org.osmdroid.util.GeoPoint;

import java.util.ArrayList;

public class OSRM_Tracking  extends Tracking {

    GraphHopperRoadManager tracking;
    Dialog dialog;
    public OSRM_Tracking(Context context) {
        super(context);
        tracking= new GraphHopperRoadManager("2d356bfa-e7cb-4b6b-b3d8-0271b88627b6",false);
        tracking.setElevation(true);
        tracking.addRequestOption("&locale=pl");
        tracking.addRequestOption("&profile=foot");
    }

    @Override
    protected void  Dowland_Tracking(GeoPoint begin, GeoPoint end, Road road,activity_Tracking activity_tracking)
    {
        ArrayList<GeoPoint> punkty=new ArrayList<GeoPoint>();
        punkty.add(begin);
        punkty.add(end);
        Road road1=tracking.getRoad(punkty);
        road.mRouteHigh=road1.mRouteHigh;
        road.mNodes=road1.mNodes;
        road.mStatus=road1.mStatus;
        road.mDuration=road1.mDuration;
        road.mLegs=road1.mLegs;
        road.mLength=road1.mLength;
    }
}
