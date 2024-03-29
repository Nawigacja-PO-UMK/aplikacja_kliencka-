package com.search_location;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.osmdroid.bonuspack.location.POI;
import org.osmdroid.bonuspack.utils.BonusPackHelper;
import org.osmdroid.util.BoundingBox;
import org.osmdroid.util.GeoPoint;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * POI Provider using GeoNames services.
 * Currently, "find Nearby Wikipedia" and "Wikipedia Articles in Bounding Box" services.
 * @see <a href="http://www.geonames.org">GeoNames API</a>
 * @author M.Kergall
 */
public class GeoNamesPOIProvider {

    protected String mUserName;

    /**
     * @param account the registered "username" to give to GeoNames service.
     * @see <a href="http://www.geonames.org/login">GeoNames Account</a>
     */
    public GeoNamesPOIProvider(String account){
        mUserName = account;
    }

    private String getUrlCloseTo(GeoPoint p, int maxResults, double maxDistance){
        StringBuilder url = new StringBuilder("https://secure.geonames.org/findNearbyWikipediaJSON?");
        url.append("&lat="+p.getLatitude());
        url.append("&lng="+p.getLongitude());
        url.append("&maxRows="+maxResults);
        url.append("&radius="+maxDistance); //km
        url.append("&lang="+Locale.getDefault().getLanguage());
        url.append("&username="+mUserName);
        return url.toString();
    }

    private String getUrlInside(BoundingBox boundingBox, int maxResults){
        StringBuilder url = new StringBuilder("https://secure.geonames.org/wikipediaBoundingBoxJSON?");
        url.append("&south="+boundingBox.getLatSouth());
        url.append("&north="+boundingBox.getLatNorth());
        url.append("&east="+boundingBox.getLonEast());
        url.append("&west="+boundingBox.getLonWest());
        url.append("&maxRows="+maxResults);
        url.append("&lang=pl");
        url.append("&username="+mUserName);
        return url.toString();
    }

    /**
     * @param fullUrl
     * @return the list of POI
     */
    public ArrayList<POI> getThem(String fullUrl){
        try{
            String jString =  BonusPackHelper.requestStringFromUrl(fullUrl);
            if(jString==null)
                jString="";
            JSONObject jRoot = new JSONObject(jString);
            JSONArray jPlaceIds = jRoot.getJSONArray("geonames");
            int n = jPlaceIds.length();
            ArrayList<POI> pois = new ArrayList<POI>(n);
            for (int i=0; i<n; i++){
                JSONObject jPlace = jPlaceIds.getJSONObject(i);
                POI poi = new POI(POI.POI_SERVICE_GEONAMES_WIKIPEDIA);
                poi.mLocation = new GeoPoint(jPlace.getDouble("lat"),
                        jPlace.getDouble("lng"),
                        jPlace.optDouble("elevation", 0.0));
                poi.mCategory = jPlace.optString("feature");
                poi.mType = jPlace.getString("title");
                poi.mDescription = jPlace.optString("summary");
                poi.mThumbnailPath = jPlace.optString("thumbnailImg", null);
				if (poi.mThumbnailPath != null){
					poi.mThumbnail = BonusPackHelper.loadBitmap(poi.mThumbnailPath);
				}

                poi.mUrl = jPlace.optString("wikipediaUrl", null);
                if (poi.mUrl != null)
                    poi.mUrl = "http://" + poi.mUrl;
                poi.mRank = jPlace.optInt("rank", 0);
                pois.add(0,poi);
            }
            Log.d(BonusPackHelper.LOG_TAG, "done");
            return pois;
        }catch (JSONException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }


    public ArrayList<POI> getPOICloseTo(GeoPoint position,int maxResults, double maxDistance){
        String url = getUrlCloseTo(position, maxResults, maxDistance);
        return getThem(url);
    }

    public ArrayList<POI> getPOIInside(BoundingBox boundingBox, int maxResults){
        String url = getUrlInside(boundingBox, maxResults);
        return getThem(url);
    }
}
