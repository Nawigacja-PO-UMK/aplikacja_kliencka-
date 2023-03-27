package com.loader_Map_Building;

import android.content.Context;

import com.loader_Map_Building.Style.Stylistyka;

import org.osmdroid.bonuspack.kml.KmlDocument;
import org.osmdroid.util.BoundingBox;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.FolderOverlay;
import org.osmdroid.views.overlay.Overlay;

public class wczytywanie_mapy implements Runnable {

        final int level;
        final int index;
        final private KmlDocument[] kmlDocuments;
        final private FolderOverlay[] folderOverlays;
        private final MapView mapView;
        private Context context;
        private Stylistyka stylistyka;

        public wczytywanie_mapy(KmlDocument[] kmlDocuments,FolderOverlay[] folderOverlays,int index, int level, MapView mapView, Context context)
        {
            this.level=level;
            this.index=index;
            this.kmlDocuments=kmlDocuments;
            this.folderOverlays=folderOverlays;
            this.mapView=mapView;
            this.context=context;
        }
        @Override
        public void run() {
            kmlDocuments[index] = new KmlDocument();

            // UWAGA! To co jest pobierane z Overpass jest konwertowane na nowe
            // obiekty i przypisane do FolderOverlays[index]. Pierwotne obiekty nie zostaty
            //ze wzgledow oszczednosci miejsca. Jezeli chce się także te obiekty, trzeba  dac
            //ponizszy kod po pobraniu danych a nie przed!

            folderOverlays[index] = (FolderOverlay) kmlDocuments[index].mKmlRoot.buildOverlay(mapView, null, null,kmlDocuments[index]);

            Map_Overpass map_overpass = new Map_Overpass();
            String tag = "level=" + level;
            BoundingBox boxA = new BoundingBox(53.01784, 18.60415, 53.01693, 18.60197);
            String url = map_overpass.urlForTagSearchKml(tag, boxA,10000,1000);
            ///Dodawanie obiektów z JSON do kmlDocument i tworzenie warstwy piętra
            while(!map_overpass.addInKmlFolder(kmlDocuments[index].mKmlRoot,url));

            ////// Dodanie stylistyki (Budowa mapy wystylizowanej, na razie bez zadnych
            ////// klikalnych obiektów na niej bezpośrednio
            stylistyka = new Stylistyka(context,kmlDocuments[index],mapView);
            stylistyka.zastosuj_stylistyke();

            for(Overlay overlay: stylistyka.get_warstwy())
            {
                folderOverlays[index].add(overlay);
            }
            //////



            folderOverlays[index].setName("Floor"+level);
        }
}
