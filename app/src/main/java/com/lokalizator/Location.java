package com.lokalizator;

import android.content.Context;

import org.osmdroid.api.IMapController;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.mylocation.IMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

public class Location  {
    private Context kontekst;
    private MapView mapView;
    private MyLocationNewOverlay myLocationNewOverlay;
    private GeoPoint last_kow_location;
    private IMapController mapController;
    public boolean seting_center;
    public android.location.Location Location;
    private uniwersal_location  LocationSource;
    public Location(Context kontekst, MapView mapView, Akcje_na_lokacizacji[] Akcje,uniwersal_location  locationSource,GeoPoint new_kow_location)
    {
        this.kontekst=kontekst;
        this.mapView=mapView;
        this.seting_center=false;
        mapController = mapView.getController();
        if(new_kow_location!=null) {
            mapController.setZoom(19);
            this.last_kow_location = new_kow_location;
        }
            else {
            mapController.setZoom(10);
            this.last_kow_location = new GeoPoint(53.017270, 18.60300);
        }
        mapController.setCenter(last_kow_location);
        this.LocationSource=locationSource;
        this.myLocationNewOverlay = new MyLocationNewOverlay(this.LocationSource,mapView) {
                @Override
                public void onLocationChanged(android.location.Location location, IMyLocationProvider source) {
                super.onLocationChanged(location, source);

                last_kow_location=new GeoPoint(location.getLatitude(),location.getLongitude());

                if(!seting_center) {
                        mapController.animateTo(last_kow_location);
                        mapController.setCenter(last_kow_location);
                        mapController.setZoom(18);
                        seting_center=true;
                    }
                Location=location;
                for(int i=0;i<Akcje.length;i++) {
                    if (Akcje[i].warunek(location))
                        Akcje[i].Akcja(location);
                    else
                        Akcje[i].Akcje_is_false(location);
                }
            }
        };
        this.myLocationNewOverlay.enableMyLocation();
        mapView.getOverlays().add(this.myLocationNewOverlay);
    }


    public uniwersal_location  getLocationSource() {
        return LocationSource;
    }

    public MyLocationNewOverlay getOverlay()
    {
        return myLocationNewOverlay;
    }

   public GeoPoint getMyLocation() {

        return last_kow_location;

    }

}
