package com.example.nawigacja_po_umk;

import android.net.wifi.ScanResult;

import java.util.List;

public interface Akcje_na_Wifi {

    void Wykonywanie_funkcji_wifi(List<ScanResult> rezultat_skanu);
    boolean kiedy_zakończyć_skanowanie(List<ScanResult> results);
}
