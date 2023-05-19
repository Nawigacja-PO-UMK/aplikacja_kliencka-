package com.example.nawigacja_po_umk;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import com.Tracking.activity_Tracking;
import com.example.nawigacja_po_umk.Activity.in_building;
import com.example.nawigacja_po_umk.ekran_Tracking.screean_Tracking;
import com.loader_Map_Building.Mapa_budynku;
import com.lokalizator.Akcje_na_lokacizacji;

import org.osmdroid.util.BoundingBox;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;

public class Loader_map implements Akcje_na_lokacizacji {

    Mapa_budynku mapa_budynku;
   private com.lokalizator.uniwersal_location location;
    private BoundingBox box;
    private MapView mapView;
    private  Context kontekst;
    private screean_Tracking screean_tracking;
    in_building in_door;
    private  activity_Tracking oldtracking;
    Loader_map(BoundingBox box, MapView mapView, Context kontekst, com.lokalizator.uniwersal_location location, screean_Tracking screean_tracking, activity_Tracking oldtracking)
   {
       this.box=box;
       this.kontekst=kontekst;
       this.mapView=mapView;
       mapa_budynku=null;
       this.location=location;
       this.screean_tracking=screean_tracking;
       this.oldtracking=oldtracking;
       in_door= new in_building();
   }


    @Override
    public boolean warunek(Location location) {
        return box.getLatNorth()>location.getLatitude() && box.getLatSouth()<location.getLatitude()
                && box.getLonEast()>location.getLongitude() && box.getLonWest()<location.getLongitude();
    }

    @Override
    public void Akcja(Location location)
    {
        this.oldtracking.setRun(false);
           if (mapa_budynku == null) {
               mapa_budynku = new Mapa_budynku(kontekst, box,mapView,screean_tracking);
               mapa_budynku.wczytywanie_mapy(0);
               Bundle bundle=new Bundle();
               bundle.putSerializable("mapa",mapa_budynku);
               in_door.setArguments(bundle);
               ((MainActivity)kontekst).replace_fragment(in_door,R.id.in_door);
           }
         else {
               if (mapa_budynku.tracking_buliding.warunek(location))
                   mapa_budynku.tracking_buliding.Akcja(location);

           }
           /*
           if(this.location.isLocation_builging())
            {
             if (mapa_budynku.get_level_trasa() == )
              mapa_budynku.wczytaj_nowa_mape((int) location.getAltitude());
            }
           else
           {
               this.location.setLocation_builging(true);
           }
           /
 */

    }

    @Override
    public void Akcje_is_false(Location location) {
        mapa_budynku=null;
        this.oldtracking.setRun(true);
      //  ((MainActivity)kontekst).replace_fragment(new Fragment());
        this.location.setLocation_builging(false);
    }
}
