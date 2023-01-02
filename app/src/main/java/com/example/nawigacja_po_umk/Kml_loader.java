package com.example.nawigacja_po_umk;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import org.osmdroid.bonuspack.kml.KmlDocument;
import org.osmdroid.bonuspack.kml.KmlFeature;
import org.osmdroid.util.BoundingBox;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.FolderOverlay;

public class Kml_loader extends AsyncTask<Void, Void, Void> {
    ProgressDialog progressDialog;
    KmlDocument kmlDocument;

    private Context context;
    private MapView mapView;
    int floor_level;
    public znacznik_Pozycji znacznik;

    Kml_loader(Context context, MapView mapView,int floor_level )
    {
        this.progressDialog = new ProgressDialog(context);
        this.context=context;
        this.mapView=mapView;
        this.floor_level=floor_level;
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog.setMessage("Loading Project...");
        progressDialog.show();
    }

    @Override
    protected Void doInBackground(Void... voids) {

        tworzenieKmlDocument();
        FolderOverlay kmlOverlay = (FolderOverlay) kmlDocument.mKmlRoot.buildOverlay(mapView, null, null,kmlDocument);
        kmlOverlay.setName("Floor"+floor_level);
        mapView.getOverlays().add(kmlOverlay);
        ///dodawanieznacznika
        dodawanie_znacznika_lokalizacji();
        mapView.setBackgroundColor(2000);
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        progressDialog.dismiss();
        mapView.invalidate();
        BoundingBox bb = kmlDocument.mKmlRoot.getBoundingBox();
        mapView.zoomToBoundingBox(bb, true);
        super.onPostExecute(aVoid);
    }

    private void tworzenieKmlDocument() {
        kmlDocument = new KmlDocument();
        Map_Overpass map_overpass = new Map_Overpass();
        String tag = "level=" + floor_level;
        BoundingBox boxA = new BoundingBox(53.01784, 18.60515, 53.01673, 18.60197);
        String url = map_overpass.urlForTagSearchKml(tag, boxA,10000,1000);
        ///Dodawanie obiektów z JSON do kmlDocument i tworzenie warstwy piętra
        map_overpass.addInKmlFolder(kmlDocument.mKmlRoot,url);
    }
    public KmlFeature[] print_item_KML()
    {

        KmlFeature[] mItem= kmlDocument.mKmlRoot.mItems.toArray(new KmlFeature[0]);
        return mItem;
    }

    void dodawanie_znacznika_lokalizacji()
    {
        znacznik= new znacznik_Pozycji(mapView,context,floor_level);
        nasłuchiwanie_znacznika_pozycji nasłuchiwanie = new nasłuchiwanie_znacznika_pozycji(context,floor_level);
        znacznik.setOnMarkerDragListener(nasłuchiwanie);
        mapView.getOverlays().add(znacznik);

    }

}