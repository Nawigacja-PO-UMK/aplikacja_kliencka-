package com.example.nawigacja_po_umk;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;

import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.example.nawigacja_po_umk.Activity.search;
import com.example.nawigacja_po_umk.Activity.trackingActivity;
import com.example.nawigacja_po_umk.Mapa;
import com.example.nawigacja_po_umk.Nawigation_Fragment.Nawigation;
import com.example.nawigacja_po_umk.R;
import com.example.nawigacja_po_umk.ekranMarker.ekran_marker;
import com.example.nawigacja_po_umk.ekran_Tracking.screean_Tracking;
import com.google.android.material.navigation.NavigationView;
import com.search_location.search_location;

import org.osmdroid.bonuspack.location.POI;
import org.osmdroid.config.Configuration;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;


public class MainActivity extends AppCompatActivity{

    private Context context;
    public Mapa mapa;
    MapView mapView;
    FragmentManager fragmentManager;
    public screean_Tracking screean_tracking;
    @SuppressLint({"MissingInflatedId", "ResourceType"})
    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragmentManager=getSupportFragmentManager();
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Context ctx = getApplicationContext();
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));
        setContentView(R.layout.activity_main);
        context = this;
        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PackageManager.PERMISSION_GRANTED);
        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, PackageManager.PERMISSION_GRANTED);

        NavigationView navigationView= findViewById(R.id.navigation_bar_item_icon_view);
        navigationView.setItemIconTintList(null);
        NavController navController= Navigation.findNavController(this,R.id.my_nav_host_fragment);
        NavigationUI.setupWithNavController(navigationView,navController);
    }

     public void replace_fragment(Fragment fragment)
    {
        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.conteiner,fragment);
        fragmentTransaction.commit();
    }
/*
    @Override
    public void onResume() {
        super.onResume();

        if(mapView!=null) {
            mapa.newInstance(mapView,screean_tracking);
            mapView.onResume();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if(mapView!=null)
            mapView.onPause();
    }
*/
}