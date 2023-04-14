package com.example.nawigacja_po_umk;


import android.content.Context;

import android.location.Address;

import com.example.nawigacja_po_umk.ekranMarker.ekran_marker;


import org.osmdroid.bonuspack.kml.KmlFeature;

import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;


public class Add_marker {

    public static Marker Add_marker(KmlFeature item, MapView mapView,String opis)
    {
        Marker marker =new Marker(mapView);
        marker.setPosition(item.getBoundingBox().getCenter());
        marker.setTitle(opis);
        return marker;
    }
    public static Marker Add_marker(Address address,Context kontext, MapView mapView)
    {
        Marker marker =new Marker(mapView);
        marker.setPosition(new GeoPoint(address.getLatitude(),address.getLongitude()));
        marker.setRelatedObject(address);
        marker.setInfoWindow(new ekran_marker(mapView,kontext));
        return marker;
    }
    }


