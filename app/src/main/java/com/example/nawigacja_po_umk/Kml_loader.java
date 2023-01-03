package com.example.nawigacja_po_umk;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;
import org.osmdroid.api.IGeoPoint;
import org.osmdroid.bonuspack.kml.KmlDocument;
import org.osmdroid.bonuspack.kml.KmlFeature;
import org.osmdroid.util.BoundingBox;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.FolderOverlay;

public class Kml_loader extends AsyncTask<Void, Void, Void> {
    ProgressDialog progressDialog;
    KmlDocument[] kmlDocument;

    private Context context;
    private MapView mapView;
    private FolderOverlay[] folderOverlays;
    int floor_level;
    int minlevel;
    int maxlevel;
    public Znacznik_Pozycji znacznik;

    Kml_loader(Context context, MapView mapView,int floor_level,int minlevel,int maxlevel )
    {
        this.progressDialog = new ProgressDialog(context);
        this.context=context;
        this.mapView=mapView;
        this.floor_level=floor_level;
        this.minlevel=minlevel;
        this.maxlevel=maxlevel;
        this.kmlDocument =new KmlDocument[maxlevel-minlevel+1];
        this.folderOverlays=new FolderOverlay[maxlevel-minlevel+1];
    }
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog.setMessage("Loading Project...");
        progressDialog.show();
    }
    @Override
    protected Void doInBackground(Void... voids) {

        Thread[] threads=new Thread[maxlevel-minlevel+1];
        for (int i=minlevel,j=0;i<=maxlevel;i++,j++) {
            threads[j] = new Thread(new wczytywanie_mapy(kmlDocument,folderOverlays,j, i,mapView));
            threads[j].start();
        }
        try {
            threads[floor_level-minlevel].join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        show_map();
        return null;
    }
    @Override
    protected void onPostExecute(Void aVoid) {
        progressDialog.dismiss();
        mapView.invalidate();
        BoundingBox bb = kmlDocument[floor_level-minlevel].mKmlRoot.getBoundingBox();
        mapView.zoomToBoundingBox(bb, true);
        super.onPostExecute(aVoid);
    }

    private void  show_map()
    {
        mapView.getOverlays().add(folderOverlays[floor_level-minlevel]);
        ///dodawanieznacznika
       dodawanie_znacznika_lokalizacji();
        mapView.setBackgroundColor(2000);
    }
    public void  show_map(int level)
    {
        floor_level=level;
        show_map();
    }
    public KmlFeature[] print_item_KML()
    {
        KmlFeature[] mItem= kmlDocument[floor_level-minlevel].mKmlRoot.mItems.toArray(new KmlFeature[0]);
        return mItem;
    }
    void dodawanie_znacznika_lokalizacji()
    {
        znacznik= new Znacznik_Pozycji(mapView,context,floor_level, (GeoPoint) mapView.getMapCenter());
        nasłuchiwanie_znacznika_pozycji nasłuchiwanie = new nasłuchiwanie_znacznika_pozycji(context,floor_level);
        znacznik.setOnMarkerDragListener(nasłuchiwanie);
        mapView.getOverlays().add(znacznik);
    }
}