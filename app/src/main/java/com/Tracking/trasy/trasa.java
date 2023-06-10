package com.Tracking.trasy;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.Toast;

import com.example.nawigacja_po_umk.R;

import org.osmdroid.bonuspack.routing.Road;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Polyline;

import java.util.ArrayList;
import java.util.List;

public abstract class trasa {
    public ArrayList<Road> roads;
    protected ArrayList<String> Tracking;
    public int Color;
    public Polyline polyline=null;
    protected double delta=20;
    Context context;
    MapView mapView;
    Drawable nodeIcon;
    public List<Marker> markers_instruction;
    public List<Marker> markers_target;

    public abstract ArrayList<GeoPoint>   print_point();
    public abstract List<Marker> bulid_markers_Tracking();
    public abstract boolean is_end_tracking(GeoPoint newpoint_start);
    public abstract void  ActualizacjaLotalizacji(GeoPoint newpoint_start);
    public abstract boolean is_route(GeoPoint location);
    public  abstract List<Marker>print_marker();
    public trasa(Road road, String newTracking, Context context, MapView mapView,Marker target)
    {
     new_instancion(road,newTracking,context,mapView,target);
    }
    public void removeTargets()
    {
        for(int i=0;i<markers_target.size();i++) {
            markers_target.get(i).remove(mapView);
        }
    }
    public void removeTarget(int index)
    {

        markers_target.get(index).remove(mapView);
        markers_target.remove(index);
    }
    public trasa(){};

    public void new_instancion(Road road, String newTracking, Context context, MapView mapView,Marker target)
    {

        this.roads=new ArrayList<Road>();
        this.Tracking=new ArrayList<String>();
        this.Color=0xFF0000FF;
        this.Tracking=new ArrayList<String>();
        this.context=context;
        this.mapView=mapView;
        this.nodeIcon =context.getDrawable(R.drawable.marker_node);
        markers_instruction =new ArrayList<>();
        this.markers_target=new ArrayList<>();
        add_trasa(road,newTracking,target);
    }
    public boolean is_trasa()
    {
        return roads.size()>0;
    }

    public void add_trasa(Road road,String newTracking,Marker target)
    {
        this.roads.add(road);
        markers_target.add(target);
        this.Tracking.add(newTracking);
    }

    public void ActualizacjaLotalizacji(Road newroad)
    {
        roads.set(0,newroad);
    }

    public abstract void add_last_Target();

    public double distance(int index)
    {
        double distance=0;
        for(int i=1; i<roads.get(index).mRouteHigh.size();i++)
        {
            distance+=roads.get(index).mRouteHigh.get(i).distanceToAsDouble(roads.get(index).mRouteHigh.get(i-1));
        }
        return distance;
    }

   static public double Distance(Road road)
   {
       double distance=0;
       for(int i=1; i<road.mRouteHigh.size();i++)
       {
           distance+=road.mRouteHigh.get(i).distanceToAsDouble(road.mRouteHigh.get(i-1));
       }
       return distance;
   }


    public MiniDistance mini_distance_route(GeoPoint location)
    {
        MiniDistance minidistance=new MiniDistance();
        minidistance.distance=Double.MAX_VALUE;
        minidistance.index=-1;
        if(roads.size()>0) {
            int kroki=roads.get(0).mRouteHigh.size();
            if(roads.get(0).mRouteHigh.size()>100)
                kroki=50;
            for (int i = 0; i < kroki; i++) {
                if(minidistance.distance>odległość(location,roads.get(0).mRouteHigh.get(i))-0.0000002) {
                    minidistance.distance = odległość(location, roads.get(0).mRouteHigh.get(i));
                    minidistance.index=i;
                }
            }
        }
        return minidistance;
    }

    public double from_route(GeoPoint location)
    {
        if(roads.size()>0 && roads.get(0).mRouteHigh.size()>0)
            return odległość(roads.get(0).mRouteHigh.get(0),location);
        else
            return 0;
    }
    public void  remove_tracking(int index)
    {
        roads.remove(index); polyline=null;
        Tracking.remove(index);
    }

    public double odległość(GeoPoint punkt1 ,GeoPoint punkt2)
    {
        return punkt1.distanceToAsDouble(punkt2);
        //Toast.makeText(context,String.valueOf(a)+" "+String.valueOf(b), Toast.LENGTH_SHORT).show();
    }
    public String now_instruction(GeoPoint location)
    {
        int kroki=roads.get(0).mNodes.size();
        if(kroki>100)
        kroki=100;
        for(int i=0;i<kroki;i++)
        {
            if(odległość(roads.get(0).mNodes.get(i).mLocation,location)<delta) {
                String instructions= roads.get(0).mNodes.get(i).mInstructions;
                for(int j=i;j>=0;j--)
                {
                    roads.get(0).mNodes.remove(j);
                }
                return instructions;
            }
        }
        return null;
    }
    public String print_descryption()
    {
        String allinstructions=new String();
        String instruction;
        int kroki=roads.get(0).mNodes.size();
            Road road= roads.get(0);
            if(kroki>10)
                kroki=10;
            for(int j=0;j<kroki;j++)
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
        return allinstructions;
    }

    public List<Marker> getMarkers_instruction()
    {
        return new ArrayList<>();///markers_instruction;
    }
    public void  remove_Markers()
    {
        markers_instruction =new ArrayList<>();

    }
    public String print_name_tracking() {

        String tracking = new String();
        for (int i = 0; i < Tracking.size() ; i++)
            tracking+=Tracking.get(i)+" "+roads.get(i).getLengthDurationText(context,roads.get(i).mLength,roads.get(i).mDuration)+"\n";
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

    public List<Marker> markers()
    {
        return print_marker();
    }
}
