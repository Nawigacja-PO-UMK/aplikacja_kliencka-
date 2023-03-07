package com.Tracking.DowlandTracking;

import android.content.Context;

import org.osmdroid.bonuspack.routing.Road;
import org.osmdroid.util.GeoPoint;

public class Building_Tracking extends Tracking {


    public Building_Tracking(Context context, int level) {
        super(context, level);
    }

    @Override
    protected Road Dowland_Tracking(GeoPoint begin, GeoPoint end) {
            Road trasa=new Road();
            return trasa;
    }
}
