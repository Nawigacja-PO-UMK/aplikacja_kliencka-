package com.example.nawigacja_po_umk;

import org.jetbrains.annotations.NotNull;
import org.osmdroid.bonuspack.kml.KmlFeature;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

public class Add_marker {

    public static Item seach_item(@NotNull String name, KmlFeature[][] items)
    {
        for (int j=0;j< items.length;j++) {
            for (int i = 0; i < items[j].length; i++) {
                if (items[j][i].mName != null && items[j][i].mName.equals(name)) {
                   Item item=new Item(items[j][i],j);
                    return item;
                }
            }
        }
     return null;
    }
    public static Marker Add_marker(KmlFeature item, MapView mapView,String opis)
    {
        Marker marker =new Marker(mapView);
        marker.setPosition(item.getBoundingBox().getCenter());
        marker.setTitle(opis);
        return marker;
    }

}
    class Item
    {
        KmlFeature item;
        int level;
                Item(KmlFeature item,int level)
                {
                    this.item=item;
                    this.level=level;
                }
    }
