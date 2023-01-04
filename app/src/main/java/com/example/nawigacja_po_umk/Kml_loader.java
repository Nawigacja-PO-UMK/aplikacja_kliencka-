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
            //threads[j] = new Thread(new wczytywanie_mapy(kmlDocument,folderOverlays,j, i,mapView));
            //threads[j].start();
            wczytywanie_mapy(kmlDocument,folderOverlays,j, i,mapView);
        }
        /*
        try {
            for (int i=0;i<threads.length;i++) {
                threads[i].join();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        /
         */
        show_map();
        return null;
    }

    void wczytywanie_mapy(KmlDocument[] kmlDocuments,FolderOverlay[] folderOverlays,int index, int level,MapView mapView)
    {
        kmlDocuments[index] = new KmlDocument();
        Map_Overpass map_overpass = new Map_Overpass();
        String tag = "level=" + level;
        BoundingBox boxA = new BoundingBox(53.01784, 18.60515, 53.01673, 18.60197);
        String url = map_overpass.urlForTagSearchKml(tag, boxA,10000,1000);
        ///Dodawanie obiektów z JSON do kmlDocument i tworzenie warstwy piętra
        map_overpass.addInKmlFolder(kmlDocuments[index].mKmlRoot,url);
        folderOverlays[index] = (FolderOverlay) kmlDocuments[index].mKmlRoot.buildOverlay(mapView, null, null,kmlDocuments[index]);
        folderOverlays[index].setName("Floor"+level);
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
        mapView.getOverlays().add(0,folderOverlays[floor_level-minlevel]);
        ///dodawanieznacznika

        mapView.setBackgroundColor(2000);
    }
    public void  show_map(int level)
    {
        floor_level=level;
        show_map();
    }
    public KmlFeature[][] print_item_KML()
    {
        KmlFeature[][] mItem= new KmlFeature[kmlDocument.length][];
        for(int i=0;i<kmlDocument.length;i++) {
            mItem[i] = kmlDocument[i].mKmlRoot.mItems.toArray(new KmlFeature[0]);
        }
        return mItem;
    }

}