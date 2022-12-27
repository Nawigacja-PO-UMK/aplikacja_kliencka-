package com.example.nawigacja_po_umk;

import android.content.Context;
import android.content.SharedPreferences;
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

import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public abstract class Baza {

    protected final String plikBazy;
    protected final Context kontekst;
    protected JSONArray Bazadanych;
    private final URL url;
    private SharedPreferences plik;
    private SharedPreferences.Editor edytor;

    public Baza(String plikBazy,Context kontekst,URL url)  {
        this.plikBazy=plikBazy;
        this.kontekst=kontekst;
        this.url=url;
        Bazadanych = new JSONArray();
    }
    abstract JSONObject parsowanie_do_JSON(List<ScanResult> rezultat_skanu, Object opis);
    abstract protected Object[] parsowanie_z_JSON(String dane);


    public Object[] odczytaj_dane()  {

        String JSON=odczytaj_plik();
        if( JSON!=null) {
            return parsowanie_z_JSON(JSON);
        }
        return null;
    }


    public void Zapisywanie_do_Bazy(List<ScanResult> rezultat_skanu,Object opis)
    {
        try {
            String JSON=odczytaj_plik();
            if( JSON!=null)
                this.Bazadanych = new JSONArray(JSON);
            if (rezultat_skanu!=null && opis!=null) {
                JSONObject skan = parsowanie_do_JSON(rezultat_skanu,opis);
                this.Bazadanych.put(skan);
                Zapisywanie_do_pliku(this.Bazadanych.toString());
            } else
                Toast.makeText(kontekst, "nie można zapisać (błąd skanu)", Toast.LENGTH_LONG).show();

        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(kontekst, "nie można zapisać (bład danych JSON)", Toast.LENGTH_LONG).show();
        }
    }
       public String odczytaj_plik()
    {
        plik = kontekst.getSharedPreferences(plikBazy, Context.MODE_PRIVATE);
        String dane=plik.getString("baza",null);
        return dane;

    }
    void Zapisywanie_do_pliku(String dane)
    {
        plik = kontekst.getSharedPreferences(plikBazy, Context.MODE_PRIVATE);
        plik.edit().putString("baza",dane).commit();
    }

    void wysyłanie_na_Serwer()
    {
        ConnectivityManager łączę =(ConnectivityManager) kontekst.getSystemService(Context.CONNECTIVITY_SERVICE);
        if(!(łączę.getActiveNetworkInfo()!=null &&  łączę.getActiveNetworkInfo().isConnected()))
        {
            Toast.makeText(kontekst,"włacz internet",Toast.LENGTH_LONG).show();
            return;
        }
        String baza=odczytaj_plik();
        StringRequest WysyłaneDane= new StringRequest(Request.Method.POST, url.toString(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(kontekst,response,Toast.LENGTH_LONG).show();
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
                wysyłane.put("dane",baza);
                return wysyłane;
            }
        };
        RequestQueue gniazdo= Volley.newRequestQueue(kontekst);
        gniazdo.add(WysyłaneDane);
    }

    public void kasuj()
    {
        plik = kontekst.getSharedPreferences(plikBazy, Context.MODE_PRIVATE);
        plik.edit().remove("baza").commit();
        Bazadanych=new JSONArray();
    }

}
