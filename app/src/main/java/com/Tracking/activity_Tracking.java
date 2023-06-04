package com.Tracking;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.location.Address;
import android.location.Location;
import android.widget.Toast;

import com.Text_convert_voice;
import com.Tracking.DowlandTracking.Tracking;
import com.Tracking.trasy.trasa;
import com.Tracking.trasy.trasa_building;
import com.example.nawigacja_po_umk.Add_marker;
import com.example.nawigacja_po_umk.ekran_Tracking.screean_Tracking;
import com.lokalizator.Akcje_na_lokacizacji;
import com.search_location.search_location;

import org.osmdroid.bonuspack.routing.Road;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Polyline;

import java.lang.reflect.Array;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class activity_Tracking implements Akcje_na_lokacizacji {

    public com.Tracking.trasy.trasa trasa;
    public ArrayList<Address> addresses;
    public MapView mapView;
    protected Context kontekst;
    protected Tracking traking;
    protected GeoPoint location;
    protected  double delta=1.5;
    protected boolean now_tracking;
    private trasa typtrasa;
    private boolean run;
    private boolean[] wait;
    long time_last_actualization;
    private Text_convert_voice text_convert_voice;
    boolean voice;
    public screean_Tracking screean_tracking;
    Dialog dialog_loader;
    boolean first_location;
    public activity_Tracking(MapView mapView, Context kontekst, trasa typtrasa, Tracking tracking, screean_Tracking screean_tracking)  {
        this.typtrasa=typtrasa;
        this.kontekst=kontekst;
        this.mapView=mapView;
        this.traking=tracking;
        this.location=null;
        now_tracking=false;
        this.text_convert_voice=new Text_convert_voice(kontekst);
        voice=true;
        this.screean_tracking=screean_tracking;
        creating_dialog_tracking();
        first_location=false;
        this.run=true;
        this.wait=new  boolean[1];
        this.wait[0]=false;
        addresses=new ArrayList<>();
    }

    public GeoPoint getLocation(){
        return location;
    }
    @SuppressLint("SuspiciousIndentation")
    public void setRun(boolean run) {
        this.run = run;
        if(location!=null && run)
        aktualizuj_tracking(location);
    }
    public void newinstancjon(MapView mapView, screean_Tracking screean_tracking)
    {
        this.mapView=mapView;
        this.screean_tracking=screean_tracking;
        if(this.trasa!=null) {
            trasa trasa = this.trasa;
            this.trasa = null;
            int count_list=addresses.size()-1;
            for (int i = count_list; i >=0; i--) {
                add_tracking(addresses.get(i));
                addresses.remove(count_list);
            }
        }
    }
    private void  creating_dialog_tracking()
    {
        dialog_loader= new Dialog(kontekst);
        dialog_loader.setTitle("loading");
        dialog_loader.setTitle("Ustalanie trasy");
    }


    public void setVoice(boolean voice)
    {
        this.voice=voice;
    }

    public trasa getTrasa() {
        return trasa;
    }


    public void add_tracking(Object adress)
    {

        Address location=(Address) adress;
        GeoPoint newposition= new GeoPoint(location.getLatitude(),location.getLongitude());
        if(adress  instanceof  com.search_location.Address)
        {
            newposition.setAltitude(((com.search_location.Address) adress ).getAltitude());
        }
        if(this.location!=null)
        {
            this.addresses.add(location);
            if (trasa != null && trasa.is_trasa())
            {
                remove_polyline(trasa.polyline);
                Road road=new Road();
                traking.add_tracking(newposition,road,this);
                trasa.add_trasa(road,search_location.search_name_Adress(location),
                        Add_marker.Add_marker(location,kontekst, mapView));
            }
            else {
                Road road=new Road();
                this.location.setAltitude(0);
                traking.tracking(this.location,newposition,road,this);
                typtrasa.new_instancion(road,search_location.search_name_Adress(location),kontekst,mapView
                ,Add_marker.Add_marker(location,kontekst, mapView));
                trasa=typtrasa;
            }
            mapView.getOverlays().add(0,trasa.polyline());
            mapView.getOverlays().addAll(1,trasa.bulid_markers_Tracking());
            trasa.add_last_Target();
            mapView.invalidate();
        }
        else
            Toast.makeText(kontekst, "Twoje położenie jest nie znanie", Toast.LENGTH_SHORT).show();


    }

    public void aktualizuj_tracking(GeoPoint newlocation) {

        now_tracking=true;
        location=newlocation;
        location.setAltitude(0);
        if(trasa!=null) {
            synchronized (trasa) {
                remove_polyline(trasa.polyline);
                if(trasa.roads.size()>0)
                aktualization_trasa(newlocation);
                if(this.trasa.markers_instruction.size()>0)
                 mapView.getOverlays().removeAll(this.trasa.markers_instruction);
                    this.trasa.remove_Markers();
                    mapView.getOverlays().add(1, this.trasa.polyline());
                    mapView.getOverlays().removeAll(trasa.markers_target);
                    mapView.getOverlays().addAll(trasa.markers());
                    mapView.getOverlays().addAll(1, this.trasa.bulid_markers_Tracking());
                if(!this.trasa.is_trasa())
                {
                    this.trasa =null;
                }
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
            typtrasa.polyline=null;
            typtrasa.roads.clear();
            if(typtrasa instanceof trasa_building)
            mapView.getOverlays().removeAll(((trasa_building) typtrasa).polylines);
            mapView.getOverlays().removeAll(trasa.getMarkers_instruction());
            trasa.remove_Markers();
            trasa.removeTargets();
            trasa.roads.clone();
            trasa = null;
            traking.Remove_Tracking();
            addresses=new ArrayList<Address>();
        }

        mapView.invalidate();
    }

    protected void aktualization_trasa(GeoPoint newlocation)
    {
        if(trasa.roads.size()>0 && trasa.roads.get(0).mRouteHigh.size()>0) {
            if (!trasa.is_end_tracking(newlocation)) {
                if (!trasa.is_route(newlocation)) {
                    dialog_loader.show();

                    traking.Aktulaizuj_location(0, newlocation, trasa.roads.get(0),this);
                    dialog_loader.dismiss();
                } else
                    trasa.ActualizacjaLotalizacji(newlocation);
            } else
                end_tracking(0);
        }

    }
    void end_tracking(int index)
    {
        addresses.remove(index);
        trasa.remove_tracking(index);
        mapView.getOverlays().removeAll(trasa.markers_instruction);
        trasa.remove_Markers();
        trasa.removeTarget(index);
        mapView.invalidate();
        traking.Remove_Tracking(index);
        trasa.bulid_markers_Tracking();
        Toast.makeText(kontekst, "Dodarłeś do celu", Toast.LENGTH_SHORT).show();
    }

    protected void  remove_polyline(Polyline polyline)
    {
        if(polyline!=null)
            mapView.getOverlays().remove(polyline);
    }

    @Override
    public boolean warunek(android.location.Location location) {
            if (!first_location) {
                this.location = new GeoPoint(location);
                this.location.setAltitude(0);
                first_location = true;
                time_last_actualization = (new Date()).getTime();
            }
            if (run && trasa != null && !now_tracking && (new Date()).getTime() - time_last_actualization > 1000 && trasa.roads.get(0).mRouteHigh.size()>0) {
                time_last_actualization = (new Date()).getTime();
                if(wait[0]) {
                    wait[0]=false;
                    return true;
                }
                    return trasa.odległość(new GeoPoint(location), this.location) > delta;
            } else
                return false;
    }

    @Override
    public void Akcja(android.location.Location location) {

        aktualizuj_tracking(new GeoPoint(location.getLatitude(),location.getLongitude(),location.getAltitude()));
        if(voice && trasa!=null)
       {
            text_convert_voice.Speech(trasa.now_instruction(new GeoPoint(location)));
        }
        screean_tracking.update_descryption(trasa);
    }

    @Override
    public void Akcje_is_false(Location location) {

    }


}
