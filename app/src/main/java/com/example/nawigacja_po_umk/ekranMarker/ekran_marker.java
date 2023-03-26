package com.example.nawigacja_po_umk.ekranMarker;


import static androidx.core.content.ContextCompat.startActivity;
import static com.google.android.material.internal.ContextUtils.getActivity;
import static com.search_location.search_location.search;
import static com.search_location.search_location.search_name_Adress;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.preference.DialogPreference;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.nawigacja_po_umk.Activity.search;
import com.example.nawigacja_po_umk.MainActivity;
import com.example.nawigacja_po_umk.R;
import com.search_location.Address;
import com.search_location.search_location;

import org.osmdroid.bonuspack.location.POI;
import org.osmdroid.util.BoundingBox;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.infowindow.MarkerInfoWindow;

import java.io.IOException;
import java.util.List;

public class ekran_marker extends  MarkerInfoWindow {

    Button btn;
    POI  mSelectedPoi;
    Address address;
    TextView title;
    TextView descryption,subdescryption;
    TextView Phone,open;
    TextView facebook,url;
    Button trasing;
    Context kontext;
    ImageView imageView;
    Marker marker;
    FragmentManager fragmentManager;
    DialogFragment dialogFragment;

    @SuppressLint("RestrictedApi")
    public ekran_marker(MapView mapView, Context kontext) {
        super(R.layout.window_marker, mapView);
        this.kontext = kontext;
        this.title = (TextView) mView.findViewById(R.id.title);
        this.descryption = (TextView) mView.findViewById(R.id.bubble_description);
        this.imageView = (ImageView) mView.findViewById(R.id.bubble_image);
        this.url=(TextView)mView.findViewById(R.id.url);
        this.Phone=(TextView) mView.findViewById(R.id.phon);
        this.subdescryption=(TextView) mView.findViewById(R.id.bubble_subdescription);
        this.facebook=(TextView)mView.findViewById(R.id.facebook);
        this.open=(TextView)mView.findViewById(R.id.open);
        this.trasing=(Button) mView.findViewById(R.id.trasing);
        this.fragmentManager=((AppCompatActivity)getActivity(kontext)).getSupportFragmentManager();
        mView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                if(mSelectedPoi!=null)
                mSelectedPoi.fetchThumbnailOnThread(imageView);
            }
        });
        trasing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    ((MainActivity)kontext).mapa.add_tracking(address);
                search.tracking_Activity(kontext,((MainActivity)kontext).mapa,((MainActivity)kontext).screean_tracking);
            }
        });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    dialogFragment=new galeria(mSelectedPoi,boundingBox_converter_Geopoint(mSelectedPoi.mLocation));
                    dialogFragment.show(fragmentManager, "galeria");
            }
        });
    }
    @Override public void onOpen(Object item) {

        marker = (Marker) item;
        if (marker.getRelatedObject() instanceof POI) {
            mSelectedPoi = (POI) marker.getRelatedObject();
            address = (Address) search_location.search(mSelectedPoi.mLocation, 1).get(0);
        } else if (marker.getRelatedObject() instanceof Address) {
            address = (Address) marker.getRelatedObject();
            List<POI>tmp =search_location.search_flickr(1,
                    boundingBox_converter_Geopoint(new GeoPoint(address.getLatitude(),address.getLongitude())),false);
            if(tmp.size()>0)
                mSelectedPoi=tmp.get(0);
        }

        String subdesctyption="";
        List<POI> pois;
        if(mSelectedPoi!=null)
           pois=search_location.search(1,mSelectedPoi.mLocation,0.03);
        else
            pois=search_location.search(1,new GeoPoint(address.getLatitude(),address.getLongitude()),0.03);

           if(pois.size()>0)
            {
                if(pois.get(0).mType!=null)
                    title.setText(pois.get(0).mType);
                else
                    title.setText(search_name_Adress(address));

                marker.setSnippet(pois.get(0).mDescription);
            }
            else
                title.setText(search_name_Adress(address));
        for(int i=0;i<address.getMaxAddressLineIndex();i++)
        {
            subdesctyption+=address.getAddressLine(i);
        }
        marker.setSubDescription(subdesctyption);
        if(address.getPhone()!=null) {
            Phone.setText("Phone:" + address.getPhone());
            Phone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse("tel:"+address.getPhone()));
                    kontext.startActivity(intent);

                }
            });
        }
        if(address instanceof com.search_location.Address)
        {
            if(((com.search_location.Address)address).getOpen_closs()!=null)
                open.setText("open:"+((com.search_location.Address)address).getOpen_closs());
            if(((com.search_location.Address)address).getFacebook()!=null)
                facebook.setText("Facebook:"+((com.search_location.Address)address).getFacebook());
        }
        if(address.getUrl()!=null) {
            url.setText("Url:" + address.getUrl());
            url.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(address.getUrl()));
                    kontext.startActivity(intent);
                }
            });
        }
            super.onOpen(item);
    }


    private BoundingBox boundingBox_converter_Geopoint(GeoPoint point)
    {
        BoundingBox boundingBox=new BoundingBox(
                point.getLatitude()+0.0005,
                point.getLongitude()+0.0005,
                point.getLatitude()-0.0005,
                point.getLongitude()-0.0005
        );
        return  boundingBox;
    }

}
