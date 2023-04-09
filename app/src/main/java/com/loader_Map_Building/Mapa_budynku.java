package com.loader_Map_Building;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Address;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.widget.Toast;

import com.Tracking.DowlandTracking.Building_Tracking;
import com.Tracking.DowlandTracking.OSRM_Tracking;
import com.Tracking.activity_Tracking;
import com.Tracking.trasy.trasa;
import com.Tracking.trasy.trasa_building;
import com.example.nawigacja_po_umk.ekran_Tracking.screean_Tracking;
import com.search_location.Item;
import com.Tracking.DowlandTracking.Tracking;
import com.search_location.search_location;

import org.osmdroid.api.IMapController;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.BoundingBox;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Locale;

public class Mapa_budynku implements Serializable {

    private final MapView mapView;
    private IMapController mapController;
    Context kontekst;
    private int levelmax;
    private int levelmin;
    private int level;
    private Tracking traking;
    private Kml_loader loadKml;
    private ArrayList<Marker>[] markers;
    public activity_Tracking tracking_buliding;
    BoundingBox box;

    public Mapa_budynku(Context kontekst, BoundingBox box,MapView mapView, screean_Tracking screean_tracking) {
        this.kontekst = kontekst;
        this.mapView = mapView;
        levelmax=2;
        levelmin=-1;
        level=0;
        this.box=box;
        this.tracking_buliding=new activity_Tracking(mapView,kontekst,new trasa_building(),
                new OSRM_Tracking(kontekst,0),screean_tracking);
    }
    public trasa get_trasa()
    {
        return tracking_buliding.getTrasa();
    }


    public int level() {
                return level;
    }
    public int get_level_trasa()
    {
        if(null!=tracking_buliding.getTrasa())
            return ((trasa_building) tracking_buliding.getTrasa()).level();
        else
            return Integer.MAX_VALUE;
    }

    public int levelmax()
    {
        return levelmax;
    }

    public int Levelmin()
    {
        return levelmin;
    }

    public void wczytywanie_mapy(int level) {

        ConnectivityManager łączę =(ConnectivityManager) kontekst.getSystemService(Context.CONNECTIVITY_SERVICE);
        if(!(łączę.getActiveNetworkInfo()!=null &&  łączę.getActiveNetworkInfo().isConnected()))
            Toast.makeText(kontekst,"włacz internet",Toast.LENGTH_LONG).show();
            else {
            if (loadKml != null)
                loadKml.show_map(level);
            else
                loadKml = (Kml_loader) new Kml_loader(kontekst,box, mapView, level, levelmin, levelmax).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            }
    }
    @SuppressLint("SuspiciousIndentation")
    public void wczytaj_nowa_mape(int level)
    {
        if(level!=this.level) {
            mapView.getOverlays().remove(loadKml.folderOverlays[this.level-levelmin]);
            trasa_building trasa = (trasa_building) tracking_buliding.getTrasa();
            //mapView.getOverlays().add(trasa.polyline(level));
            wczytywanie_mapy(level);
            this.level=level;
        }
    }

    public void add_tracking(String nameRoom)
    {
        Item item = search_location.search_in_building(nameRoom, loadKml.print_item_KML());
        if(item!=null) {
            Locale locale=new Locale("PL");
            Address address= new Address(locale);
            address.setFeatureName(item.item.mName);
            address.setLatitude(item.item.getBoundingBox().getCenterLatitude());
            address.setLongitude(item.item.getBoundingBox().getCenterLongitude());
                tracking_buliding.add_tracking(address);
        }
        else
            Toast.makeText(kontekst, "nie właściwa nazwa pomieszczenia", Toast.LENGTH_SHORT).show();
    }
    public void remove_tracking()
    {
        tracking_buliding.remove_tracking();
    }
}
