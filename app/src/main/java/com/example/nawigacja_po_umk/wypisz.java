package com.example.nawigacja_po_umk;

import android.net.wifi.ScanResult;
import android.widget.TextView;

import java.util.List;

public class wypisz implements Akcje_na_Wifi {
    TextView ekran;

    public static String wypisz_skanowanie(List<ScanResult> dane_punktów)
    {
        String dane= new String();
        for ( ScanResult i:dane_punktów)
        {
            dane+="nazwa:"+i.SSID;
            dane+="\nmac:"+i.BSSID;
            dane+="\nRSSI:"+i.level;
            dane+="\n________________\n";
        }
        return dane;
    }

    wypisz(TextView ekran)
    {
       this.ekran=ekran;
    }
    @Override
    public void Wykonywanie_funkcji_wifi(List<ScanResult> rezultat_skanu) {
        ekran.setText(wypisz_skanowanie(rezultat_skanu));
    }
    @Override
    public boolean kiedy_zakończyć_skanowanie(List<ScanResult> results)
    {
        return false;
    }
}
