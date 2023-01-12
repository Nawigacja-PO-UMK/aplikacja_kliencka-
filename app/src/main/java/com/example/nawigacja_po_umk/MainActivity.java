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
import android.text.method.ScrollingMovementMethod;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
    EditText room;
    Mapa mapa;
    TextView instruction;
    TextView tracking;
    Button remove;
    @SuppressLint("MissingInflatedId")
    @Override protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Context ctx = getApplicationContext();
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));
        setContentView(R.layout.activity_main);
        room=findViewById(R.id.editTextTextPersonName);
        context = this;
        mapa= new Mapa(context,findViewById(R.id.map));
        instruction= (TextView) findViewById(R.id.instruction);
        tracking=(TextView) findViewById(R.id.tracking);
        remove=(Button)findViewById(R.id.remove);
        tracking.setMovementMethod(new ScrollingMovementMethod());
               ///pozycjonowanie
        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 0);
        try {
            pozycja = new Pozycjonowanie(context);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        //uruchomie jak bedziemy mieli serwer i skończony model od marcina
       /// pozycja.odczytaj_pozycje(mapa.loadKml.znacznik);
    }

    public void removeTracking(View view)
    {
        tracking.setText("");
        tracking.setBackgroundColor(0);
        mapa.remove_tracking();

    }


    @SuppressLint("SuspiciousIndentation")
    public void LewelUpMap(View view)
    {
        if(mapa.levelmax()>mapa.level()) {
            mapa.wczytaj_nowa_mape(mapa.level() + 1);
            update_descryption();
        }
    }

    public void LewelDownMap(View view)
    {
        if(mapa.Levelmin()<mapa.level()) {
            mapa.wczytaj_nowa_mape(mapa.level() - 1);
            update_descryption();
        }
    }

    public void trasowanie(View view)
    {
    mapa.add_tracking(room.getText().toString());
    instruction.setMovementMethod(new ScrollingMovementMethod());
    update_descryption();
    }
    private void update_descryption()
    {
        trasa[] trasa =mapa.get_trasa();
        if(trasa[mapa.level()-mapa.Levelmin()]!= null) {
            tracking.setBackgroundColor(0xFFFFFFFF);
            instruction.setBackgroundColor(0xFFFFFFFF);
            instruction.setAllCaps(true);
            instruction.setText(trasa[mapa.level() - mapa.Levelmin()].print_descryption());
            tracking.setText(trasa[mapa.level() - mapa.Levelmin()].print_name_tracking());
        }
        else
        {
            tracking.setBackgroundColor(0);
            instruction.setBackgroundColor(0);
            instruction.setText("");
            tracking.setText("");
        }
    }
}