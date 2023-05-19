package com.Tracking.trasy;


import android.content.Context;
import android.os.Parcel;
import android.widget.Toast;

import org.osmdroid.bonuspack.routing.Road;
import org.osmdroid.bonuspack.routing.RoadNode;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Polyline;

import java.util.ArrayList;
import java.util.List;

public class trasa_outside extends trasa {

    public trasa_outside(Road road, String newTracking, Context context, MapView mapView,Marker Target)
    {
        super(road, newTracking,context, mapView,Target);
    }

    public  trasa_outside()
    {
        super();
    }
    @Override
    public void add_last_Target() {
    mapView.getOverlays().add(markers_target.get(markers_target.size()-1));
    }

    @Override
    public boolean is_end_tracking(GeoPoint newpoint_start)
    {
        if(roads.size()>0) {
            int index = roads.get(0).mRouteHigh.size() - 1;
            return odległość(newpoint_start, roads.get(0).mRouteHigh.get(index)) < delta;
        }
        else
            return false;
    }

    @Override
    public void  ActualizacjaLotalizacji(GeoPoint newpoint_start)
    {
        if(roads.size()>0) {
            Road tmpRoad = roads.get(0);
            MiniDistance miniDistance = mini_distance_route(newpoint_start);
            GeoPoint between = null;
            boolean is_remove_lost = false;
            if (miniDistance.index != 0) {
                if (miniDistance.index + 1 == tmpRoad.mRouteHigh.size()) {
                    between = tmpRoad.mRouteHigh.get(miniDistance.index - 1);
                } else {
                    if (condition_in_between(tmpRoad.mRouteHigh.get(miniDistance.index),
                            tmpRoad.mRouteHigh.get(miniDistance.index + 1),
                            newpoint_start)) {
                        between = tmpRoad.mRouteHigh.get(miniDistance.index + 1);
                        is_remove_lost = true;
                    } else if (condition_in_between(tmpRoad.mRouteHigh.get(miniDistance.index),
                            tmpRoad.mRouteHigh.get(miniDistance.index - 1),
                            newpoint_start)) {
                        between = tmpRoad.mRouteHigh.get(miniDistance.index - 1);
                    }
                }
            } else {
                if (condition_in_between(tmpRoad.mRouteHigh.get(0), tmpRoad.mRouteHigh.get(1), newpoint_start)) {
                    between = tmpRoad.mRouteHigh.get(1);
                    is_remove_lost = true;
                }
            }
            if (between != null) {
                double odległośćB = odległość(newpoint_start, between);
                GeoPoint point = Geopoint_between_Geopoint(tmpRoad.mRouteHigh.get(miniDistance.index),
                        between,
                        odległośćB / miniDistance.distance);
                for (int i = miniDistance.index - 1; i >= 0; i--) {
                    tmpRoad.mRouteHigh.remove(i);

                }
                if (is_remove_lost) {
                    tmpRoad.mRouteHigh.remove(0);
                }
                tmpRoad.mRouteHigh.add(0, point);
                tmpRoad.mLength = distance(0) / 1000;
                tmpRoad.mDuration = tmpRoad.mLength * 700;
            }
            roads.set(0, tmpRoad);
        }
    }

    private boolean condition_in_between(GeoPoint A,GeoPoint B,GeoPoint point)
    {
        if(A.getLongitude()<point.getLongitude() && B.getLongitude()>point.getLongitude())
            return true;
        if(B.getLongitude()<point.getLongitude() && A.getLongitude()>point.getLongitude())
            return true;
        if(A.getLatitude()<point.getLatitude() && B.getLatitude()>point.getLatitude())
            return true;
        if(B.getLatitude()<point.getLatitude() && A.getLatitude()>point.getLatitude())
            return true;

        return false;
    }
    private GeoPoint Geopoint_between_Geopoint( GeoPoint A ,GeoPoint B, double ratio)
    {
        double a=(A.getLatitude()- B.getLatitude());
        double latitude =(B.getLatitude()- A.getLatitude())/ratio+A.getLatitude();
        double longitude =(B.getLongitude()- A.getLongitude())/ratio+A.getLongitude();
        return  new GeoPoint(latitude,longitude);
    }
    @Override
    public boolean is_route(GeoPoint location)
    {
        return mini_distance_route(location).distance< delta;
    }

    @Override
    public List<Marker> print_marker() {
        return markers_target;
    }


    @Override
    public List<Marker> bulid_markers_Tracking()
    {

        for( int j=0;j< roads.size();j++) {
            Road road= roads.get(j);
            for (int i = 0; i < road.mNodes.size(); i++) {
                RoadNode node = road.mNodes.get(i);
                Marker nodeMarker = new Marker(mapView);
                nodeMarker.setPosition(node.mLocation);
                nodeMarker.setIcon(nodeIcon);
                nodeMarker.setTitle("Krok"+String.valueOf((i+1)*(j+1)));
                nodeMarker.setSnippet(node.mInstructions);
                nodeMarker.setSubDescription(Road.getLengthDurationText(context, node.mLength, node.mDuration));
                markers_instruction.add(nodeMarker);
            }
        }
        return markers_instruction;
    }

   @Override
    public ArrayList<GeoPoint> print_point()
    {
        ArrayList<GeoPoint> pozostała_trasa=new ArrayList<GeoPoint>();

        for (int i =0;i<roads.size();i++) {
           Road road= roads.get(i);
           for(int j=0;j<road.mRouteHigh.size();j++)
           {
              pozostała_trasa.add(road.mRouteHigh.get(j));
           }
        }
        return  pozostała_trasa;
    }
}

