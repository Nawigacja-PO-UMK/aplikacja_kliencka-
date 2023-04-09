package com.example.nawigacja_po_umk.Nawigation_Fragment.ulunione;

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
import android.widget.Toast;

import com.example.nawigacja_po_umk.MainActivity;
import com.example.nawigacja_po_umk.Nawigation_Fragment.Nawigation.Nawigation;
import com.example.nawigacja_po_umk.R;
import com.search_location.Address;
import com.search_location.search_location;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;


public class save extends Fragment {


    public save() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ((MainActivity)getHost()).replace_fragment(new list_ulubione(),R.id.conteiner_save);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ((MainActivity)getHost()).replace_fragment(new list_ulubione(),R.id.conteiner_save);
        return inflater.inflate(R.layout.fragment_save, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view,savedInstanceState);
        ((MainActivity)getHost()).replace_fragment(new list_ulubione(),R.id.conteiner_save);
    }
}