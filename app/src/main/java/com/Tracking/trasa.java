package com.Tracking;


import android.content.Context;
import android.graphics.drawable.Drawable;

import com.example.nawigacja_po_umk.R;

import org.osmdroid.bonuspack.routing.Road;
import org.osmdroid.bonuspack.routing.RoadNode;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Polyline;

import java.util.ArrayList;
import java.util.List;

public class trasa {

    private ArrayList<Road> roads;
    private ArrayList<String> Tracking;
    int numer_trasy;
    int numer_etapu_trasy;
    public int Color;
    public Polyline polyline=null;
    private double delta=0.001;
    Context context;
    MapView mapView;
    Drawable nodeIcon;
    List<Marker> markers;
    public trasa(Road road,String newTracking,Context context,MapView mapView)
    {
        this.roads=new ArrayList<Road>();
        this.Tracking=new ArrayList<String>();
        numer_etapu_trasy=0;
        numer_trasy=0;
        this.Color=0xFF0000FF;
        this.Tracking=new ArrayList<String>();
        this.context=context;
        this.mapView=mapView;
        this.nodeIcon =context.getDrawable(R.drawable.marker_node);
        markers=new ArrayList<>();
        add_trasa(road,newTracking);
    }
    public boolean is_trasa()
    {
        return roads.size()>0;
    }

   public void add_trasa(Road road,String newTracking)
    {
        this.roads.add(road);
        this.Tracking.add(newTracking);
    }

    public void ActualizacjaLotalizacji(Road newroad)
    {
            roads.set(0,newroad);
    }

    public boolean is_end_tracking(GeoPoint newpoint_start)
    {
        return odległość(newpoint_start,last_point())< delta;
    }
    public GeoPoint last_point()
    {
        int index=roads.get(0).mRouteHigh.size()-1;
        return roads.get(0).mRouteHigh.get(index);
    }
    public void  ActualizacjaLotalizacji(GeoPoint newpoint_start)
    {

            Road tmpRoad = roads.get(0);
            tmpRoad.mLength += odległość(newpoint_start, roads.get(0).mRouteHigh.get(1)) - odległość(roads.get(0).mRouteHigh.get(0), roads.get(0).mRouteHigh.get(1));

            if(odległość(tmpRoad.mRouteHigh.get(0),newpoint_start)<delta/10)
                tmpRoad.mRouteHigh.set(0, newpoint_start);
            else
                tmpRoad.mRouteHigh.add(0, newpoint_start);

            roads.set(0, tmpRoad);

    }
    public boolean is_route(GeoPoint now_location)
    {
        return (odległość(roads.get(0).mRouteHigh.get(0),now_location)
                >odległość(roads.get(0).mRouteHigh.get(1),now_location));
    }
    public void  remove_tracking(int index)
    {
        roads.remove(index); polyline=null;
        Tracking.remove(index);
    }

    public double odległość(GeoPoint punkt1 ,GeoPoint punkt2)
    {
        return Math.sqrt(Math.pow(punkt1.getLongitude()-punkt2.getLongitude(),2)
                +Math.pow(punkt1.getLatitude()-punkt2.getLatitude(),2));
    }
    public String print_descryption()
    {
        String allinstructions=new String();
        int etap=numer_etapu_trasy;
        String instruction;
        for (int i =numer_trasy;i<roads.size();i++) {
            Road road= roads.get(i);
            for(int j=etap;j<road.mNodes.size();j++)
            {
                if (j==0) {
                    allinstructions += "zacznij na końcu\n\n";
                    continue;
                }
                if(j==road.mNodes.size()-1) {
                    allinstructions += "Dotarłeś do Punktu Docelowego\n\n";
                    continue;
                }
                allinstructions+=Road.getLengthDurationText(context, road.mNodes.get(j).mLength, road.mNodes.get(j).mDuration);
                instruction= road.mNodes.get(j).mInstructions;
                if(instruction!=null | j!=road.mNodes.size()-2)
                    allinstructions+=" "+instruction;
                else
                    allinstructions+="Powinineś zobaczyć punkt docelowy";
                allinstructions+="\n\n";
            }
            etap=0;
        }
        return allinstructions;
    }
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
                markers.add(nodeMarker);
            }
        }
        return markers;
    }
    public List<Marker> getMarkers()
    {
        return markers;
    }
    public void  remove_Markers()
    {
        markers=new ArrayList<>();

    }


    ArrayList<GeoPoint> print_point()
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


    public void next()
    {
        double lenght = 0;
        if(roads.get(numer_trasy).mNodes.size()-1 > numer_etapu_trasy) {
            lenght = odległość(roads.get(0).mRouteHigh.get(0), roads.get(0).mRouteHigh.get(1));
            roads.get(0).mRouteHigh.remove(0);
        }
        else {
            if (roads.size() - 1 > numer_trasy) {
                numer_etapu_trasy = 0;
                if (roads.size() > 0)
                    roads.remove(0);
                lenght = 0;
            }
        }
            roads.get(0).mLength-=lenght;
    }

    public String print_name_tracking() {

        String tracking = new String();
        for (int i = 0; i < Tracking.size() ; i++)
            tracking+=Tracking.get(i)+" "+roads.get(i).getLengthDurationText(context,1,roads.get(i).mDuration);
            //tracking+=Tracking.get(i)+"\t\t\t Czas:"+ roads.get(i).mDuration+"\t\t\t"+(roads.get(i).mLength*1000)+" m\n";
        return tracking;
    }


    public Polyline polyline()
    {
        polyline=new Polyline();
        ArrayList<GeoPoint> points=print_point();
        for(int i=0;i<points.size();i++)
        {
            polyline.addPoint(points.get(i));
        }
        polyline.setWidth(7.0f);
        polyline.setColor(Color);
        return polyline;
    }

}
