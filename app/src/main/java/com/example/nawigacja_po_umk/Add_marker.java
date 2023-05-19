package com.example.nawigacja_po_umk;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.UserHandle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.nawigacja_po_umk.ekranMarker.ekran_marker;
import com.search_location.Item;
import com.search_location.search_location;

import org.jetbrains.annotations.NotNull;
import org.osmdroid.bonuspack.kml.KmlFeature;
import org.osmdroid.bonuspack.location.POI;
import org.osmdroid.bonuspack.routing.OSRMRoadManager;
import org.osmdroid.bonuspack.routing.RoadManager;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class Add_marker {


    public static Marker Add_marker(Address address,Context kontext, MapView mapView)
    {
        Marker marker =new Marker(mapView);


        marker.setRelatedObject(address);
        marker.setInfoWindow(new ekran_marker(mapView,kontext));
        double a=0;
        if(address  instanceof  com.search_location.Address)
        {
            a=(((com.search_location.Address) address ).getAltitude());
        }
        marker.setPosition(new GeoPoint(address.getLatitude(),address.getLongitude(),a));
        return marker;
    }
    }


