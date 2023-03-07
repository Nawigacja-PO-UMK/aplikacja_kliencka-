package com.Tracking;

import android.content.Context;
import android.location.Address;
import android.location.Location;
import android.widget.Toast;

import com.Text_convert_voice;
import com.Tracking.DowlandTracking.Tracking;
import com.Tracking.trasy.trasa;
import com.example.nawigacja_po_umk.Add_marker;
import com.example.nawigacja_po_umk.ekran_Tracking.screean_Tracking;
import com.lokalizator.Akcje_na_lokacizacji;
import com.search_location.search_location;

import org.osmdroid.bonuspack.routing.Road;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Polyline;

public class activity_Tracking implements Akcje_na_lokacizacji {

    public com.Tracking.trasy.trasa trasa;
    public MapView mapView;
    protected Context kontekst;
    protected Tracking traking;
    protected GeoPoint location;
    protected  double delta=1;
    protected boolean now_tracking;
    private trasa typtrasa;
    private Text_convert_voice text_convert_voice;
    boolean voice;
    private screean_Tracking screean_tracking;
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
        if(this.location!=null)
        {

            if (trasa != null && trasa.is_trasa())
            {
                remove_polyline(trasa.polyline);
                trasa.add_trasa(traking.add_tracking(0, newposition),search_location.search_name_Adress(location),
                        Add_marker.Add_marker(location, mapView));
            }
            else {
                Road road=traking.tracking(0,this.location,newposition,0);
                typtrasa.new_instancion(road,search_location.search_name_Adress(location),kontekst,mapView
                ,Add_marker.Add_marker(location, mapView));
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
        if(trasa!=null) {

            synchronized (trasa) {
                remove_polyline(trasa.polyline);
                aktualization_trasa(newlocation);
                if(trasa.markers_instruction.size()>0)
                 mapView.getOverlays().removeAll(trasa.markers_instruction);
                trasa.remove_Markers();
                mapView.getOverlays().add(0, trasa.polyline());
                mapView.getOverlays().addAll(1,trasa.bulid_markers_Tracking());
                if(!trasa.is_trasa())
                {
                    trasa=null;
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
            mapView.getOverlays().removeAll(trasa.getMarkers_instruction());
            trasa.remove_Markers();
            trasa.removeTargets();
            trasa = null;
            traking.Remove_Tracking();
        }

        mapView.invalidate();
    }

    protected void aktualization_trasa(GeoPoint newlocation)
    {
            if(!trasa.is_end_tracking(newlocation)) {
                if (!trasa.is_route(newlocation))
                        trasa.ActualizacjaLotalizacji(traking.Aktulaizuj_location(0, newlocation));
                    else
                        trasa.ActualizacjaLotalizacji(newlocation);
            }
            else
                end_tracking(0);

    }
    void end_tracking(int index)
    {

        trasa.remove_tracking(index);
        mapView.getOverlays().removeAll(trasa.markers_instruction);
        trasa.remove_Markers();
        trasa.removeTarget(index);
        mapView.invalidate();
        now_tracking=true;
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

        if(trasa!=null  && !now_tracking)
        {
            return trasa.odległość(new GeoPoint(location),this.location)> delta;

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