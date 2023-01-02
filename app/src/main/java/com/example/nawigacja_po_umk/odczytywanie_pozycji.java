package com.example.nawigacja_po_umk;

import static java.lang.Math.abs;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.wifi.ScanResult;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.osmdroid.util.GeoPoint;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class odczytywanie_pozycji implements Akcje_na_Wifi{

    private wspułżedne XY;
    private final int skala_błędu_skanowania=4;
    private Znacznik_Pozycji znacznik;
    private Context kontekst;
    URL url;
    odczytywanie_pozycji(Context kontekst,Znacznik_Pozycji znacznik)
    {
        this.XY=XY;
        this.kontekst=kontekst;
        this.znacznik=znacznik;
        try {
            url=new URL("https://nawigacjapoumk.000webhostapp.com");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

    }

    void odczytaj_pozycje(List<ScanResult> rezultat_saknu)
    {
     String JSON=parsowanie_JSON(rezultat_saknu);
     wysyłanie(JSON);

    }

    private void wysyłanie(String JSON)
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
                     odbieranie_danych(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(kontekst,error.toString(),Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String,String> getParams()
            {
                Map<String,String> wysyłane= new HashMap<String,String>();
                wysyłane.put("skan",JSON);
                return wysyłane;
            }
        };
        RequestQueue gniazdo= Volley.newRequestQueue(kontekst);
        gniazdo.add(WysyłaneDane);

    }

    void odbieranie_danych(String JSON)
    {
        ///jakaś struktura danych parsowan;
        zmian_pozycji_wskaźnika(new wspułżedne());
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
        return lista_punktów.toString();
    }

    private void zmian_pozycji_wskaźnika(wspułżedne dane)
    {
        if (dane !=null) {
            znacznik.przesunięcię_wskaznika(new GeoPoint(dane.X,dane.Y));
            Toast.makeText(kontekst, "X:"+dane.X +" Y:"+dane.Y+" Z:"+dane.Z, Toast.LENGTH_SHORT).show();
        }
        else
            Toast.makeText(kontekst, "nie znaleziono pozycji", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void Wykonywanie_funkcji_wifi(List<ScanResult> rezultat_skanu) {
        odczytaj_pozycje(rezultat_skanu);
    }
    @Override
    public boolean kiedy_zakończyć_skanowanie(List<ScanResult> results) {
        return false;
    }
}
