package com.locaton_Wifi;

import static java.lang.Math.abs;

import android.content.Context;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.wifi.ScanResult;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.lokalizator.WifiMYLocationProvider;
import com.lokalizator.uniwersal_location;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.osmdroid.views.overlay.mylocation.IMyLocationConsumer;
import org.osmdroid.views.overlay.mylocation.IMyLocationProvider;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class odczytywanie_pozycji implements Akcje_na_Wifi {

    private współrzedne XY;
    private final int skala_błędu_skanowania=4;
    private IMyLocationConsumer locationConsumer;
    private Context kontekst;
    private boolean kiedy_zakończyć;
    private Location location;
    private IMyLocationProvider myLocationProvider;
    URL url;
    private long time_scan=4000;
    private long actual_time;


    public void setKiedy_zakończyć(boolean kiedy_zakończyć) {
        this.kiedy_zakończyć = kiedy_zakończyć;
    }

    public odczytywanie_pozycji(Context kontekst, IMyLocationConsumer locationConsumer, Location location, uniwersal_location wifiMYLocationProvider)
    {
        this.XY=XY;
        this.kontekst=kontekst;
        this.location=location;
        this.locationConsumer=locationConsumer;
        this.myLocationProvider=wifiMYLocationProvider;
        kiedy_zakończyć=false;
        actual_time=(new Date()).getTime();
        try {
            url=new URL("https://34.125.216.223/MLModel/location_wifi.php");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

    }

    void odczytaj_pozycje(List<ScanResult> rezultat_saknu)
    {
     String JSON=parsowanie_JSON(rezultat_saknu);
     wysyłanie(JSON);

    }

    public void wysyłanie(String JSON)
    {
        ConnectivityManager łączę =(ConnectivityManager) kontekst.getSystemService(Context.CONNECTIVITY_SERVICE);
        if(!(łączę.getActiveNetworkInfo()!=null &&  łączę.getActiveNetworkInfo().isConnected()))
        {
            Toast.makeText(kontekst,"włacz internet",Toast.LENGTH_LONG).show();
            return;
        }
        final String[] wynik = {new String()};
        StringRequest WysyłaneDane= new StringRequest(Request.Method.POST, url.toString(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            odbieranie_danych(response);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
               /// Toast.makeText(kontekst,error.toString(),Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String,String> getParams()
            {
                Map<String,String> wysyłane= new HashMap<String,String>();

                //Toast.makeText(kontekst, JSON, Toast.LENGTH_SHORT).show();
                wysyłane.put("skan",JSON);
                return wysyłane;
            }
        };
        RequestQueue gniazdo= Volley.newRequestQueue(kontekst);
        gniazdo.add(WysyłaneDane);

    }





    void odbieranie_danych(String JSON) throws JSONException {
        JSONObject jsonObject= new JSONObject(JSON);
        współrzedne współrzedne=new współrzedne();
        JSONArray XY=jsonObject.getJSONArray("XY").getJSONArray(0);

        współrzedne.X= XY.getDouble(1);
        współrzedne.Y=XY.getDouble(0);
        współrzedne.Z=XY.getDouble(2);
        zmian_pozycji_wskaźnika(współrzedne);
    }



   private String parsowanie_JSON(List<ScanResult> rezultat_saknu)
    {
        JSONArray lista_punktów=new JSONArray();

        JSONObject punkt = new JSONObject();
        try {
        for (ScanResult sk: rezultat_saknu) {
                punkt.put("Name", sk.SSID);
            punkt.put("MAC", sk.BSSID);
            punkt.put("RSSI", sk.level);
            lista_punktów.put(punkt);
        }
        } catch (JSONException e) {
        e.printStackTrace();
    }
        JSONArray wynik =new JSONArray();
        JSONObject jsonObject=new JSONObject();
        try {
            jsonObject.put("skan",lista_punktów);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        wynik.put(jsonObject);
        return wynik.toString();
    }

    private void zmian_pozycji_wskaźnika(współrzedne dane)
    {
        if (dane !=null) {

            location.setAltitude(dane.Z);
            location.setLongitude(dane.Y);
            location.setLatitude(dane.X);
            location.setTime((new Date()).getTime());

            locationConsumer.onLocationChanged(location,myLocationProvider);

        }
        else
            Toast.makeText(kontekst, "nie znaleziono pozycji", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void Wykonywanie_funkcji_wifi(List<ScanResult> rezultat_skanu) {
        if((new Date()).getTime()-actual_time>time_scan) {
            odczytaj_pozycje(rezultat_skanu);
            actual_time=(new Date()).getTime();
        }
    }
    @Override
    public boolean kiedy_zakończyć_skanowanie(List<ScanResult> results) {
        return kiedy_zakończyć;
    }
}
