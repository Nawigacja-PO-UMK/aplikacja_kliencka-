package com.Tracking.trasy;

import android.annotation.SuppressLint;

import org.osmdroid.bonuspack.routing.Road;
import org.osmdroid.bonuspack.routing.RoadNode;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Polyline;

import java.util.ArrayList;
import java.util.List;

public class trasa_building extends  trasa {



    private  int level;
    public List<Polyline> polylines;
    public trasa_building(int level){super();this.level=level;

        polylines=new ArrayList<>();}


    public void setLevel(int level) {
        this.level = level;
    }

    @SuppressLint("SuspiciousIndentation")
    @Override
    public void add_last_Target() {
            Marker marker = markers_target.get(markers_target.size() - 1);
            if (roads.size()>0 && roads.size()>0 && roads.get(0).mRouteHigh.size()>0
                    && (int )roads.get(0).mRouteHigh.get(0).getAltitude()== marker.getPosition().getAltitude())
                mapView.getOverlays().add(marker);
    }

    @Override
    public List<Marker> print_marker()
    {
        ArrayList markers= new ArrayList<>();
        for(int i=0;i<markers_target.size();i++)
        {
            if(markers_target.get(i).getPosition().getAltitude()==level())
                markers.add(markers_target.get(i));
        }
        return markers;
    }

    public List<Marker> print_marker(int level)
    {
        ArrayList markers= new ArrayList<>();
        for(int i=0;i<markers_target.size();i++)
        {
            if(markers_target.get(i).getPosition().getAltitude()==level)
                markers.add(markers_target.get(i));
        }
        return markers;
    }

    public double level()
    {
        if(roads.get(0).mRouteHigh.size()>0)
        return roads.get(0).mRouteHigh.get(0).getAltitude();
        else
            return 0;
    }
    @SuppressLint("SuspiciousIndentation")
    public List<Polyline> polyline(int level)
    {
        polylines= new ArrayList<>();
        polyline=new Polyline();
        ArrayList<GeoPoint> points=All_print_point();
            for (int i = 0; i < points.size(); i++) {
                if (points.get(i).getAltitude() == level)
                    polyline.addPoint(points.get(i));
                else
                {
                    if(polyline.getActualPoints().size()>0) {
                        polyline.setWidth(7.0f);
                        polyline.setColor(Color);
                        polylines.add(polyline);
                        polyline = new Polyline();
                    }
                }
            }
        polyline.setWidth(7.0f);
        polyline.setColor(Color);
        polylines.add(polyline);

        return polylines;
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
    public ArrayList<GeoPoint> All_print_point() {

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

    private int search_last_on_level()
    {
        int index = 0;
        for(int i=0;i<roads.get(0).mRouteHigh.size();i++)
        {
            if (roads.get(0).mRouteHigh.get(0).getAltitude()==roads.get(0).mRouteHigh.get(i).getAltitude())
                index=i;

        }
        return index;
    }

    @Override
    public boolean is_end_tracking(GeoPoint newpoint_start)
    {
        if(roads.size()>0) {
            int index = search_last_on_level();
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

    public boolean is_level() {

        return roads!=null && roads.size()> 0 && roads.get(0).mRouteHigh.size()>0;
    }
}
