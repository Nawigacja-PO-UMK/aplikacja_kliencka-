package com.example.nawigacja_po_umk;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;

import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.Text_convert_voice;
import com.Tracking.trasy.trasa;
import com.locaton_Wifi.Pozycjonowanie;
import com.Tracking.trasy.trasa_outside;

import org.osmdroid.config.Configuration;

import java.io.IOException;
import java.net.MalformedURLException;


public class MainActivity extends AppCompatActivity{

    private Context context;
    Pozycjonowanie pozycja;
    EditText room;
    Mapa mapa;
    TextView instruction;
    TextView tracking;
    Button remove;
    @SuppressLint("MissingInflatedId")
    @Override protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Context ctx = getApplicationContext();
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));
        setContentView(R.layout.activity_main);
        room=findViewById(R.id.editTextTextPersonName);
        context = this;
        instruction= (TextView) findViewById(R.id.instruction);
        tracking=(TextView) findViewById(R.id.tracking);
        remove=(Button)findViewById(R.id.remove);
        tracking.setMovementMethod(new ScrollingMovementMethod());
                ///pozycjonowanie

        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PackageManager.PERMISSION_GRANTED);
        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, PackageManager.PERMISSION_GRANTED);

        mapa= new Mapa(context,findViewById(R.id.map));
        try {
            pozycja = new Pozycjonowanie(context);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        //uruchomie jak bedziemy mieli serwer i skończony model od marcina
       /// pozycja.odczytaj_pozycje(mapa.loadKml.znacznik);
    }

    public void removeTracking(View view)
    {
        if(mapa.getMapa_budynku()!=null)
            mapa.getMapa_budynku().remove_tracking();
        else
            mapa.remove_tracking();
        update_descryption();
    }


    @SuppressLint("SuspiciousIndentation")
    public void LewelUpMap(View view)
    {
        if(mapa.getMapa_budynku()!=null && mapa.getMapa_budynku().levelmax()>mapa.getMapa_budynku().level()) {
            mapa.getMapa_budynku().wczytaj_nowa_mape(mapa.getMapa_budynku().level() + 1);
            update_descryption();
        }
    }

    public void LewelDownMap(View view)
    {
        if(mapa.getMapa_budynku()!=null && mapa.getMapa_budynku().Levelmin()<mapa.getMapa_budynku().level()) {
            mapa.getMapa_budynku().wczytaj_nowa_mape(mapa.getMapa_budynku().level() - 1);
            update_descryption();
        }
    }

    public void trasowanie(View view) throws IOException {



        if(mapa.getMapa_budynku()!=null)
            mapa.getMapa_budynku().add_tracking(room.getText().toString());
        else
            mapa.add_tracking(room.getText().toString());
    instruction.setMovementMethod(new ScrollingMovementMethod());
    update_descryption();
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
}