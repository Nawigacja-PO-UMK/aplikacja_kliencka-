package com.example.nawigacja_po_umk;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.widget.Toast;

import java.util.Date;
import java.util.List;

public class zapisywanie_wifi_do_Bazy implements Akcje_na_Wifi{

    protected Baza baza;
    protected Object opis;
    protected boolean czynagrywać;
    protected Context kontekst;
    Date czas_początkowy;
    boolean nagranie;


    zapisywanie_wifi_do_Bazy(Baza baza,Context kontekst,Object opis,boolean czynagrywać)
    {
        this.opis=opis;
        this.baza=baza;
        this.czynagrywać=czynagrywać;
        this.kontekst=kontekst;
        this.czas_początkowy =new Date();
        this.nagranie=czynagrywać;
    }

    public void przestań_nagrywać()
    {
        czynagrywać=false;
    }

    @Override
    public void Wykonywanie_funkcji_wifi(List<ScanResult> rezultat_skanu) {

        if(nagranie) {
            Date obecny_czas = new Date();
            ((opis_nagrania) opis).czas = (obecny_czas.getTime() - czas_początkowy.getTime()) / 1000;
        }
            baza.Zapisywanie_do_Bazy(rezultat_skanu,opis);

        if(!czynagrywać)
            Toast.makeText(kontekst, "zapisane", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean kiedy_zakończyć_skanowanie(List<ScanResult> results) {
        return !czynagrywać;
    }
}
