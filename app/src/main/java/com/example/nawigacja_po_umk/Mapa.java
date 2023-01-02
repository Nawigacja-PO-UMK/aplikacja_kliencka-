package com.example.nawigacja_po_umk;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.widget.Toast;

import org.osmdroid.api.IMapController;
import org.osmdroid.bonuspack.kml.KmlDocument;
import org.osmdroid.bonuspack.kml.KmlFeature;
import org.osmdroid.bonuspack.routing.OSRMRoadManager;
import org.osmdroid.bonuspack.routing.Road;
import org.osmdroid.bonuspack.routing.RoadManager;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.DefaultOverlayManager;
import org.osmdroid.views.overlay.FolderOverlay;
import org.osmdroid.views.overlay.Overlay;
import org.osmdroid.views.overlay.OverlayItem;
import org.osmdroid.views.overlay.OverlayManager;
import org.osmdroid.views.overlay.OverlayWithIW;
import org.osmdroid.views.overlay.Polyline;

import java.util.ArrayList;

public class Mapa {

    private MapView mapView;
    private IMapController mapController;
    Context kontekst;
    private int level;
    private int levelmax;
    private int levelmin;
    private  Traking traking;

     Kml_loader loadKml;

    Mapa(Context kontekst, MapView mapView) {
        this.kontekst = kontekst;
        this.mapView = mapView;
        mapView.setTileSource(TileSourceFactory.MAPNIK);
        GeoPoint point2 = new GeoPoint(53.01699, 18.60282);
        IMapController mapController = mapView.getController();
        mapController.setCenter(point2);
        mapController.setZoom(15);
        mapView.setMultiTouchControls(true);
        levelmax=2;
        levelmin=-1;
        level = 0;
        wczytywanie_mapy(level);
    }

    public int level() {
        return level;
    }

    public int levelmax()
    {
        return levelmax;
    }

    public int Levelmin()
    {
        return levelmin;
    }
    @SuppressLint("SuspiciousIndentation")
    private void wczytywanie_mapy(int level) {

        ConnectivityManager łączę =(ConnectivityManager) kontekst.getSystemService(Context.CONNECTIVITY_SERVICE);
        if(!(łączę.getActiveNetworkInfo()!=null &&  łączę.getActiveNetworkInfo().isConnected()))
            Toast.makeText(kontekst,"włacz internet",Toast.LENGTH_LONG).show();
            else
                if(loadKml!= null)
                loadKml.wczytywanie_mapy(level);
                else
        loadKml= (Kml_loader) new Kml_loader(kontekst,mapView,level,-1,2).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }
    public void wczytaj_nowa_mape(int level)
    {
        this.level=level;
        mapView.getOverlays().clear();
        mapView.invalidate();
        wczytywanie_mapy(level);
    }

    public wspułżedne odczytaj_wspułrzędne()
    {
        wspułżedne XY=new wspułżedne();
        GeoPoint punkt=loadKml.znacznik.getPosition();
        XY.X=punkt.getLatitude();
        XY.Y=punkt.getLongitude();
        XY.Z=level;
        return XY;
    }
    public void add_marker(String nameRoom)
    {

        KmlFeature item = Add_marker.seach_item(nameRoom, loadKml.print_item_KML());
        if(item!=null) {
            Add_marker.Add_marker(item, mapView, "miejsce docelowe");
            Polyline polyline;
            if (traking == null) {
                this.traking=new Traking(kontekst);
               polyline=traking.tracking(loadKml.znacznik.getPosition(), item.getBoundingBox().getCenter());
            }
            else
                polyline= traking.tracking(item.getBoundingBox().getCenter());


            mapView.getOverlays().add(1,polyline);
        }
        else
            Toast.makeText(kontekst, "nie właściwa nazwa pomieszczenia", Toast.LENGTH_SHORT).show();
    }



}
