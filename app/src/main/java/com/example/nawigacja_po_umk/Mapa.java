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
import org.osmdroid.views.overlay.Marker;
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
    private trasa[] trasa;
    public Znacznik_Pozycji znacznik;
    public nasłuchiwanie_znacznika_pozycji dragondrop;
    private Kml_loader loadKml;
    private GeoPoint point2;
    private ArrayList<Marker>[] markers;

    Mapa(Context kontekst, MapView mapView) {
        this.kontekst = kontekst;
        this.mapView = mapView;
        mapView.setTileSource(TileSourceFactory.MAPNIK);
        this.traking=new Traking(kontekst,level);
        point2 = new GeoPoint(53.01699, 18.60282);
        IMapController mapController = mapView.getController();
        mapController.setCenter(point2);
        mapController.setZoom(15);
        mapView.setMultiTouchControls(true);
        levelmax=2;
        levelmin=-1;
        level = 0;
        deklarowanie_markers();
        trasa=new trasa[levelmax-levelmin+1];
        wczytywanie_mapy(level);
        dodawanie_znacznika_lokalizacji();
    }
    public trasa[] get_trasa()
    {
        return trasa;
    }
    private void  deklarowanie_markers()
    {
        this.markers=new ArrayList[levelmax-levelmin+1];
        for (int i=0;i<markers.length;i++)
        {
            markers[i]=new ArrayList<Marker>();
        }
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
        updata_znaczik();
        mapView.getOverlays().clear();
        if(markers[level-levelmin].size()>0)
            mapView.getOverlays().addAll(markers[level-levelmin]);
        if(trasa[level-levelmin]!=null )
        mapView.getOverlays().add(trasa[level-levelmin].polyline());
        mapView.getOverlays().add(znacznik);
        mapView.invalidate();
        wczytywanie_mapy(level);
    }
    private void updata_znaczik()
    {
        znacznik.updata_pozytion(level);
        dragondrop.update_level(level);
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
        Item item = Add_marker.seach_item(nameRoom, loadKml.print_item_KML());
        if(item!=null) {
           markers[item.level].add(Add_marker.Add_marker(item.item, mapView, "miejsce docelowe"));
            if (trasa[item.level] == null)
            {
                Road road=traking.tracking(level,znacznik.getPosition(), item.item.getBoundingBox().getCenter(),parseInt(item.item.mExtendedData.get("level")));
                trasa[item.level]=new trasa(road,item.item.mName);
            }
            else {
                remove_polyline(trasa[item.level].polyline);
                trasa[item.level].add_trasa(traking.tracking(parseInt(item.item.mExtendedData.get("level")), item.item.getBoundingBox().getCenter()), item.item.mName);
            }
            if(item.level==(level-levelmin)) {
                mapView.getOverlays().add(1,trasa[item.level].polyline());
                mapView.getOverlays().add(markers[item.level].get(markers[item.level].size()-1));
                mapView.invalidate();
            }
        }
        else
            Toast.makeText(kontekst, "nie właściwa nazwa pomieszczenia", Toast.LENGTH_SHORT).show();
    }

    private void dodawanie_znacznika_lokalizacji() {
        znacznik = new Znacznik_Pozycji(mapView, kontekst, level, point2);
        dragondrop = new nasłuchiwanie_znacznika_pozycji(kontekst, level);
        znacznik.setOnMarkerDragListener(dragondrop);
        mapView.getOverlays().add(znacznik);
        mapView.invalidate();
    }
    public void remove_tracking()
    {
        if(trasa!=null) {
            for(int i=0;i<trasa.length;i++)
                remove_polyline(trasa[i].polyline);
            trasa = null;
        }
        for(int j=0;j<markers.length;j++)
            for(int i=0;i<markers[j].size();i++)
                markers[j].get(i).remove(mapView);

        mapView.invalidate();
    }
    private void  remove_polyline(Polyline polyline)
    {
        if(polyline!=null)
            mapView.getOverlays().remove(polyline);
    }
}
