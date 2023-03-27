package com.example.nawigacja_po_umk.Nawigation_Fragment;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.nawigacja_po_umk.Activity.search;
import com.example.nawigacja_po_umk.MainActivity;
import com.example.nawigacja_po_umk.Mapa;
import com.example.nawigacja_po_umk.R;
import com.example.nawigacja_po_umk.ekranMarker.ekran_marker;
import com.example.nawigacja_po_umk.ekran_Tracking.screean_Tracking;
import com.search_location.search_location;

import org.osmdroid.bonuspack.location.POI;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Overlay;

import java.io.IOException;
import java.io.Serializable;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

public class Nawigation extends Fragment {

    MapView mapView;
    EditText room;
    Button search;
    Button go;

    public Nawigation() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_nawigation, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ((MainActivity) getHost()).screean_tracking = new screean_Tracking();
        this.mapView = view.findViewById(R.id.map);
        if (((MainActivity) getHost()).mapa == null) {
            ((MainActivity) getHost()).mapa = new Mapa((Context) getHost(), mapView, ((MainActivity) getHost()).screean_tracking);
        }
        else
        {
            ((MainActivity) getHost()).mapa.newInstance(mapView,((MainActivity) getHost()).screean_tracking);
        }
        room = view.findViewById(R.id.editTextTextPersonName);
        search = view.findViewById(R.id.search_around);
        go = view.findViewById(R.id.go);
        go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    trasowanie(v);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search_around(v);
            }
        });
    }


    public void search_around(View view) {
        List<POI> POIs = null;
        POIs = search_location.search_flickr(10, mapView.getBoundingBox(), false);
        Comparator comparator = new Comparator() {
            @Override
            public int compare(Object o1, Object o2) {
                return ((POI) o2).mRank - ((POI) o1).mRank;
            }

            @Override
            public boolean equals(Object obj) {
                return false;
            }
        };
        POIs.sort(comparator);
        for (int i = 0; i < POIs.size(); i++) {
            Marker marker = new Marker(mapView);
            marker.setPosition(POIs.get(i).mLocation);
            if (POIs.get(i).mThumbnailPath != null) {
                marker.setIcon(new BitmapDrawable(POIs.get(i).getThumbnail()));
                marker.setImage(new BitmapDrawable(POIs.get(i).getThumbnail()));
            }
            marker.setTitle(POIs.get(i).mType);
            marker.setSnippet(POIs.get(i).mDescription);
            marker.setRelatedObject(POIs.get(i));
            marker.setInfoWindow(new ekran_marker(mapView, getContext()));
            mapView.getOverlays().add(marker);
        }

    }

    public void trasowanie(View view) throws IOException {
        Fragment fragment = new search();
        Bundle bundle = new Bundle();
        bundle.putString("word", room.getText().toString());
        bundle.putSerializable("mapa", ((MainActivity) getHost()).mapa);
        bundle.putSerializable("tracking", ((MainActivity) getHost()).screean_tracking);
        fragment.setArguments(bundle);
        ((MainActivity) getHost()).replace_fragment(fragment);
    }

    @Override
    public void onResume() {
        mapView.onResume();
        super.onResume();
    }

    @Override
    public void onPause() {
        mapView.onPause();
        super.onPause();
    }
    @Override
    public void onDetach()
    {
        mapView.onDetach();
        super.onDetach();
    }


}