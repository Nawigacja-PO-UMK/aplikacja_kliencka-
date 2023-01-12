package com.example.nawigacja_po_umk;

import org.osmdroid.bonuspack.routing.Road;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.overlay.Polyline;

import java.util.ArrayList;

public class trasa {

    private ArrayList<Road> roads;
    private ArrayList<String> Tracking;
    int numer_trasy;
    int numer_etapu_trasy;
    public int Color;
    public Polyline polyline=null;
    public trasa(Road road,String newTracking)
    {
        this.roads=new ArrayList<Road>();
        this.Tracking=new ArrayList<String>();
        numer_etapu_trasy=0;
        numer_trasy=0;
        this.Color=0xFF0000FF;
        this.Tracking=new ArrayList<String>();
        add_trasa(road,newTracking);
    }

   public void add_trasa(Road road,String newTracking)
    {
        this.roads.add(road);
        this.Tracking.add(newTracking);
    }


    public String print_descryption()
    {
        String allinstructions=new String();
        int etap=numer_etapu_trasy;
        String instruction;
        for (int i =numer_trasy;i<roads.size();i++) {
            Road road= roads.get(i);
            for(int j=etap;j<road.mNodes.size();j++)
            {
                if (j==0) {
                    allinstructions += "zacznij na końcu\n\n";
                    continue;
                }
                if(j==road.mNodes.size()-1) {
                    allinstructions += "Dotarłeś do Punktu Docelowego\n\n";
                    continue;
                }
                allinstructions+="Za "+road.mNodes.get(j).mDuration+"m ";
                instruction= road.mNodes.get(j).mInstructions;
                if(instruction!=null | j!=road.mNodes.size()-2)
                    allinstructions+=instruction;
                else
                    allinstructions+="Powinineś zobaczyć punkt docelowy";
                allinstructions+="\n\n";
            }
            etap=0;
        }
        return allinstructions;
    }

    ArrayList<GeoPoint> print_point()
    {
        ArrayList<GeoPoint> pozostała_trasa=new ArrayList<GeoPoint>();
        int etap = numer_etapu_trasy;

        for (int i =numer_trasy;i<roads.size();i++) {
           Road road= roads.get(i);
           for(int j=etap;j<road.mNodes.size();j++)
           {
              pozostała_trasa.add(road.mNodes.get(j).mLocation);
           }
           etap=0;
        }
        return  pozostała_trasa;
    }

    void next()
    {
        if(roads.get(numer_trasy).mNodes.size()-1 > numer_etapu_trasy)
            numer_etapu_trasy++;
        else
            if(roads.size()-1> numer_trasy) {
                numer_etapu_trasy = 0;
                numer_trasy++;
            }
    }

    String print_name_tracking() {

        String tracking = new String();
        for (int i = 0; i < Tracking.size() ; i++)
            tracking+=Tracking.get(i)+"\t\t\t Czas:"+ roads.get(i).mDuration+"\t\t\t"+(roads.get(i).mLength*1000)+" m\n";
        return tracking;
    }

    Polyline polyline()
    {
        polyline=new Polyline();
        ArrayList<GeoPoint> points=print_point();
        for(int i=0;i<points.size();i++)
        {
            polyline.addPoint(points.get(i));
        }
        polyline.setWidth(7.0f);
        polyline.setColor(Color);
        return polyline;
    }

}
