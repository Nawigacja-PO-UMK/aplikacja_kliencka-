package com.example.nawigacja_po_umk;

import android.content.Context;
import android.location.Location;

import com.example.nawigacja_po_umk.ekran_Tracking.screean_Tracking;
import com.loader_Map_Building.Mapa_budynku;
import com.lokalizator.Akcje_na_lokacizacji;

import org.osmdroid.util.BoundingBox;
import org.osmdroid.views.MapView;

public class Loader_map implements Akcje_na_lokacizacji {

    Mapa_budynku mapa_budynku;
   private com.lokalizator.uniwersal_location location;
    private BoundingBox box;
    private MapView mapView;
    private  Context kontekst;
    private screean_Tracking screean_tracking;
    Loader_map(BoundingBox box, MapView mapView, Context kontekst, com.lokalizator.uniwersal_location location, screean_Tracking screean_tracking)
   {
       this.box=box;
       this.kontekst=kontekst;
       this.mapView=mapView;
       mapa_budynku=null;
       this.location=location;
       this.screean_tracking=screean_tracking;
   }


    @Override
    public boolean warunek(Location location) {
        return box.getLatNorth()>location.getLatitude() && box.getLatSouth()<location.getLatitude()
                && box.getLonEast()>location.getLongitude() && box.getLonWest()<location.getLongitude();
    }

    @Override
    public void Akcja(Location location)
    {
           if (mapa_budynku == null) {
               mapa_budynku = new Mapa_budynku(kontekst, mapView,screean_tracking);
               mapa_budynku.wczytywanie_mapy(0);
           }

           if(this.location.isLocation_builging())
           {
               if (mapa_budynku.level() != (int) location.getAltitude())
                   mapa_budynku.wczytaj_nowa_mape((int) location.getAltitude());
           }
           else
           {
               this.location.setLocation_builging(true);
           }

    }

    @Override
    public void Akcje_is_false(Location location) {
        mapa_budynku=null;
        this.location.setLocation_builging(false);
    }
}
