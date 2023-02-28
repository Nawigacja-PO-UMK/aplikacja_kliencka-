package com.loader_Map_Building;

import static java.lang.Integer.parseInt;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.widget.Toast;

import com.Tracking.activity_Tracking_in_building;
import com.search_location.Item;
import com.Tracking.Traking;
import com.Tracking.trasa;
import com.search_location.search_location;

import org.osmdroid.api.IMapController;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

import java.util.ArrayList;

public class Mapa_budynku  {

    private MapView mapView;
    private IMapController mapController;
    Context kontekst;
    private int levelmax;
    private int levelmin;
    private Traking traking;
    private Kml_loader loadKml;
    private ArrayList<Marker>[] markers;
    private activity_Tracking_in_building tracking_buliding;

    public Mapa_budynku(Context kontekst, MapView mapView) {
        this.kontekst = kontekst;
        this.mapView = mapView;
        mapView.setTileSource(TileSourceFactory.MAPNIK);
        levelmax=2;
        levelmin=-1;
        this.tracking_buliding=new activity_Tracking_in_building(mapView,kontekst,true,levelmax,levelmin);
        //wczytywanie_mapy(0);
    }
    public trasa get_trasa()
    {
        return tracking_buliding.get_trasa();
    }
    public int level() {
        return tracking_buliding.actual_level();
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
                loadKml = (Kml_loader) new Kml_loader(kontekst, mapView, level, levelmin, levelmax).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            }

    }
    @SuppressLint("SuspiciousIndentation")
    public void wczytaj_nowa_mape(int level)
    {
        mapView.getOverlays().clear();
        tracking_buliding.add_tracking_map(level-levelmin);
        wczytywanie_mapy(level);
    }

    public void add_tracking(String nameRoom)
    {
        Item item = search_location.search_in_building(nameRoom, loadKml.print_item_KML());
        if(item!=null) {
                tracking_buliding.add_tracking(item);
        }
        else
            Toast.makeText(kontekst, "nie właściwa nazwa pomieszczenia", Toast.LENGTH_SHORT).show();
    }
    public void remove_tracking()
    {
        tracking_buliding.remove_tracking();
    }

}
