package com.search_location;

import org.osmdroid.bonuspack.kml.KmlFeature;

public class Item {
    public KmlFeature item;
    public int level;

    public Item(KmlFeature item, int level) {
        this.item = item;
        this.level = level;
    }
}
