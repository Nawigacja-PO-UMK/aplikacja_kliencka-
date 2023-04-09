package com.example.nawigacja_po_umk.Activity;

import android.app.LauncherActivity;
import android.content.Context;
import android.location.Address;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;

import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nawigacja_po_umk.MainActivity;
import com.example.nawigacja_po_umk.Mapa;
import com.example.nawigacja_po_umk.R;
import com.example.nawigacja_po_umk.ekran_Tracking.screean_Tracking;
import com.search_location.search_location;

import java.util.ArrayList;
import java.util.List;


public class search extends Fragment {


String word;
ListView listView;
Mapa mapa;
screean_Tracking screean_tracking;
public search() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        word=(String) getArguments().get("word");
        mapa=(Mapa)getArguments().get("mapa");
        screean_tracking=(screean_Tracking) getArguments().get("tracking");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search, container, false);
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        listView= view.findViewById(R.id.list_item);
        if(mapa.getMapa_budynku()!=null)
            mapa.getMapa_budynku().add_tracking(word);
        else
            out_side(word);
    }
    public void  out_side(String nazwa)  {
        List<Address> address = search_location.search(nazwa,30);
        if(address.size()>0) {
            List<String> strings = new ArrayList<>();
            for (int i = 0; i < address.size(); i++) {
                strings.add(search_location.search_name_Adress(address.get(i)));
            }
            ArrayAdapter adapter = new ArrayAdapter<String>(((MainActivity)getHost()), R.layout.item_list,strings);

            listView.setAdapter(adapter);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    mapa.add_tracking(address.get(position));
                    tracking_Activity((Context) getHost(),mapa,screean_tracking);
                }
            });
        }
        else {
            if(mapa.tracking.getTrasa()==null)
                ((MainActivity)getHost()).replace_fragment(new Fragment(),R.id.conteiner);
            else
                tracking_Activity(getContext(),mapa,screean_tracking);
            Toast.makeText(((MainActivity) getHost()), "nie znaleziono", Toast.LENGTH_SHORT).show();
        }
    }

    static public void tracking_Activity(Context kontext ,Mapa mapa,screean_Tracking screean_tracking)
    {
        Fragment fragmentTracking=new trackingActivity();
        Bundle bundle= new Bundle();
        bundle.putSerializable("mapa", mapa);
        bundle.putSerializable("screan",screean_tracking);
        fragmentTracking.setArguments(bundle);
        ((MainActivity)kontext).replace_fragment(fragmentTracking,R.id.conteiner);
    }

}