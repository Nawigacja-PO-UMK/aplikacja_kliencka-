package com.example.nawigacja_po_umk;

import android.content.Context;
import android.net.wifi.ScanResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.List;

public class baza_testów  extends Baza{

    public baza_testów(String plikBazy, Context kontekst, URL url) {
        super(plikBazy, kontekst, url);
    }

    @Override
    JSONObject parsowanie_do_JSON(List<ScanResult> rezultat_skanu, Object opis) {
        typ_danych_bazy_test dane=(typ_danych_bazy_test) new formatowanie_danych_do_bazy(rezultat_skanu,opis,1).get();
        try {
            //klasa wspułzedne
            JSONObject opisn= new JSONObject();
            opisn.put("czas",dane.opis.czas);
            opisn.put("nagranie",dane.opis.nagranie);
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
            skan.put("opis_nagrania",opisn);
            skan.put("skan",lista_punktów);
            return skan;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected Object[] parsowanie_z_JSON(String dane) {

        try {
            JSONArray skany=new JSONArray(dane);
            typ_danych_bazy_test[]  wynik = new typ_danych_bazy_test[skany.length()];
            for (int j=0;j<skany.length();j++) {
                JSONObject skan = skany.getJSONObject(j);
                wynik[j]=new typ_danych_bazy_test();

                JSONObject opis = skan.getJSONObject("opis_nagrania");
                wynik[j].opis = new opis_nagrania();
                wynik[j].opis.czas =  opis.getDouble("czas");
                wynik[j].opis.nagranie = opis.getInt("nagranie");
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
