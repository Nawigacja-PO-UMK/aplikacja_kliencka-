package com.locaton_Wifi;

import android.content.Context;

import com.loader_Map_Building.Znacznik_Pozycji;

import java.net.MalformedURLException;

public class Pozycjonowanie {

        private Context kontekst;
        private Wifi_Manager WIFI;



        public Pozycjonowanie(Context kontekst) throws MalformedURLException {
            WIFI = new Wifi_Manager(kontekst);
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

