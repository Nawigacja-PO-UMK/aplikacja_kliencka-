package com.example.nawigacja_po_umk.Nawigation_Fragment.Map_Building;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.nawigacja_po_umk.MainActivity;
import com.example.nawigacja_po_umk.Nawigation_Fragment.Map_Building.list_map;
import com.example.nawigacja_po_umk.R;


public class Map_buliding extends Fragment {




    public Map_buliding() {
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
        return inflater.inflate(R.layout.fragment_map_buliding, container, false);
    }
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        list_map list_map=new list_map();
        ((MainActivity) getHost()).replace_fragment(list_map, R.id.conteinter_Map_building);

    }

}