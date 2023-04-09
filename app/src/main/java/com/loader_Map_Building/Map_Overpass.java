package com.loader_Map_Building;

import android.util.Log;

import org.osmdroid.bonuspack.location.OverpassAPIProvider;
import org.osmdroid.bonuspack.utils.BonusPackHelper;
import org.osmdroid.util.BoundingBox;

import java.net.URLEncoder;

public class Map_Overpass extends OverpassAPIProvider {
    @Override
    public String urlForTagSearchKml(String tag, BoundingBox bb, int limit, int timeout) {
        StringBuilder s = new StringBuilder();
        String tag1 = "level=" + tag;
        String tag2 = "level~" + "\"" +"^" + tag + "." + "\"";
        String tag3 = "level~" + "\"" + ".-" + tag + "$" + "\"";
        String tag4 = "level~" + "\"" + "^" + tag + "$" + "\"";

        s.append(mService + "?data=");
        String sBB = "(" + bb.getLatSouth() + "," + bb.getLonWest() + "," + bb.getLatNorth() + "," + bb.getLonEast() + ")";
        String data =
                "[out:json][timeout:" + timeout + "];" +
                        "(way[amenity]" + sBB +
                        ";way[" + tag1 + "] [!highway][room]" + sBB +
                        ";way[" + tag1 + "] [!highway][indoor]" + sBB +
                        ";way[" + tag2 + "] [highway=\"steps\"]" + sBB +
                        ";way[" + tag3 + "] [highway=\"steps\"]" + sBB +
                        ";way[" + tag4 + "] [highway=\"steps\"]" + sBB +
                        ";way[" + tag1 + "] [highway=elevator]" + sBB +";);"
                        + "out qt geom tags " + limit + ";"; //relation isolated to get geometry with body option
        //TODO: see issue https://github.com/drolbr/Overpass-API/issues/134#issuecomment-58847362
        //When solved, simplify.
        Log.d(BonusPackHelper.LOG_TAG, "data=" + data);
        s.append(URLEncoder.encode(data));
        return s.toString();
    }
}
