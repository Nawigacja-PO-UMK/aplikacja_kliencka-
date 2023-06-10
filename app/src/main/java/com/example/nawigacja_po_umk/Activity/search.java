package com.example.nawigacja_po_umk.Activity;

import android.app.SearchManager;
import android.content.Context;
import android.location.Address;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.Tracking.activity_Tracking;
import com.example.nawigacja_po_umk.MainActivity;
import com.example.nawigacja_po_umk.Mapa;
import com.example.nawigacja_po_umk.R;
import com.example.nawigacja_po_umk.ekran_Tracking.screean_Tracking;
import com.search_location.Item;
import com.search_location.search_location;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;


public class search extends Fragment {


String word;
ListView listView;
Mapa mapa;
screean_Tracking screean_tracking;
static in_building in_door=new in_building();

public search() {

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
        if(mapa.getMapa_budynku()!=null) {
            ((MainActivity)getHost()).replace_fragment(new Fragment(),R.id.in_door);
            add_list_Adress(Adapter_Adress_search(word),mapa.getMapa_budynku().tracking_buliding);
        }
        else
            add_list_Adress(search_location.search(word,30),mapa.tracking);
    }

    private List<Address> Adapter_Adress_search(String word)
    {
        List<Address> addresses= new ArrayList<>();
        List<Item> items = search_location.search_in_building(word,mapa.getMapa_budynku().get_item());
        for (Item item:items) {
                Locale locale = new Locale("PL");
                com.search_location.Address address = new com.search_location.Address(locale);
                address.setFeatureName(item.item.mName + " na poziomie " + String.valueOf(item.level + mapa.getMapa_budynku().Levelmin()));
                address.setLatitude(item.item.getBoundingBox().getCenterLatitude());
                address.setLongitude(item.item.getBoundingBox().getCenterLongitude());
                address.setAltitude(item.level + mapa.getMapa_budynku().Levelmin());
                addresses.add(address);

        }

        addresses.sort(new Comparator<Address>() {
            @Override
            public int compare(Address o1, Address o2) {
                return odległość(o2.getFeatureName().toLowerCase(),word.toLowerCase())-odległość(o1.getFeatureName().toLowerCase(),word.toLowerCase());
            }
        });
        return addresses;
    }
    private int  odległość(String s1,String s2)
    {
        int max=0;
        for(int i=0;i<s2.length();i++)
        {
            int tmp=0;
            int h=i;
            for(int j=0;j<s1.length();j++)
            {

                if(s2.getBytes()[i]==s1.getBytes()[j])
                {
                 tmp++;
                 if(s2.length()>(i+1))
                 i++;
                }
                else {
                    if (max < tmp)
                        max = tmp;
                    i-=tmp;
                    tmp=0;
                    if(i<0) i=0;
                }
        }
        i=h;
        }

        return max;
    }


    public void add_list_Adress(List<Address> address, activity_Tracking activity_tracking)  {
        if(address.size()>0) {
            List<String> strings = new ArrayList<>();
            for (int i = 0; i < address.size(); i++) {
                strings.add(search_location.search_name_Adress(address.get(i)));
            }
            ArrayAdapter adapter = new ArrayAdapter<String>(((MainActivity)getHost()), R.layout.item_list, strings);
            listView.setAdapter(adapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    activity_tracking.add_tracking(address.get(position));
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
        if(mapa.getMapa_budynku()!=null) {
            Bundle bundle_indor =new Bundle();
            bundle_indor.putSerializable("mapa",mapa.getMapa_budynku());
            in_door.setArguments(bundle_indor);
            ((MainActivity) kontext).replace_fragment(in_door, R.id.in_door);
        }
        Fragment fragmentTracking=new trackingActivity();
        Bundle bundle= new Bundle();
        bundle.putSerializable("mapa", mapa);
        bundle.putSerializable("screan",screean_tracking);
        fragmentTracking.setArguments(bundle);
        ((MainActivity)kontext).replace_fragment(fragmentTracking,R.id.conteiner);

    }
}