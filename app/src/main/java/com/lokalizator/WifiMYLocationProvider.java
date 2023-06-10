package com.lokalizator;

import android.content.Context;
import android.location.Location;

import com.loader_Map_Building.Znacznik_Pozycji;
import com.locaton_Wifi.Wifi_Manager;
import com.locaton_Wifi.odczytywanie_pozycji;

import org.osmdroid.views.overlay.mylocation.IMyLocationConsumer;
import org.osmdroid.views.overlay.mylocation.IMyLocationProvider;

public class WifiMYLocationProvider implements IMyLocationProvider {


    Wifi_Manager WIFI;
    Context context;
    Znacznik_Pozycji znacznik_pozycji;
    odczytywanie_pozycji sesja;
    Location location;
    uniwersal_location uniwersal_location;

    public WifiMYLocationProvider(Context kontekst,uniwersal_location uniwersal_location)
    {
        WIFI = new Wifi_Manager(kontekst);
        this.context=kontekst;
     location= new Location("wifi") ;
     this.uniwersal_location=uniwersal_location;
    }

    @Override
    public boolean startLocationProvider(IMyLocationConsumer myLocationConsumer) {


        sesja=new odczytywanie_pozycji(context,myLocationConsumer,location,uniwersal_location);
        WIFI.Akcje_Wifi(sesja);
      //  sesja.wysyłanie("[{\"skan\":[{\"Name\":\"eduroam\",\"MAC\":\"00:0c:e6:02:1e:13\",\"RSSI\":-46},{\"Name\":\"eduroam\",\"MAC\":\"00:0c:e6:02:11:15\",\"RSSI\":-47},{\"Name\":\"MW40V_7EAA\",\"MAC\":\"0c:71:8c:2c:7e:aa\",\"RSSI\":-54},{\"Name\":\"konferencja\",\"MAC\":\"00:0c:e6:02:42:01\",\"RSSI\":-70}]}]");
        return true;
    }

    @Override
    public void stopLocationProvider() {

        sesja.setKiedy_zakończyć(true);
    }

    @Override
    public Location getLastKnownLocation() {
        return location;
    }

    @Override
    public void destroy() {

    }
}
