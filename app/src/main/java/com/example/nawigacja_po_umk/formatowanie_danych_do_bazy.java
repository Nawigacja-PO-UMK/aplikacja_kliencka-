package com.example.nawigacja_po_umk;

import android.net.wifi.ScanResult;

import java.util.List;


public class formatowanie_danych_do_bazy {

Object Dane;
    formatowanie_danych_do_bazy(List<ScanResult> rezultat_skanu, Object opis,int typ)
    {
        switch (typ) {
            case 0:
                 typ_danych_bazy_skan dane=new typ_danych_bazy_skan();
                 dane.XY=(wspułżedne) opis;
                 dane.AP=wypisz_skanowanie(rezultat_skanu);
                 Dane=dane;
                break;
            case 1:
                typ_danych_bazy_test d =new typ_danych_bazy_test();
                d.opis=(opis_nagrania) opis;
                d.AP=wypisz_skanowanie(rezultat_skanu);
                Dane=d;
                break;
            default:
                return;
        }

    }

    public Object get()
    {
        return this.Dane;
    }

    private static skan[] wypisz_skanowanie(List<ScanResult> dane_punktów) {
        skan[] skany = new skan[dane_punktów.size()];
        int j=0;
        for (ScanResult i : dane_punktów) {
            skany[j]=new skan();
            skany[j].Nazwa =i.SSID;
            skany[j].MAC =i.BSSID;
            skany[j].RSSI = i.level;
            j++;
        }
        return skany;
    }
}


