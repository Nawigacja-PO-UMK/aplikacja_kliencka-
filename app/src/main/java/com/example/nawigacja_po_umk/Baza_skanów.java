package com.example.nawigacja_po_umk;

import android.content.Context;
import android.net.wifi.ScanResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.List;

public class Baza_skanów extends Baza{

    public Baza_skanów(String plikBazy, Context kontekst, URL url) {
        super(plikBazy, kontekst, url);
    }

@Override
    JSONObject parsowanie_do_JSON(List<ScanResult> rezultat_skanu,Object opis)
    {
        typ_danych_bazy_skan dane = (typ_danych_bazy_skan) new formatowanie_danych_do_bazy(rezultat_skanu,(wspułżedne) opis,0).get();
        try {
            //klasa wspułzedne
            JSONObject xy= new JSONObject();
            xy.put("X",dane.XY.X);
            xy.put("Y",dane.XY.Y);
            xy.put("Z",dane.XY.Z);
            JSONArray lista_punktów=new JSONArray();
            for (skan sk: dane.AP) {
                //pojedyńczy zeskanowany router
                JSONObject punkt = new JSONObject();
                punkt.put("Name", sk.Nazwa);
                punkt.put("MAC", sk.MAC);
                punkt.put("RSSI", sk.RSSI);
                ///klasa
                lista_punktów.put(punkt);
            }
            JSONObject skan=new JSONObject();
            skan.put("XY",xy);
            skan.put("skan",lista_punktów);
            return skan;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    @Override
    protected Object[] parsowanie_z_JSON( String dane)
    {

        try {
            JSONArray skany=new JSONArray(dane);
            typ_danych_bazy_skan[]  wynik = new typ_danych_bazy_skan[skany.length()];
            for (int j=0;j<skany.length();j++) {
                JSONObject skan = skany.getJSONObject(j);
                wynik[j]=new typ_danych_bazy_skan();
                ///klasa wspułżednych
                JSONObject xy = skan.getJSONObject("XY");
                wynik[j].XY = new wspułżedne();
                wynik[j].XY.X =  xy.getDouble("X");
                wynik[j].XY.Y =  xy.getDouble("Y");
                wynik[j].XY.Z =  xy.getDouble("Z");
                // skany
                JSONArray listapunktów = skan.getJSONArray("skan");
                wynik[j].AP = new skan[listapunktów.length()];
                for (int i = 0; i < listapunktów.length(); i++) {
                    wynik[j].AP[i] = new skan();
                    JSONObject punkt = listapunktów.getJSONObject(i);

                    wynik[j].AP[i].Nazwa = punkt.getString("Name");
                    wynik[j].AP[i].MAC = punkt.getString("MAC");
                    wynik[j].AP[i].RSSI = punkt.getInt("RSSI");
                }
            }
            return wynik;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

}
