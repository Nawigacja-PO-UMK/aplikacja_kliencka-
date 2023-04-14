package com.example.nawigacja_po_umk;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;

import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;

import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;
import com.example.nawigacja_po_umk.ekran_Tracking.screean_Tracking;
import com.google.android.material.navigation.NavigationView;

import org.osmdroid.config.Configuration;
import org.osmdroid.views.MapView;


public class MainActivity extends AppCompatActivity{

    private Context context;
    public Mapa mapa;
    MapView mapView;
    FragmentManager fragmentManager;
    public screean_Tracking screean_tracking;
    NavController navController;
    NavigationView navigationView;
    static public SharedPreferences plik;
    final static public String file_save = "save_like";
    public static SharedPreferences.Editor prefsEditor;
    NavigationUI navigationUI;

    @SuppressLint({"MissingInflatedId", "ResourceType"})
    @Override protected void onCreate(Bundle savedInstanceState) {
        plik =this.getSharedPreferences(file_save, Context.MODE_PRIVATE);
         prefsEditor= plik.edit();
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
         navigationView= findViewById(R.id.navigation_bar_item_icon_view);
        navigationView.setItemIconTintList(null);
        navController= Navigation.findNavController(this,R.id.my_nav_host_fragment);
        NavigationUI.setupWithNavController(navigationView,navController);
    }

     public void replace_fragment(Fragment fragment, int index_contenr)
    {
        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
        fragmentTransaction.replace(index_contenr,fragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onResume() {
        if(mapView!=null) {
            mapView.onResume();
        }
        super.onResume();
    }

    @Override
    public void onPause() {
        if(mapView!=null) {
            mapView.onPause();
        }
            super.onPause();
    }



}