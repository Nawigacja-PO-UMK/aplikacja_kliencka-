package com.example.nawigacja_po_umk;

import android.content.Context;
import android.location.Location;

import com.loader_Map_Building.Mapa_budynku;
import com.lokalizator.Akcje_na_lokacizacji;

import org.osmdroid.util.BoundingBox;
import org.osmdroid.views.MapView;

public class Loader_map implements Akcje_na_lokacizacji {

    Mapa_budynku mapa_budynku;
   private BoundingBox box;
    private MapView mapView;
    private  Context kontekst;
   Loader_map(BoundingBox box, MapView mapView, Context kontekst)
   {
       this.box=box;
       this.kontekst=kontekst;
       this.mapView=mapView;
   }


    @Override
    public boolean warunek(Location location) {
        return box.getLatNorth()>location.getLatitude() && box.getLatSouth()<location.getLatitude()
                && box.getLonEast()>location.getLongitude() && box.getLonWest()<location.getLongitude();
    }

    @Override
    public void Akcja(Location location) {
       if(mapa_budynku==null)
      mapa_budynku= new Mapa_budynku(kontekst,mapView);
       else
           if(mapa_budynku.level()!=(int)location.getAltitude())
                mapa_budynku.wczytaj_nowa_mape(mapa_budynku.level());
    }

    @Override
    public void Akcje_is_false(Location location) {
        mapa_budynku=null;
    }
}
