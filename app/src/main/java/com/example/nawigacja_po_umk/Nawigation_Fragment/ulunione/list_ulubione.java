package com.example.nawigacja_po_umk.Nawigation_Fragment.ulunione;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

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
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.search_location.Address;
import com.search_location.search_location;

import org.json.JSONArray;
import org.json.JSONException;
import org.osmdroid.util.GeoPoint;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class list_ulubione extends Fragment {

    static List<like> ulubione = new ArrayList();
    static private SharedPreferences plik;
    final static private String file_save = "save_like";

    public list_ulubione() {
    }


    public static list_ulubione newInstance(String param1, String param2) {
        list_ulubione fragment = new list_ulubione();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_list_ulubione, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ListView listlocation = (ListView) view.findViewById(R.id.save_location);
        actualization_list(listlocation);
        listlocation.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Nawigation.tracking.add((Address) search_location.search(new GeoPoint(ulubione.get(position).X, ulubione.get(position).Y)
                        , 1).get(0));
                Toast.makeText(getContext(), "Dodano do Nawigacji ", Toast.LENGTH_SHORT).show();
            }
        });
        listlocation.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                ulubione.remove(position);
                save(getContext());
                actualization_list(listlocation);
                return true;
            }
        });
    }

    private void actualization_list(ListView listlocation)
    {
        List<String> names = new ArrayList<>();
        reade(getContext());
        if (ulubione != null)
            for (int i=0 ;i< ulubione.size();i++) {
                names.add(ulubione.get(i).Name);
            }
        listlocation.setAdapter(new ArrayAdapter<String>(getContext(), R.layout.item_map, names));
    }
    public static void add_save_like(Address address, Context kontext)
    {
        reade(kontext);
        if (ulubione == null)
            ulubione = new ArrayList<>();
        like like=new like(address.getLatitude(),address.getLongitude(),search_location.search_name_Adress(address));
        ulubione.add(like);
        save(kontext);
    }

    public static void save(Context kontext) {
        plik = kontext.getSharedPreferences(file_save, Context.MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = plik.edit();
        Gson gson = new Gson();
        String json = gson.toJson(ulubione);
        prefsEditor.putString("like", json);
        prefsEditor.commit();
    }

    private static void reade(Context kontext) {
        plik = kontext.getSharedPreferences(file_save, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = plik.getString("like", "");
        try {
            JSONArray jsonArray= new JSONArray(json);
            ulubione=new ArrayList<>();
            for(int i=0;i<jsonArray.length();i++) {
               ulubione.add(gson.fromJson(jsonArray.getString(i),like.class));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}