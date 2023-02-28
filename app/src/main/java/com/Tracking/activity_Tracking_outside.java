package com.Tracking;

import android.app.ProgressDialog;
import android.content.Context;
import android.location.Address;
import android.location.Location;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.nawigacja_po_umk.Add_marker;
import com.lokalizator.Akcje_na_lokacizacji;
import com.search_location.search_location;

import org.osmdroid.bonuspack.location.POI;
import org.osmdroid.bonuspack.routing.Road;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Polyline;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

public class activity_Tracking_outside implements Akcje_na_lokacizacji {

    protected ArrayList<Marker>  markers;
    public com.Tracking.trasa trasa;
    public MapView mapView;
    protected Context kontekst;
    protected Traking traking;
    protected GeoPoint location;
    protected  double delta=0.0001;
    protected boolean now_tracking;
    public activity_Tracking_outside(MapView mapView, Context kontekst, boolean building) {

        this.kontekst=kontekst;
        this.mapView=mapView;
        this.traking=new Traking(kontekst,0,building);
        this.location=null;
        this.markers=new ArrayList<Marker>();
        now_tracking=false;
    }


    protected com.Tracking.trasa getTrasa(int index) {
        return trasa;
    }


    public void add_tracking(Object adress)
    {

        Address location=(Address) adress;
        GeoPoint newposition= new GeoPoint(location.getLatitude(),location.getLongitude());
        if(this.location!=null)
        {
            markers.add(Add_marker.Add_marker(location, mapView));
            if (trasa != null && trasa.is_trasa())
            {
                remove_polyline(trasa.polyline);
                trasa.add_trasa(traking.add_tracking(0, newposition),search_location.search_name_Adress(location));
            }
            else {

                Road road=traking.tracking(0,this.location,newposition,0);
                trasa=new trasa(road,search_location.search_name_Adress(location),kontekst,mapView);
            }
            mapView.getOverlays().add(0,trasa.polyline());
            mapView.getOverlays().addAll(1,trasa.bulid_markers_Tracking());
            mapView.getOverlays().add(markers.get(markers.size()-1));
            mapView.invalidate();
        }
        else
            Toast.makeText(kontekst, "Twoje położenie jest nie znanie", Toast.LENGTH_SHORT).show();


    }

    public void aktualizuj_tracking(GeoPoint newlocation) {

        now_tracking=true;
        location=newlocation;
        if(trasa!=null) {

            synchronized (trasa) {
                remove_polyline(trasa.polyline);
                aktualization_trasa(newlocation);
                if(trasa.markers.size()>0)
                 mapView.getOverlays().removeAll(trasa.markers);
                trasa.remove_Markers();
                mapView.getOverlays().add(0, trasa.polyline());
                mapView.getOverlays().addAll(1,trasa.bulid_markers_Tracking());
                mapView.invalidate();
            }

        }
        now_tracking=false;

    }


     public void remove_tracking()
    {
        if(trasa!=null)
        {
            remove_polyline(trasa.polyline);
            mapView.getOverlays().removeAll(trasa.getMarkers());
            trasa.remove_Markers();
            trasa = null;
        }
        for(int i=0;i<markers.size();i++)
            markers.get(i).remove(mapView);
        mapView.invalidate();
    }

    protected void aktualization_trasa(GeoPoint newlocation)
    {

            if(!trasa.is_end_tracking(newlocation))
                if(trasa.is_route(newlocation))
                    trasa.ActualizacjaLotalizacji(traking.Aktulaizuj_location(0, newlocation));
                else
                    trasa.ActualizacjaLotalizacji(newlocation);
            else
                end_tracking(0);

    }
    void end_tracking(int index)
    {
        mapView.getOverlays().remove(markers.get(index));
        markers.remove(index);
        trasa.remove_tracking(index);
        mapView.getOverlays().removeAll(trasa.markers);
        trasa.remove_Markers();
        mapView.invalidate();
        now_tracking=true;
        traking.Remove_Tracking(index);
        Toast.makeText(kontekst, "Dodarłeś do celu", Toast.LENGTH_SHORT).show();
    }



    protected void  remove_polyline(Polyline polyline)
    {
        if(polyline!=null)
            mapView.getOverlays().remove(polyline);
    }

    @Override
    public boolean warunek(android.location.Location location) {


        if(trasa!=null  && !now_tracking)
        {
            ArrayList<GeoPoint> points = trasa.print_point();
            GeoPoint point_l=new GeoPoint(location);
            if(points.size()>1)
            return trasa.odległość(points.get(0),point_l)> delta;
            else
                return false;
        }
        else
            if(!now_tracking)
                return true;
            else
                return false;
    }


    @Override
    public void Akcja(android.location.Location location) {

        aktualizuj_tracking(new GeoPoint(location.getLatitude(),location.getLongitude()));

    }

    @Override
    public void Akcje_is_false(Location location) {

    }
}
