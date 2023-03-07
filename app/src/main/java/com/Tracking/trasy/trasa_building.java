package com.Tracking.trasy;

import android.annotation.SuppressLint;
import android.content.Context;

import org.osmdroid.bonuspack.routing.Road;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Polyline;

import java.util.ArrayList;
import java.util.List;

public class trasa_building extends  trasa {


    public trasa_building(){ super();}

    @Override
    public void add_last_Target() {
        Marker marker=markers_target.get(markers_target.size()-1);
        if(roads.get(0).mRouteHigh.get(0).getAltitude()==marker.getPosition().getAltitude())
        mapView.getOverlays().add(marker);
    }

    public int level()
    {
        return 0;
    }
    @SuppressLint("SuspiciousIndentation")
    public Polyline polyline(int level)
    {
        polyline=new Polyline();
        ArrayList<GeoPoint> points=print_point();
        for(int i=0;i<points.size();i++)
        {
            if(points.get(i).getAltitude()==level)
            polyline.addPoint(points.get(i));
        }
        polyline.setWidth(7.0f);
        polyline.setColor(Color);
        return polyline;
    }
    @Override
    public ArrayList<GeoPoint> print_point() {
        ArrayList<GeoPoint> pozostała_trasa=new ArrayList<GeoPoint>();

        for (int i =0;i<roads.size();i++) {
            Road road= roads.get(i);
            for(int j=0;j<road.mRouteHigh.size();j++)
            {
                if(road.mRouteHigh.get(j).getAltitude()==level())
                pozostała_trasa.add(road.mRouteHigh.get(j));
            }
        }
        return  pozostała_trasa;
    }

    @Override
    public List<Marker> bulid_markers_Tracking() {
        return null;
    }

    @Override
    public boolean is_end_tracking(GeoPoint newpoint_start) {
        return false;
    }

    @Override
    public void ActualizacjaLotalizacji(GeoPoint newpoint_start) {

    }

    @Override
    public boolean is_route(GeoPoint location) {
        return false;
    }

    trasa_building(Road road, String newTracking, Context context, MapView mapView,Marker target)
    {
        super(road,newTracking,context,mapView,target);
    }



}
