package com.example.nawigacja_po_umk.Nawigation_Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.nawigacja_po_umk.Mapa;
import com.example.nawigacja_po_umk.R;
import com.example.nawigacja_po_umk.ekran_Tracking.screean_Tracking;
import com.search_location.search_location;

import org.osmdroid.views.MapView;


public class Parking_Fragment extends Fragment {


    public Parking_Fragment() {
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
        return inflater.inflate(R.layout.fragment_parking_, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        MapView map_parking= (MapView) view.findViewById(R.id.map_parking);
        screean_Tracking screean_tracking=new screean_Tracking();
        Mapa mapa= new Mapa(getContext(),map_parking,screean_tracking);
        Button zaparkuj=(Button) view.findViewById(R.id.zaprkuj);
        zaparkuj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mapa.location.getMyLocation()!=null) {
                    search_location.my_car = mapa.location.getMyLocation();
                    Toast.makeText(getContext(), "Zaparkowany", Toast.LENGTH_SHORT).show();
                }
                else
                    Toast.makeText(getContext(), "Nieznana lokalizacja", Toast.LENGTH_SHORT).show();
            }
        });
    }
}