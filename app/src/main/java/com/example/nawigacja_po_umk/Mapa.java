package com.example.nawigacja_po_umk;

import static java.lang.Integer.parseInt;

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
import org.osmdroid.util.BoundingBox;
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
    public trasa trasa;
    public Znacznik_Pozycji znacznik;
    private Kml_loader loadKml;
    private GeoPoint point2;
    Mapa(Context kontekst, MapView mapView) {
        this.kontekst = kontekst;
        this.mapView = mapView;
        mapView.setTileSource(TileSourceFactory.MAPNIK);
        point2 = new GeoPoint(53.01699, 18.60282);
        IMapController mapController = mapView.getController();
        mapController.setCenter(point2);
        mapController.setZoom(15);
        mapView.setMultiTouchControls(true);
        levelmax=2;
        levelmin=-1;
        level = 0;
        wczytywanie_mapy(level);
        dodawanie_znacznika_lokalizacji();
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
            else {
            if (loadKml != null)
                loadKml.show_map(level);
            else
                loadKml = (Kml_loader) new Kml_loader(kontekst, mapView, level, levelmin, levelmax).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            }

    }
    public void wczytaj_nowa_mape(int level)
    {
        this.level=level;
        mapView.getOverlays().clear();
        mapView.getOverlays().add(znacznik);
        mapView.invalidate();
        wczytywanie_mapy(level);


    }

    public wspułżedne odczytaj_wspułrzędne()
    {
        wspułżedne XY=new wspułżedne();
        GeoPoint punkt=znacznik.getPosition();
        XY.X=punkt.getLatitude();
        XY.Y=punkt.getLongitude();
        XY.Z=level;
        return XY;
    }
    public void add_tracking(String nameRoom)
    {
        KmlFeature item = Add_marker.seach_item(nameRoom, loadKml.print_item_KML());
        if(item!=null) {
            Add_marker.Add_marker(item, mapView, "miejsce docelowe");
            Polyline polyline;
            if (traking == null) {
                this.traking=new Traking(kontekst,level);
                Road road=traking.tracking(level,znacznik.getPosition(), item.getBoundingBox().getCenter(),parseInt(item.mExtendedData.get("level")));
                trasa=new trasa(road);
            }
            else
                trasa.add_trasa(traking.tracking(parseInt(item.mExtendedData.get("level")),item.getBoundingBox().getCenter()));
            int size= mapView.getOverlays().size();
            mapView.getOverlays().add(size-3, trasa.polyline());
        }
        else
            Toast.makeText(kontekst, "nie właściwa nazwa pomieszczenia", Toast.LENGTH_SHORT).show();
    }

    private void dodawanie_znacznika_lokalizacji() {
        znacznik = new Znacznik_Pozycji(mapView, kontekst, level, point2);
        nasłuchiwanie_znacznika_pozycji nasłuchiwanie = new nasłuchiwanie_znacznika_pozycji(kontekst, level);
        znacznik.setOnMarkerDragListener(nasłuchiwanie);
        mapView.getOverlays().add(znacznik);
        mapView.invalidate();
    }
}
