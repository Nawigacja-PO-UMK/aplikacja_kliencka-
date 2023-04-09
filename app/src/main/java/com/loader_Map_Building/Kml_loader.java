package com.loader_Map_Building;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.loader_Map_Building.wczytywanie_mapy;

import org.osmdroid.bonuspack.kml.KmlDocument;
import org.osmdroid.bonuspack.kml.KmlFeature;
import org.osmdroid.util.BoundingBox;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.FolderOverlay;

public class Kml_loader extends AsyncTask<Void, Void, Void> {
    ProgressDialog progressDialog;

    KmlDocument[] kmlDocument;
    private Context context;
    private MapView mapView;
    public FolderOverlay[] folderOverlays;
    int floor_level;
    int minlevel;
    int maxlevel;
    BoundingBox box;

    Kml_loader(Context context,BoundingBox box, MapView mapView,int floor_level,int minlevel,int maxlevel )
    {
        this.box=box;
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
            threads[j] = new Thread(new wczytywanie_mapy(box,kmlDocument,folderOverlays,j, i,mapView,context));
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
        //mapView.zoomToBoundingBox(bb, true);
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