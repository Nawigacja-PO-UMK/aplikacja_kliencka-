package com.example.nawigacja_po_umk;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;

import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import org.osmdroid.api.IGeoPoint;
import org.osmdroid.api.IMapController;
import org.osmdroid.bonuspack.clustering.RadiusMarkerClusterer;
import org.osmdroid.bonuspack.clustering.StaticCluster;
import org.osmdroid.bonuspack.kml.KmlDocument;

import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.BoundingBox;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.CustomZoomButtonsController;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.FolderOverlay;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.infowindow.InfoWindow;

import java.net.MalformedURLException;


public class MainActivity extends AppCompatActivity{

    private Context context;
    Pozycjonowanie pozycja;
    final String Baza="daneBazy.jos";
    TextView plik;
    Switch przełocznik;
    Button skan;
    Mapa mapa;
    boolean nagrywanie=false;

    @Override protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Context ctx = getApplicationContext();
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));
        setContentView(R.layout.activity_main);
        context = this;
        mapa= new Mapa(context,findViewById(R.id.map));
        ///pozycjonowanie
        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 0);
        try {
            pozycja = new Pozycjonowanie(context,Baza);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

    }

    @SuppressLint("SuspiciousIndentation")
    public void LewelUpMap(View view)
    {
        if(mapa.levelmax()>mapa.level())
            mapa.wczytaj_nowa_mape(mapa.level()+1);
    }

    public void LewelDownMap(View view)
    {
        if(mapa.Levelmin()<mapa.level())
            mapa.wczytaj_nowa_mape(mapa.level()-1);
    }

}