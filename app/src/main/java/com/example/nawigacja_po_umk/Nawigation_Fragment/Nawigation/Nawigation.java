package com.example.nawigacja_po_umk.Nawigation_Fragment.Nawigation;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.nawigacja_po_umk.Activity.in_building;
import com.example.nawigacja_po_umk.Activity.search;
import com.example.nawigacja_po_umk.Activity.trackingActivity;
import com.example.nawigacja_po_umk.MainActivity;
import com.example.nawigacja_po_umk.Mapa;
import com.example.nawigacja_po_umk.R;
import com.example.nawigacja_po_umk.ekran_Tracking.screean_Tracking;
import com.loader_Map_Building.Mapa_budynku;
import com.search_location.Address;

import org.osmdroid.util.BoundingBox;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;

import java.util.ArrayList;

public class Nawigation extends Fragment {

    MapView mapView;
    Mapa mapa;
    static public ArrayList<Address> tracking=new ArrayList<>();
    public Nawigation() {
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
        mapa=((MainActivity)getHost()).mapa;
        ((MainActivity) getHost()).screean_tracking = new screean_Tracking();
        this.mapView = view.findViewById(R.id.map);
        if (((MainActivity) getHost()).mapa == null) {
            ((MainActivity) getHost()).mapa = new Mapa((Context) getHost(), mapView, ((MainActivity) getHost()).screean_tracking);
        }
        else
        {
            ((MainActivity) getHost()).mapa.newInstance(mapView,((MainActivity) getHost()).screean_tracking);
        }
        if(getArguments()!=null)
        {
           mapView.getController().setCenter(new GeoPoint(getArguments().getDouble("x"),
                   getArguments().getDouble("y")));
            BoundingBox box=new BoundingBox();
            box.set(getArguments().getDouble("n"),getArguments().getDouble("e"),getArguments().getDouble("s"),getArguments().getDouble("w"));
            Mapa_budynku mapa_budynku=new Mapa_budynku(getContext(),box,mapView,((MainActivity) getHost()).screean_tracking);
            mapa_budynku.wczytywanie_mapy(0);
            Bundle bundle=new Bundle();
            bundle.putSerializable("mapa",mapa_budynku);
            in_building in_door= new in_building();
            in_door.setArguments(bundle);
            ((MainActivity)getHost()).replace_fragment(in_door,R.id.conteiner);
            mapa.location.seting_center=true;
            Fragment fragment=new Fragment();
            ((MainActivity)getHost()).replace_fragment(fragment,R.id.inferface_nawigation);
        }
        else
        {
            interface_nawigation interface_nawigation=new interface_nawigation();
            interface_nawigation.mapView=mapView;
            ((MainActivity)getHost()).replace_fragment(interface_nawigation,R.id.inferface_nawigation);
            if(tracking.size()>0)
            {
                for(int i=0;i<tracking.size();i++)
                    mapa.add_tracking(tracking.get(i));

                search.tracking_Activity(getContext(),mapa,((MainActivity) getHost()).screean_tracking);
                tracking=new ArrayList<>();
            }
        }

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