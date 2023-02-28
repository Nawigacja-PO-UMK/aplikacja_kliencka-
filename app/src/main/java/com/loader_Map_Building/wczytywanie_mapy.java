package com.loader_Map_Building;

import org.osmdroid.bonuspack.kml.KmlDocument;
import org.osmdroid.util.BoundingBox;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.FolderOverlay;

public class wczytywanie_mapy implements Runnable {

        final int level;
        final int index;
        final private KmlDocument[] kmlDocuments;
        final private FolderOverlay[] folderOverlays;
        private MapView mapView;
        public wczytywanie_mapy(KmlDocument[] kmlDocuments,FolderOverlay[] folderOverlays,int index, int level, MapView mapView)
        {
            this.level=level;
            this.index=index;
            this.kmlDocuments=kmlDocuments;
            this.folderOverlays=folderOverlays;
            this.mapView=mapView;
        }
        @Override
        public void run() {
            kmlDocuments[index] = new KmlDocument();
            Map_Overpass map_overpass = new Map_Overpass();
            String tag = "level=" + level;
            BoundingBox boxA = new BoundingBox(53.01784, 18.60515, 53.01673, 18.60197);
            String url = map_overpass.urlForTagSearchKml(tag, boxA,10000,1000);
            ///Dodawanie obiektów z JSON do kmlDocument i tworzenie warstwy piętra
            while(!map_overpass.addInKmlFolder(kmlDocuments[index].mKmlRoot,url));
            folderOverlays[index] = (FolderOverlay) kmlDocuments[index].mKmlRoot.buildOverlay(mapView, null, null,kmlDocuments[index]);
            folderOverlays[index].setName("Floor"+level);
        }
}
