package com.example.nawigacja_po_umk;

import static java.lang.Math.abs;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.widget.Toast;

import org.osmdroid.util.GeoPoint;

import java.util.List;

public class odczytywanie_pozycji implements Akcje_na_Wifi{

    private wspułżedne XY;
    private final int skala_błędu_skanowania=4;
    private znacznik_Pozycji znacznik;
    private Context kontekst;
    odczytywanie_pozycji(Context kontekst,znacznik_Pozycji znacznik)
    {
        this.XY=XY;
        this.kontekst=kontekst;
        this.znacznik=znacznik;

    }

    wspułżedne odczytaj_pozycje(List<ScanResult> rezultat_saknu)
    {
     String JSON=  parsowanie_JSON(rezultat_saknu);
     //wysyłanie na serwer

        // odbieranie z serwera

        // parsowanie do wspułżedne
        return null;

    }
   private String parsowanie_JSON(List<ScanResult> rezultat_saknu)
    {
        String wynik="";
        ///parsowanie
        return wynik;
    }
    @Override
    public void Wykonywanie_funkcji_wifi(List<ScanResult> rezultat_skanu) {
        wspułżedne dane=odczytaj_pozycje(rezultat_skanu);
        if (dane !=null) {
            znacznik.przesunięcię_wskaznika(new GeoPoint(dane.X,dane.Y));
            Toast.makeText(kontekst, "X:"+dane.X +" Y:"+dane.Y+" Z:"+dane.Z, Toast.LENGTH_SHORT).show();
        }
        else
            Toast.makeText(kontekst, "nie znaleziono pozycji", Toast.LENGTH_SHORT).show();
    }
    @Override
    public boolean kiedy_zakończyć_skanowanie(List<ScanResult> results) {
        return false;
    }
}
