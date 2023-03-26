package com.example.nawigacja_po_umk.ekranMarker;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.example.nawigacja_po_umk.Activity.trackingActivity;
import com.example.nawigacja_po_umk.MainActivity;
import com.example.nawigacja_po_umk.R;
import com.search_location.search_location;

import org.osmdroid.bonuspack.location.POI;
import org.osmdroid.util.BoundingBox;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class galeria extends DialogFragment {

    ImageView imageView;
    BoundingBox boundingBox;
    List<POI> galeria;
    int index;
    galeria(POI main, BoundingBox boundingBox)
    {
        this.imageView=imageView;
        this.boundingBox=boundingBox;
        galeria=search_location.search_flickr(5,boundingBox,true);
        if(not_exist(galeria,main))
        {
            byte[] url_tmp=main.mThumbnailPath.getBytes(StandardCharsets.UTF_8);
            int length=url_tmp.length;
            url_tmp[length-5]='B';
            POI main_big= new POI(POI.POI_SERVICE_FLICKR);
            main_big.mThumbnailPath=new String(url_tmp);
            main_big.mThumbnail=null;

            if(galeria==null) {
                galeria = new ArrayList<POI>();
            }
                galeria.add(0, main_big);
        }
        index=0;
    }
    private boolean not_exist(List<POI> pois,POI poi)
    {
        if(pois!=null) {
            for (int i = 0; i < pois.size(); i++) {
                if (pois.get(i).mId == poi.mId) {
                    poi = pois.get(i);
                    pois.remove(i);
                    pois.add(0, poi);
                    return false;
                }
            }
            return true;
        }
        else
            return false;
    }

    @SuppressLint("SuspiciousIndentation")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.galeria, container, false);
        if(galeria!=null) {
            ImageView Image_show = (ImageView) view.findViewById(R.id.image_galeria);
            Image_show.setOnTouchListener(new View.OnTouchListener() {
                protected double x;
                protected double y;

                @Override
                public boolean onTouch(View v, MotionEvent event) {

                    if (event.getAction() == MotionEvent.ACTION_MOVE) {
                        x = event.getX();
                        y = event.getY();
                    }
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        if (x < event.getX()) {
                            if (index < galeria.size() - 1) {
                                index++;
                                Image_show.setImageBitmap(galeria.get(index).getThumbnail());
                            }
                        }
                        if (x > event.getX()) {
                            if (index > 0) {
                                index--;
                                Image_show.setImageBitmap(galeria.get(index).getThumbnail());
                            }
                        }
                    }
                    return true;
                }

            });
            Image_show.setImageBitmap(galeria.get(index).getThumbnail());
        }
        else
        Toast.makeText(view.getContext(), "galeria jest nie dostępna", Toast.LENGTH_SHORT).show();

        return view;
    }

}
