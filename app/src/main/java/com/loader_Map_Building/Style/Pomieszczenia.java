package com.loader_Map_Building.Style;

import android.graphics.Color;

import org.osmdroid.bonuspack.kml.KmlFeature;
import org.osmdroid.bonuspack.kml.KmlGeometry;
import org.osmdroid.bonuspack.kml.KmlPlacemark;
import org.osmdroid.views.overlay.Overlay;
import org.osmdroid.views.overlay.Polygon;

import java.util.ArrayList;
import java.util.List;

public class Pomieszczenia {

    private final int color;
    private final List<Polygon> polygon_list = new ArrayList<>();

    public Pomieszczenia(int color)
    {
        this.color=color;
    }

    public void Dodaj_pomieszczenie(KmlFeature feature)
    {
        KmlGeometry geometry = ((KmlPlacemark)(feature)).mGeometry;

        Polygon polygon = new Polygon();

        polygon.setPoints(geometry.mCoordinates);
        polygon.setFillColor(color);
        polygon.setStrokeWidth(7);
        polygon.setStrokeColor(Color.GRAY);
        polygon_list.add(polygon);
    }

    public ArrayList<Overlay> get_warstwy()
    {
        return new ArrayList<>(polygon_list);
    }
}