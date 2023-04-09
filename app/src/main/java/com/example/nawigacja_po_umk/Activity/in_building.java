package com.example.nawigacja_po_umk.Activity;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nawigacja_po_umk.Mapa;
import com.example.nawigacja_po_umk.R;
import com.loader_Map_Building.Mapa_budynku;


public class in_building extends Fragment {


    Mapa_budynku mapa;
    public in_building() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mapa=(Mapa_budynku) getArguments().get("mapa");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.in_door, container, false);
    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Button up=view.findViewById(R.id.up);
        Button down=view.findViewById(R.id.down);
        TextView level= view.findViewById(R.id.textlevel);
        level.setTextSize(25);
        level.setText(String.valueOf(mapa.level()));

        up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mapa!=null && mapa.levelmax()>mapa.level()) {
                    mapa.wczytaj_nowa_mape(mapa.level() + 1);
                    level.setText(String.valueOf(mapa.level()));
                }
            }
        });

        down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mapa!=null && mapa.Levelmin()<mapa.level()) {
                    mapa.wczytaj_nowa_mape(mapa.level() - 1);
                    level.setText(String.valueOf(mapa.level()));
                }
            }
        });

    }
}