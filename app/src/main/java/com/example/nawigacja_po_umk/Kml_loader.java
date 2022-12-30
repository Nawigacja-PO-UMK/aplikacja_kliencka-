package com.example.nawigacja_po_umk;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import org.osmdroid.bonuspack.kml.KmlDocument;
import org.osmdroid.util.BoundingBox;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.FolderOverlay;

public class Kml_loader extends AsyncTask<Void, Void, Void> {
    @SuppressLint("StaticFieldLeak")
    private final MapView mapView;

    private KmlDocument kml_Document;
    private FolderOverlay kml_Overlay;
    private final int floor_level;

    Kml_loader( MapView mapView,int floor_level )
    {
        this.mapView=mapView;
        this.floor_level=floor_level;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        createKml();
        kml_Overlay = (FolderOverlay) kml_Document.mKmlRoot.buildOverlay(mapView, null, null, kml_Document);
        return null;
    }

    private void createKml() {
        kml_Document = new KmlDocument();
        Map_Overpass map_overpass = new Map_Overpass();
        String tag = "level=" + floor_level;
        BoundingBox boxA = new BoundingBox(53.01784, 18.60515, 53.01673, 18.60197);
        String url = map_overpass.urlForTagSearchKml(tag, boxA,10000,1000);
        map_overpass.addInKmlFolder(kml_Document.mKmlRoot,url);
    }

    KmlDocument get_Kml_Document() { return kml_Document; }

    FolderOverlay get_Kml_Overlay() { return kml_Overlay;}

    int get_FloorLevel() { return floor_level; }

}