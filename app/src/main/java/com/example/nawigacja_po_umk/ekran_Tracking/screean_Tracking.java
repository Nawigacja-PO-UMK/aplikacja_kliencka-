package com.example.nawigacja_po_umk.ekran_Tracking;

import android.widget.TextView;

import com.Tracking.trasy.trasa;

public class screean_Tracking {

    TextView instruction;
    TextView tracking;

    public screean_Tracking(TextView instruction, TextView tracking)
    {
        this.instruction=instruction;
        this.tracking=tracking;
    }

    public void update_descryption(trasa trasa)
    {
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
