package com.example.nawigacja_po_umk.Nawigation_Fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.wifi.rtt.WifiRttManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nawigacja_po_umk.Mapa;
import com.example.nawigacja_po_umk.R;
import com.example.nawigacja_po_umk.ekran_Tracking.screean_Tracking;
import com.search_location.search_location;

import org.osmdroid.views.MapView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link time#newInstance} factory method to
 * create an instance of this fragment.
 */
public class time extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public time() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment time.
     */
    // TODO: Rename and change types and number of parameters
    public static time newInstance(String param1, String param2) {
        time fragment = new time();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_time, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView search_wifi=view.findViewById(R.id.wifi);

        search_wifi.setText(String.valueOf(getContext().getPackageManager().hasSystemFeature(PackageManager.FEATURE_WIFI_RTT)));


        WifiRttManager wifiRttManager= (WifiRttManager) getContext().getSystemService(Context.WIFI_RTT_RANGING_SERVICE);

        search_wifi.setText(String.valueOf(wifiRttManager.isAvailable()));
        IntentFilter filter =
                new IntentFilter(WifiRttManager.ACTION_WIFI_RTT_STATE_CHANGED);
        BroadcastReceiver myReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {


                Toast.makeText(context, "dział", Toast.LENGTH_SHORT).show();
            }
        };
        getContext().registerReceiver(myReceiver, filter);



    }
}