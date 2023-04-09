package com.example.nawigacja_po_umk.Activity;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.nawigacja_po_umk.MainActivity;
import com.example.nawigacja_po_umk.Mapa;
import com.example.nawigacja_po_umk.R;
import com.example.nawigacja_po_umk.ekran_Tracking.screean_Tracking;

public class trackingActivity extends Fragment {

    private Mapa mapa;
    TextView instruction;
    TextView tracking;
    Button remove;
    screean_Tracking screean_tracking;
    View view;
    FragmentManager fragmentManager;


    public trackingActivity() {

    }

    private void seting()
    {
        instruction = (TextView) view.findViewById(R.id.instruction);
        tracking = (TextView) view.findViewById(R.id.tracking);
        remove = (Button) view.findViewById(R.id.remove);
        tracking.setMovementMethod(new ScrollingMovementMethod());
        instruction.setMovementMethod(new ScrollingMovementMethod());
        screean_tracking.set(instruction, tracking);
    }

    public void removeTracking(View view)
    {
        if(mapa.getMapa_budynku()!=null)
            mapa.getMapa_budynku().remove_tracking();
        else
            mapa.remove_tracking();
        ((MainActivity)getHost()).replace_fragment(new Fragment(),R.id.conteiner);
    }




    private void update_descryption()
    {
        com.Tracking.trasy.trasa trasa;
        if(mapa.getMapa_budynku()!=null) {
            trasa = mapa.getMapa_budynku().get_trasa();
        }
        else
            trasa=mapa.getTracking().trasa;
        if(trasa!= null) {
            tracking.setBackgroundColor(0xFFFFFFFF);
            instruction.setBackgroundColor(0xFFFFFFFF);
            instruction.setAllCaps(true);
            instruction.setText(trasa.print_descryption());
            tracking.setText(trasa.print_name_tracking());
        }
        else
        {
            tracking.setBackgroundColor(0);
            instruction.setBackgroundColor(0);
            instruction.setText("");
            tracking.setText("");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
                mapa=(Mapa) getArguments().get("mapa");
            screean_tracking=(screean_Tracking)getArguments().get("screan");
            fragmentManager=(FragmentManager) getArguments().get("fragmentMenager");
    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.view=view;
        seting();
        update_descryption();
        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeTracking(v);
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.tracking, container, false);

    }
}