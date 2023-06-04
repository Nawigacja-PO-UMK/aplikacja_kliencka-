package com.example.nawigacja_po_umk.Nawigation_Fragment.Map_Building;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.nawigacja_po_umk.Dowloader_list_map;
import com.example.nawigacja_po_umk.MainActivity;
import com.example.nawigacja_po_umk.Nawigation_Fragment.Nawigation.Nawigation;
import com.example.nawigacja_po_umk.R;

import org.osmdroid.util.BoundingBox;

import java.util.ArrayList;
import java.util.List;


public class list_map extends Fragment {

    public list_map() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_list_map, container, false);
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ListView maps = view.findViewById(R.id.Map_list_item);
        maps.setAdapter(new ArrayAdapter<String>(((MainActivity) getHost()), R.layout.item_map, Dowloader_list_map.getList_name_map()));
        ///lokaclizacja mapy
        maps.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Nawigation nawigation = new Nawigation();
                Bundle bundle=new Bundle();
                BoundingBox box=Dowloader_list_map.get_list().get(position);
                bundle.putDouble("x",box.getCenterLatitude());
                bundle.putDouble("y",box.getCenterLongitude());
                bundle.putDouble("n",box.getLatNorth());
                bundle.putDouble("s",box.getLatSouth());
                bundle.putDouble("e",box.getLonEast());
                bundle.putDouble("w",box.getLonWest());
                nawigation.setArguments(bundle);
                ((MainActivity) getHost()).replace_fragment(nawigation, R.id.conteinter_Map_building);
            }
        });
    }

}