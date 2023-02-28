package com.Tracking;

import static java.lang.Integer.parseInt;

import android.content.Context;
import android.location.Location;

import com.example.nawigacja_po_umk.Add_marker;
import com.lokalizator.Akcje_na_lokacizacji;
import com.search_location.Item;

import org.osmdroid.bonuspack.routing.Road;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Polyline;

import java.util.ArrayList;

public class activity_Tracking_in_building  implements Akcje_na_lokacizacji {


    private int levelmax;
    private int levelmin;
    private Traking traking;
    public MapView mapView;
    protected Context kontekst;
    protected Location location;
    private com.Tracking.trasa[] trasa;
    private double delta=0.01;
    private ArrayList<Marker>[] markers;

    public activity_Tracking_in_building(MapView mapView, Context kontekst, boolean building, int maxlevel, int level_min) {
        this.levelmax=maxlevel;
        this.levelmin=level_min;
        this.kontekst=kontekst;
        this.mapView=mapView;
        this.traking=new Traking(kontekst,0,building);
        this.location=null;
        trasa=new trasa[levelmax-levelmin+1];
        deklarowanie_markers();
    }
    public  trasa get_trasa()
    {
        return trasa[(int)location.getAccuracy()- levelmin];
    }
    public int actual_level()
    {
        return (int)location.getAccuracy()- levelmin;
    }
    public void add_tracking_map(int index)
    {
        if(markers[index].size()>0)
            mapView.getOverlays().addAll(markers[index]);
        if(trasa[index]!=null )
            mapView.getOverlays().add(trasa[index].polyline());
        mapView.invalidate();
    }
    private void  deklarowanie_markers()
    {
        this.markers=new ArrayList[levelmax-levelmin+1];
        for (int i=0;i<markers.length;i++)
        {
            markers[i]=new ArrayList<Marker>();
        }
    }


    protected com.Tracking.trasa getTrasa(int index) {
        return trasa[index];
    }


    protected void aktualization_trasa(GeoPoint newlocation)
    {
        trasa[(int)location.getAccuracy()- levelmin].ActualizacjaLotalizacji(traking.Aktulaizuj_location(0,newlocation));
    }


    public void remove_tracking() {
        if(trasa!=null) {
            for(int i=0;i<trasa.length;i++)
                remove_polyline(trasa[i].polyline);
            trasa = null;
        }
        for(int j=0;j<markers.length;j++)
            for(int i=0;i<markers[j].size();i++)
                markers[j].get(i).remove(mapView);

        mapView.invalidate();

    }


    public void add_tracking(Item item)
    {
        if(this.location!=null) {
            markers[item.level].add(Add_marker.Add_marker(item.item, mapView, "miejsce docelowe"));
            if (trasa[item.level] == null)
            {
                Road road=traking.tracking((int)location.getAccuracy(),new GeoPoint(this.location.getLatitude(),location.getLongitude()), item.item.getBoundingBox().getCenter(),parseInt(item.item.mExtendedData.get("level")));
                trasa[item.level]=new trasa(road,item.item.mName,kontekst,mapView);
            }
            else {
                remove_polyline(trasa[item.level].polyline);
                trasa[item.level].add_trasa(traking.add_tracking(parseInt(item.item.mExtendedData.get("level")),item.item.getBoundingBox().getCenter()),item.item.mName);
            }
            if(item.level==((int)location.getAccuracy()-levelmin)) {
                mapView.getOverlays().add(1,trasa[item.level].polyline());
                mapView.getOverlays().add(markers[item.level].get(markers[item.level].size()-1));
                mapView.invalidate();
            }
        }
    }

    protected void  remove_polyline(Polyline polyline)
    {
        if(polyline!=null)
            mapView.getOverlays().remove(polyline);
    }

    public void aktualizuj_tracking(Location newlocation) {

        if(trasa!=null && trasa[(int)location.getAltitude()-levelmin]!=null) {
            remove_polyline(trasa[(int) location.getAltitude()-levelmin].polyline);
            mapView.getOverlays().add(0, trasa[(int)location.getAltitude()-levelmin].polyline());
            aktualizuj_tracking(newlocation);
            mapView.invalidate();
        }

    }

    @Override
    public boolean warunek(Location location) {

        if(trasa[(int)location.getAccuracy()-levelmin]!=null) {
            ArrayList<GeoPoint> points = trasa[(int)location.getAccuracy()-levelmin].print_point();
            GeoPoint point_l=new GeoPoint(location);
            return trasa[0].odległość(points.get(0),point_l)> delta;
        }
        else
            return true;
    }

    @Override
    public void Akcja(Location location) {
    aktualizuj_tracking(location);
    }

    @Override
    public void Akcje_is_false(Location location) {

    }
}
