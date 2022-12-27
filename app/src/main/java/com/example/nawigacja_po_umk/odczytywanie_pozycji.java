package com.example.nawigacja_po_umk;

import static java.lang.Math.abs;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.widget.Toast;

import org.osmdroid.util.GeoPoint;

import java.util.List;

public class odczytywanie_pozycji implements Akcje_na_Wifi{

    private Baza_skanów baza;
    private wspułżedne XY;
    private final int skala_błędu_skanowania=4;
    private znacznik_Pozycji znacznik;
    private Context kontekst;
    odczytywanie_pozycji(Baza_skanów baza,Context kontekst,znacznik_Pozycji znacznik)
    {
        this.XY=XY;
        this.baza=baza;
        this.kontekst=kontekst;
        this.znacznik=znacznik;

    }

    typ_danych_bazy_skan odczytaj_pozycje(wspułżedne XY)
    {
        typ_danych_bazy_skan[] skany=(typ_danych_bazy_skan[]) baza.odczytaj_dane();
        if(skany!=null) {
            for (typ_danych_bazy_skan skan : skany) {
                if (skan.XY.X == XY.X && skan.XY.Y == XY.Y)
                    return skan;
            }
        }
        return null;
    }

    typ_danych_bazy_skan odczytaj_pozycje(List<ScanResult> rezultat_saknu)
    {
        typ_danych_bazy_skan[] skany=(typ_danych_bazy_skan[])baza.odczytaj_dane();
        String wynik=new String();
        int lidzba_trafien=0;
        if(skany==null)
            return null;
        for (typ_danych_bazy_skan skan: skany)
        {
            lidzba_trafien=0;
            for (skan i: skan.AP)
            {
                for (ScanResult j: rezultat_saknu)
                {
                    if(i.MAC.equals(j.BSSID)&& abs(i.RSSI-j.level)<skala_błędu_skanowania )
                        lidzba_trafien++;

                }
            }
            if(lidzba_trafien>2)
                return skan;
        }

        return null;

    }

    @Override
    public void Wykonywanie_funkcji_wifi(List<ScanResult> rezultat_skanu) {

        typ_danych_bazy_skan dane=odczytaj_pozycje(rezultat_skanu);

        if (dane !=null) {
            znacznik.przesunięcię_wskaznika(new GeoPoint(dane.XY.X,dane.XY.Y));
            Toast.makeText(kontekst, "X:"+dane.XY.X +" Y:"+dane.XY.Y+" Z:"+dane.XY.Z, Toast.LENGTH_SHORT).show();
        }
        else
            Toast.makeText(kontekst, "nie znaleziono pozycji", Toast.LENGTH_SHORT).show();
    }
    @Override
    public boolean kiedy_zakończyć_skanowanie(List<ScanResult> results) {
        return true;
    }
}
