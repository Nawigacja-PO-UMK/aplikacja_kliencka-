package com.example.nawigacja_po_umk;

import org.osmdroid.bonuspack.routing.Road;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.overlay.Polyline;

import java.util.ArrayList;

public class trasa {

    private ArrayList<Road> roads;
    int numer_trasy;
    int numer_etapu_trasy;
    public int Color;
    public trasa(Road road)
    {
        this.roads=new ArrayList<Road>();
        this.roads.add(road);
        numer_etapu_trasy=0;
        numer_trasy=0;
        this.Color=0xFF0000FF;
    }

   public void add_trasa(Road road)
    {
        this.roads.add(road);
    }


    public String print_descryption()
    {
        String allinstructions=new String();
        int etap=numer_etapu_trasy;
        for (int i =numer_trasy;i<roads.size();i++) {
            Road road= roads.get(i);
            for(int j=etap;j<road.mNodes.size();j++)
            {
                allinstructions+="Za "+road.mNodes.get(j).mDuration+"m "+road.mNodes.get(j).mInstructions+""+"\n\n";
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

    Polyline polyline()
    {
        Polyline polyline=new Polyline();
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
