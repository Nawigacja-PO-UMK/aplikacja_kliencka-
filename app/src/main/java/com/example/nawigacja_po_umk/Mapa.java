package com.example.nawigacja_po_umk;

import static com.example.nawigacja_po_umk.MainActivity.file_save;
import static java.lang.Integer.parseInt;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.location.Address;
import android.os.Bundle;
import android.widget.Toast;

import com.Tracking.DowlandTracking.Building_Tracking;
import com.Tracking.DowlandTracking.OSRM_Tracking;
import com.Tracking.activity_Tracking;
import com.Tracking.trasy.trasa_outside;
import com.example.nawigacja_po_umk.Activity.search;
import com.example.nawigacja_po_umk.ekran_Tracking.screean_Tracking;
import com.loader_Map_Building.Mapa_budynku;
import com.lokalizator.Akcje_na_lokacizacji;
import com.lokalizator.Location;
import com.lokalizator.uniwersal_location;
import com.search_location.search_location;

import org.osmdroid.api.IMapController;
import org.osmdroid.bonuspack.routing.Road;
import org.osmdroid.events.MapEventsReceiver;
import org.osmdroid.tileprovider.tilesource.ITileSource;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.tileprovider.tilesource.XYTileSource;
import org.osmdroid.util.BoundingBox;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.MapEventsOverlay;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.gestures.RotationGestureOverlay;

import java.io.Serializable;
import java.util.List;

public class Mapa implements Serializable , MapEventsReceiver {

    public MapView mapView;
    public IMapController mapController;
    Context kontekst;
    MapEventsOverlay mapEventsOverlay;
    public Location location;
    private Mapa_budynku mapa_budynku;
    private Loader_map loader_map;
    public activity_Tracking tracking;
    public Marker marker;
    public static ITileSource tileSource=TileSource_Mapbox();
    public  uniwersal_location  sourceLocation;
    Road road= new Road();

    public Mapa(Context kontekst, MapView mapView, screean_Tracking screean_tracking)
    {
        this.kontekst = kontekst;
        this.mapView = mapView;
        mapView.setTileSource(tileSource);
        mapController = mapView.getController();
        mapController.setZoom(15);
        mapView.setMultiTouchControls(true);
        RotationGestureOverlay mRotationGestureOverlay = new RotationGestureOverlay(kontekst, mapView);
         mRotationGestureOverlay.setEnabled(true);
        mapView.setMultiTouchControls(true);
        mapView.getOverlays().add(mRotationGestureOverlay);
        mapEventsOverlay = new MapEventsOverlay(kontekst, this);
        mapView.getOverlays().add(0, mapEventsOverlay);
        sourceLocation=new uniwersal_location(kontekst);

        tracking=new activity_Tracking(mapView,kontekst,new trasa_outside(), new OSRM_Tracking(kontekst),screean_tracking);
        this.loader_map= new Loader_map(new BoundingBox(53.01784, 18.60515, 53.01673, 18.60197),
                mapView,kontekst,sourceLocation,screean_tracking,tracking);
        location=new Location(kontekst,mapView, new Akcje_na_lokacizacji[]{loader_map, tracking},sourceLocation,null);
       // Building_Tracking building_tracking=new Building_Tracking(kontekst);

       /// GeoPoint point=new GeoPoint(18.60515,53.01784,-1);
       /// GeoPoint point1=new GeoPoint(18.60197,53.01673,1);

       //building_tracking.Dowland_Tracking(point,point1,road);
    }

    public Mapa(Context kontekst, MapView mapView)
    {
        this.kontekst = kontekst;
        this.mapView = mapView;
        mapView.setTileSource(tileSource);
        mapController = mapView.getController();
        mapController.setZoom(15);
        mapView.setMultiTouchControls(true);
        RotationGestureOverlay mRotationGestureOverlay = new RotationGestureOverlay(kontekst, mapView);
        mRotationGestureOverlay.setEnabled(true);
        mapView.setMultiTouchControls(true);
        mapView.getOverlays().add(mRotationGestureOverlay);
        mapEventsOverlay = new MapEventsOverlay(kontekst, this);
        mapView.getOverlays().add(0, mapEventsOverlay);
    }

    public static ITileSource TileSource_Mapbox() {
         return new XYTileSource( "3d", 1, 20, 512, ".png?key=ttyaQAsQurB5RH7Nlny1",
                new String[] {
                        "https://api.maptiler.com/maps/streets-v2/" },"© MapTiler © współtwórcy OpenStreetMap");
    }

    public void setTileSource(ITileSource tileSource) {
        Mapa.tileSource = tileSource;
        mapView.setTileSource(tileSource);
    }

    public activity_Tracking getTracking() {
        return tracking;
    }

    public void  add_tracking(Address address)  {
        if(address!=null)
            tracking.add_tracking(address);
    }
    public Mapa_budynku getMapa_budynku() {
        return loader_map.mapa_budynku;
    }
    public void remove_tracking()
    {
        tracking.remove_tracking();
    }


    @Override
    public boolean singleTapConfirmedHelper(GeoPoint p) {

       return true;
    }

    @Override
    public boolean longPressHelper(GeoPoint p) {
        List<Address> addresses = search_location.search(p, 1);
        Toast.makeText(kontekst, String.valueOf(road.mRouteHigh.size()), Toast.LENGTH_SHORT).show();
        if (marker != null)
            mapView.getOverlays().remove(marker);
        if (marker == null || marker.getPosition().distanceToAsDouble(p) > 20) {
            marker = Add_marker.Add_marker(addresses.get(0), kontekst, mapView);
            mapView.getOverlays().add(marker);
        } else
            marker = null;
        return true;
    }
       @SuppressLint("SuspiciousIndentation")
       public void   newInstance(MapView mapView, screean_Tracking screean_tracking) {
        this.mapView=mapView;
        mapView.setTileSource(tileSource);
           mapView.setMultiTouchControls(true);
           RotationGestureOverlay mRotationGestureOverlay = new RotationGestureOverlay(kontekst, mapView);
           mRotationGestureOverlay.setEnabled(true);
           mapView.setMultiTouchControls(true);
           mapView.getOverlays().add(mRotationGestureOverlay);
           mapEventsOverlay = new MapEventsOverlay(kontekst, this);
           mapView.getOverlays().add(0, mapEventsOverlay);
           sourceLocation=new uniwersal_location(kontekst);
           this.loader_map= new Loader_map(new BoundingBox(53.01784, 18.60515, 53.01673, 18.60197),
                   mapView,kontekst,sourceLocation,screean_tracking,tracking);
           tracking.newinstancjon(mapView,screean_tracking);
          location=new Location(kontekst,mapView, new Akcje_na_lokacizacji[]{loader_map, tracking},sourceLocation,this.location.getMyLocation());
          if(tracking.trasa!=null)
          search.tracking_Activity(kontekst,this,screean_tracking);
    }

}
