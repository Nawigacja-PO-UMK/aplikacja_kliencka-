package com.example.nawigacja_po_umk.Nawigation_Fragment;

import android.annotation.SuppressLint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.nawigacja_po_umk.MainActivity;
import com.example.nawigacja_po_umk.Mapa;
import com.example.nawigacja_po_umk.R;

import org.mapsforge.map.layer.download.tilesource.TileSource;
import org.osmdroid.tileprovider.tilesource.BitmapTileSourceBase;
import org.osmdroid.tileprovider.tilesource.ITileSource;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.views.MapView;


public class mapy_fragment extends Fragment {



    public mapy_fragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_mapy_fragment, container, false);
    }
    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        ImageView map1=view.findViewById(R.id.map1);
        setTitle(map1,getContext().getDrawable(R.drawable.mapnik),TileSourceFactory.MAPNIK,"Mapnik openstreet map");
        ImageView map4=view.findViewById(R.id.map4);
        setTitle(map4,getContext().getDrawable(R.drawable.mapbox),Mapa.TileSource_Mapbox(),"Map Box street");
        ImageView map3=view.findViewById(R.id.map3);
        setTitle(map3,getContext().getDrawable(R.drawable.wikimedia),TileSourceFactory.WIKIMEDIA,"Wikimedia");
        ImageView map2=view.findViewById(R.id.map2);
        setTitle(map2,getContext().getDrawable(R.drawable.chartbundlewac),TileSourceFactory.ChartbundleWAC,"ChartbundleWAC");
    }
    private void setTitle(ImageView imageView, Drawable drawable, ITileSource tileSource,String Name)
    {
        imageView.setImageDrawable(drawable);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Mapa.tileSource=tileSource;
                Toast.makeText(getContext(), "Zmieniono kafelki na "+Name, Toast.LENGTH_SHORT).show();
            }
        });

    }
}