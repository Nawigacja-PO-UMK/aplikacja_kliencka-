package com.example.nawigacja_po_umk;

import android.content.Context;

import java.net.MalformedURLException;

public class Pozycjonowanie {

        private Context kontekst;
        private Wifi_Manager WIFI;
        private String plikBazy;


        Pozycjonowanie(Context kontekst,String plikBazy) throws MalformedURLException {
            WIFI = new Wifi_Manager(kontekst);
            this.plikBazy=plikBazy;
            this.kontekst=kontekst;
        }
        public void odczytaj_pozycje(Znacznik_Pozycji znacznik)
        {
            odczytywanie_pozycji sesja=new odczytywanie_pozycji(kontekst,znacznik);
            WIFI.Akcje_Wifi(sesja);
        }

        public void dynamiczne_funkcje_na_wifi(Akcje_na_Wifi akcja)
        {
            WIFI.Akcje_Wifi(akcja);
        }


}

