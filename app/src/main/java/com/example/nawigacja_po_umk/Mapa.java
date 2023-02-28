package com.example.nawigacja_po_umk;

import static java.lang.Integer.parseInt;

import android.content.Context;
import android.location.Address;
import android.widget.Toast;

import com.Tracking.activity_Tracking_outside;
import com.loader_Map_Building.Mapa_budynku;
import com.lokalizator.Akcje_na_lokacizacji;
import com.lokalizator.Location;
import com.search_location.search_location;

import org.osmdroid.api.IMapController;
import org.osmdroid.bonuspack.location.POI;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.BoundingBox;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;

import java.io.IOException;
import java.util.List;

public class Mapa {

    private MapView mapView;
    private IMapController mapController;
    Context kontekst;

    private Location location;
    private Mapa_budynku mapa_budynku;
    private Loader_map loader_map;
    private activity_Tracking_outside tracking;


    Mapa(Context kontekst,MapView mapView)
    {
        this.kontekst = kontekst;
        this.mapView = mapView;
        mapView.setTileSource(TileSourceFactory.MAPNIK);
        IMapController mapController = mapView.getController();
        mapController.setZoom(15);
        mapView.setMultiTouchControls(true);
        this.loader_map= new Loader_map(new BoundingBox(53.01784, 18.60515, 53.01673, 18.60197),mapView,kontekst);
        tracking=new activity_Tracking_outside(mapView,kontekst,false);
        location=new Location(kontekst,mapView, new Akcje_na_lokacizacji[]{loader_map, tracking});
    }

    public activity_Tracking_outside getTracking() {
        return tracking;
    }

    public void  add_tracking(String nazwa)  {

        List<Address> address = search_location.search(nazwa,10);
        if(address.size()>0)
            tracking.add_tracking(address.get(0));
        else
            Toast.makeText(kontekst, "nie znaleziono", Toast.LENGTH_SHORT).show();
    }
    public Mapa_budynku getMapa_budynku() {
        return loader_map.mapa_budynku;
    }
    public void remove_tracking()
    {
        tracking.remove_tracking();
    }
}
