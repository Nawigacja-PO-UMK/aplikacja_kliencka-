package com.example.nawigacja_po_umk.ekran_Tracking;

import android.widget.TextView;

import com.Tracking.trasy.trasa;

import java.io.Serializable;

public class screean_Tracking implements Serializable {

    TextView instruction;
    TextView tracking;

    public screean_Tracking(TextView instruction, TextView tracking)
    {
        this.instruction=instruction;
        this.tracking=tracking;
    }
    public screean_Tracking()
    {
     instruction=null;
     tracking=null;
    }

    public void  set(TextView instruction,TextView tracking)
    {
        this.instruction=instruction;
        this.tracking=tracking;
    }


    public void update_descryption(trasa trasa)
    {
        if(tracking!=null && instruction!=null) {
            if (trasa != null) {
                tracking.setBackgroundColor(0xFFFFFFFF);
                instruction.setBackgroundColor(0xFFFFFFFF);
                instruction.setAllCaps(true);
                instruction.setText(trasa.print_descryption());
                tracking.setText(trasa.print_name_tracking());
            } else {
                tracking.setBackgroundColor(0);
                instruction.setBackgroundColor(0);
                instruction.setText("");
                tracking.setText("");
            }
        }
    }
}
