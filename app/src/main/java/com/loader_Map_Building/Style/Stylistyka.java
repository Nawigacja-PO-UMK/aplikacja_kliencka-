package com.loader_Map_Building.Style;

import static android.graphics.Color.WHITE;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;

import androidx.core.content.res.ResourcesCompat;

import com.example.nawigacja_po_umk.R;
import org.osmdroid.api.IGeoPoint;
import org.osmdroid.bonuspack.kml.KmlDocument;
import org.osmdroid.bonuspack.kml.KmlFeature;
import org.osmdroid.bonuspack.kml.KmlGeometry;
import org.osmdroid.bonuspack.kml.KmlPlacemark;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Overlay;
import org.osmdroid.views.overlay.Polygon;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;
import java.util.Objects;

public class Stylistyka {

    Context context;
    KmlDocument kmlDocument;
    MapView mapView;

    Hashtable<String, Etykiety> etykieta_hashtable = new Hashtable<>();
    Hashtable<String, Pomieszczenia> pomieszczenie_hashtable = new Hashtable<>();

    Pomieszczenia main_layout;
    List <String> lista_pomieszczen;


    @SuppressLint("UseCompatLoadingForDrawables")
    public Stylistyka(Context context, KmlDocument kmlDocument, MapView mapView)
    {
        this.context=context;
        this.kmlDocument=kmlDocument;
        this.mapView=mapView;
        this.main_layout = new Pomieszczenia(WHITE);
        uzupelnij_tabele();
    }

    public void zastosuj_stylistyke() {

        ArrayList<KmlFeature> lista = kmlDocument.mKmlRoot.mItems;
        ArrayList<Integer>to_delete = new ArrayList<>();

        int i = 0;
        for (KmlFeature feature : lista) {

            KmlGeometry geometry = ((KmlPlacemark) (feature)).mGeometry;
            Polygon polygon = new Polygon();
            polygon.setPoints(geometry.mCoordinates);

            IGeoPoint punkt = new GeoPoint(feature.getBoundingBox().getCenterLatitude(), feature.getBoundingBox().getCenterLongitude());

            String nazwa = feature.getExtendedData("room");
            String name = feature.getExtendedData("name");
            String h = feature.getExtendedData("highway");
            if(h != null && h.equals("steps"))nazwa=feature.getExtendedData("highway");

            if(nazwa == null) {
                nazwa = feature.getExtendedData("indoor");
                if(nazwa == null) {
                    nazwa = feature.getExtendedData("highway");
                    if(nazwa == null) {
                        nazwa = feature.getExtendedData("amenity");
                        if (nazwa == null)
                            nazwa = "default";
                        else {
                            main_layout.Dodaj_pomieszczenie(feature);
                            to_delete.add(i);
                            continue;
                        }
                    }
                }
            }

            if (nazwa.equals("corridor"))
            {
                Objects.requireNonNull(pomieszczenie_hashtable.get(nazwa)).Dodaj_pomieszczenie(feature);
                i++;
                continue;
            }

            i++;

            if (name == null) name = nazwa;

            if(!nazwa.matches("steps")) Objects.requireNonNull(pomieszczenie_hashtable.get(nazwa)).Dodaj_pomieszczenie(feature);

            if(!nazwa.matches("room|toilet|toilets|storage|elevator"))
            {
                Objects.requireNonNull(etykieta_hashtable.get(nazwa)).addMarker(mapView,punkt);
                if(!nazwa.matches("steps"))Objects.requireNonNull(etykieta_hashtable.get(nazwa)).addMarker(punkt,name, 80);
            }
            else Objects.requireNonNull(etykieta_hashtable.get(nazwa)).addMarker(punkt,name,0);
        }

        for (Integer d: to_delete) {
            kmlDocument.mKmlRoot.removeItem(d);
        }
    }
    private ArrayList<Overlay> get_warstwy_kolorow()
    {
        ArrayList<Overlay> list = new ArrayList<>(main_layout.get_warstwy());

        for (String nazwa:lista_pomieszczen) {
            list.addAll(Objects.requireNonNull(pomieszczenie_hashtable.get(nazwa)).get_warstwy());
        }
        return list;
    }

    private ArrayList<Overlay> get_warstwy_etykiet()
    {
        ArrayList<Overlay> list = new ArrayList<>();

        for (String nazwa:lista_pomieszczen) {
            list.add(etykieta_hashtable.get(nazwa));
        }
        return list;
    }

    public ArrayList<Overlay> get_warstwy()
    {
        ArrayList<Overlay> list = new ArrayList<>();
        list.addAll(get_warstwy_kolorow());
        list.addAll(get_warstwy_etykiet());
        return list;
    }

    private void uzupelnij_tabele()
    {
        try {
            lista_pomieszczen = Arrays.asList("room", "cloakroom", "elevator", "corridor", "office",
                    "restaurant", "class", "steps", "toilets", "default", "storage", "toilet", "library");
            Resources res = context.getResources();
            for (String napis : lista_pomieszczen) {
                Drawable drawable;
                int color;
                switch (napis) {
                    case "library": {
                        color = Color.rgb(160, 186, 97);
                        drawable = ResourcesCompat.getDrawable(res,R.drawable.book,null);
                        break;
                    }
                    case "class": {
                        color = Color.rgb(201, 137, 169);
                        drawable = ResourcesCompat.getDrawable(res,R.drawable.sala,null);
                        break;
                    }
                    case "toilet":
                    case "toilets": {
                        color = Color.rgb(55, 184, 201);
                        drawable = ResourcesCompat.getDrawable(res,R.drawable.wc,null);
                        break;
                    }
                    case "restaurant": {
                        color = Color.rgb(122, 200, 94);
                        drawable = ResourcesCompat.getDrawable(res,R.drawable.bar,null);
                        break;
                    }
                    case "staircase":
                    case "steps": {
                        color = Color.rgb(255, 201, 14);
                        drawable = ResourcesCompat.getDrawable(res,R.drawable.steps,null);
                        break;
                    }
                    case "corridor": {
                        color = Color.rgb(222, 222, 222);
                        drawable = ResourcesCompat.getDrawable(res,R.drawable.corridor,null);
                        break;
                    }
                    case "room": {
                        color = Color.rgb(152, 200, 200);
                        drawable = ResourcesCompat.getDrawable(res,R.drawable.default_room,null);
                        break;
                    }
                    case "storage": {
                        color = Color.rgb(152, 166, 120);
                        drawable = ResourcesCompat.getDrawable(res,R.drawable.box,null);
                        break;
                    }
                    case "cloakroom": {
                        color = Color.rgb(227, 165, 160);
                        drawable = ResourcesCompat.getDrawable(res,R.drawable.hat,null);
                        break;
                    }

                    case "office": {
                        color = Color.rgb(168, 81, 189);
                        drawable = ResourcesCompat.getDrawable(res,R.drawable.office,null);
                        break;
                    }

                    case "elevator": {
                        color = Color.rgb(224, 222, 52);
                        drawable = ResourcesCompat.getDrawable(res,R.drawable.elevator,null);
                        break;
                    }

                    default: {
                        color = Color.rgb(222, 222, 222);
                        drawable = ResourcesCompat.getDrawable(res,R.drawable.defaultl,null);
                        break;
                    }
                }

                Pomieszczenia pomieszczenia = new Pomieszczenia(color);
                pomieszczenie_hashtable.put(napis, pomieszczenia);

                if (drawable != null) {
                    Etykiety etykiety = new Etykiety(drawable);
                    etykieta_hashtable.put(napis, etykiety);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}