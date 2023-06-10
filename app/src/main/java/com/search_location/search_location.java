package com.search_location;

import android.annotation.SuppressLint;
import android.location.Address;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;
import org.osmdroid.bonuspack.kml.KmlFeature;
import org.osmdroid.bonuspack.location.OverpassAPIProvider;
import org.osmdroid.bonuspack.location.POI;
import org.osmdroid.util.BoundingBox;
import org.osmdroid.util.GeoPoint;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

public class search_location {

static private final String key_api_flickr="062a0eb83c271c0de95439b5ccfd1601";


static public GeoPoint my_car;
    static public List<Address> search(String nazwa, int max_Rezultat) {
        List<Address> addresses= new ArrayList<>();
        com.search_location.GeocoderNominatim searche = new com.search_location.GeocoderNominatim(new Locale("Polish (pl)", "Poland (PL)"), "MateuszBloch_aplikacj");
                try {
                    addresses=  searche.getFromLocationName(nazwa, max_Rezultat);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if(nazwa.equals("auto")&& my_car!=null)
                {
                 Address tmpAdress=   new Address(new Locale("Polish (pl)", "Poland (PL)"));
                 tmpAdress.setLatitude(my_car.getLatitude());
                 tmpAdress.setLongitude(my_car.getLongitude());
                 tmpAdress.setFeatureName("zaparkowane Auto");
                 addresses.add(0,tmpAdress);
                }

        return addresses;

    }
    static public List<POI> search(int max_Rezultat,GeoPoint point,double distans)
    {

        com.search_location.GeoNamesPOIProvider poiProvider = new com.search_location.GeoNamesPOIProvider("matbloch");
        return poiProvider.getPOICloseTo(point,max_Rezultat,distans);
    }
    static public List<Address> search(GeoPoint point, int maxrezults)
    {
        com.search_location.GeocoderNominatim searche = new com.search_location.GeocoderNominatim(new Locale("Polish (pl)", "Poland (PL)"), "MateuszBloch_aplikacj");

        try {
            return searche.getFromLocation(point.getLatitude(),point.getLongitude(),maxrezults);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();

    }


    @SuppressLint("SuspiciousIndentation")
    static public  List<POI> search_flickr(int max_Rezultat, BoundingBox boundingBox, boolean bigsize)
    {
        com.search_location.FlickrPOIProvider poiProvider = new FlickrPOIProvider(key_api_flickr);
        if(bigsize)
            return poiProvider.getPOIInside(boundingBox,max_Rezultat,"url_l");
        else
        return poiProvider.getPOIInside(boundingBox, max_Rezultat,"url_sq");
    }
    static public  POI get_Bigsizefoto(POI poi_flickr)
    {
        com.search_location.FlickrPOIProvider poiProvider = new FlickrPOIProvider(key_api_flickr);
            return poiProvider.getPhoto(String.valueOf(poi_flickr.mId));
    }



    static public String search_name_Adress(Address address)
    {
        if(address.getFeatureName()== null) {
            if(address.getExtras().getString("display_name").split(",")[0].equals(address.getSubThoroughfare()))
                return address.getLocality()+" "+address.getSubThoroughfare();
            else
            return address.getExtras().getString("display_name").split(",")[0];

        }else
            return address.getFeatureName();
    }
    static public List<Item> search_in_building(@NotNull String name, KmlFeature[][] items)
    {
        List<Item> ITEMS= new ArrayList<>();
        for (int j=0;j< items.length;j++) {
            for (int i = 0; i < items[j].length; i++) {
                if (items[j][i].mName != null) {
                    ITEMS.add(new Item(items[j][i],j));
                }
            }
        }
           return  ITEMS;
    }






}
