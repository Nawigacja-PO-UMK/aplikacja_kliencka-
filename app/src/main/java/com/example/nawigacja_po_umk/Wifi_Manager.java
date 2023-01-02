package com.example.nawigacja_po_umk;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class Wifi_Manager extends AppCompatActivity {

    private WifiManager Wifi;
    Context kontekst;
    boolean zeskanowany=true;
    public Wifi_Manager(Context kontekst) {

        Wifi = (WifiManager) kontekst.getSystemService(Context.WIFI_SERVICE);
        this.kontekst=kontekst;
        if (!Wifi.isWifiEnabled())
            Toast.makeText(kontekst,"Prosze włączyć Wifi",Toast.LENGTH_LONG).show();
    }
    public void Akcje_Wifi(Akcje_na_Wifi W)
    {
        BroadcastReceiver wifiScanReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context c, Intent intent) {
                List<ScanResult> skan= Skanowanie();
                   W.Wykonywanie_funkcji_wifi(skan);
                   if(W.kiedy_zakończyć_skanowanie(skan))
                kontekst.unregisterReceiver(this);
                   else Wifi.startScan();
            }
        };
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
        kontekst.registerReceiver(wifiScanReceiver, intentFilter);
        Wifi.startScan();
    }
    public List<ScanResult>  Skanowanie()
    {
        List<ScanResult> punkty = Wifi.getScanResults();
        return punkty;
    }
    public void skanowanie_na_ządanie()
    {
        Wifi.startScan();
    }
}
