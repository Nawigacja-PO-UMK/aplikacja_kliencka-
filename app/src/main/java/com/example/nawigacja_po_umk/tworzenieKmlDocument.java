package com.example.nawigacja_po_umk;

import org.osmdroid.bonuspack.kml.KmlDocument;
import org.osmdroid.util.BoundingBox;

public class tworzenieKmlDocument implements Runnable {
    KmlDocument[]  kml;
    final int level;
    final int index;

    public tworzenieKmlDocument(KmlDocument[] kml,int index,int level)
    {

        this.kml=kml;
        this.level=level;
        this.index=index;
    }
    @Override
    public void run() {
        this.kml[index] = new KmlDocument();
        Map_Overpass map_overpass = new Map_Overpass();
        String tag = "level=" + level;
        BoundingBox boxA = new BoundingBox(53.01784, 18.60515, 53.01673, 18.60197);
        String url = map_overpass.urlForTagSearchKml(tag, boxA,10000,1000);
        ///Dodawanie obiektów z JSON do kmlDocument i tworzenie warstwy piętra
        map_overpass.addInKmlFolder(kml[index].mKmlRoot,url);
    }
}
