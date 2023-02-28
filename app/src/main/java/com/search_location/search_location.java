package com.search_location;

import android.location.Address;

import org.jetbrains.annotations.NotNull;
import org.osmdroid.bonuspack.kml.KmlFeature;
import org.osmdroid.bonuspack.location.GeocoderNominatim;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class search_location {




    static public List<Address> search(String nazwa, int max_Rezultat) {
            GeocoderNominatim searche = new GeocoderNominatim(new Locale("Polish (pl)", "Poland (PL)"), "MateuszBloch_aplikacj");
                try {
                    return searche.getFromLocationName(nazwa, max_Rezultat);
                } catch (IOException e) {
                    e.printStackTrace();
                }
        return new ArrayList<>();

    }
    static public List<Address> search(String nazwa, int max_Rezultat,double latitude, double longitude,double long_searche) throws IOException
    {
        GeocoderNominatim searche= new GeocoderNominatim(new Locale("Polish (pl)","Poland (PL)"),"test");
        return searche.getFromLocationName(nazwa,max_Rezultat,latitude,longitude,latitude,longitude);
    }


    static public String search_name_Adress(Address address)
    {
        if(address.getFeatureName()== null) {
            return address.getExtras().getString("display_name").split(",")[0];
        }else
            return address.getFeatureName();
    }
    static public Item search_in_building(@NotNull String name, KmlFeature[][] items)
    {
        for (int j=0;j< items.length;j++) {
            for (int i = 0; i < items[j].length; i++) {
                if (items[j][i].mName != null && items[j][i].mName.equals(name)) {
                    Item item=new Item(items[j][i],j);
                    return item;
                }
            }
        }
        return null;
    }



}
