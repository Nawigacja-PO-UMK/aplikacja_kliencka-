package com.search_location;


import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.osmdroid.bonuspack.location.POI;
import org.osmdroid.bonuspack.utils.BonusPackHelper;
import org.osmdroid.util.BoundingBox;
import org.osmdroid.util.GeoPoint;

import java.util.ArrayList;

/**
 * POI Provider using Flickr service to get geolocalized photos.
 * @see <a href="http://www.flickr.com/services/api/flickr.photos.search.html">Flickr API</a>
 * @author M.Kergall
 */
public class FlickrPOIProvider {

    protected String mApiKey;

    /**
     * @param apiKey the registered API key to give to Flickr service.
     * @see <a href="http://www.flickr.com/help/api/">Flickr registration</a>
     */
    public FlickrPOIProvider(String apiKey){
        mApiKey = apiKey;
    }

    private String getUrlInside(BoundingBox boundingBox, int maxResults,String extras){
        StringBuilder url = new StringBuilder("https://api.flickr.com/services/rest/?method=flickr.photos.search");
        url.append("&api_key="+mApiKey);
        url.append("&bbox="+boundingBox.getLonWest());
        url.append(","+boundingBox.getLatSouth());
        url.append(","+boundingBox.getLonEast());
        url.append(","+boundingBox.getLatNorth());
        url.append("&has_geo=1");
        url.append("&format=json&nojsoncallback=1");
        url.append("&per_page="+maxResults);

        //From Flickr doc: "Geo queries require some sort of limiting agent in order to prevent the database from crying."
        //And min_date_upload is considered as a limiting agent. So:
        url.append("&min_upload_date=2005/01/01");

        //Ask to provide some additional attributes we will need:
        url.append("&extras=geo,"+extras);
        url.append("&sort=interestingness-desc");
        return url.toString();
    }


	public POI getPhoto(String photoId) {
        String url = "https://api.flickr.com/services/rest/?method=flickr.photos.getInfo"
                + "&api_key=" + mApiKey
                + "&photo_id=" + photoId
                + "&format=json&nojsoncallback=1"
                +"&extras=geo,url_l";
		Log.d(BonusPackHelper.LOG_TAG, "getPhoto:"+url);
		String jString = BonusPackHelper.requestStringFromUrl(url);
		if (jString == null) {
			Log.e(BonusPackHelper.LOG_TAG, "FlickrPOIProvider: request failed.");
			return null;
		}
		try {
			POI poi = new POI(POI.POI_SERVICE_FLICKR);
			JSONObject jRoot = new JSONObject(jString);
			JSONObject jPhoto = jRoot.getJSONObject("photo");
			JSONObject jLocation = jPhoto.getJSONObject("location");
			poi.mLocation = new GeoPoint(
					jLocation.getDouble("latitude"),
					jLocation.getDouble("longitude"));
			poi.mId = Long.parseLong(photoId);
			JSONObject jTitle = jPhoto.getJSONObject("title");
			poi.mType = jTitle.getString("_content");
			JSONObject jDescription = jPhoto.getJSONObject("description");
			poi.mDescription = jDescription.getString("_content");
			if (poi.mDescription.length() > 300){
				poi.mDescription = poi.mDescription.substring(0, 300) + " (...)";
			}
			String farm = jPhoto.getString("farm");
			String server = jPhoto.getString("server");
			String secret = jPhoto.getString("secret");
			JSONObject jOwner = jPhoto.getJSONObject("owner");
			String nsid = jOwner.getString("nsid");
			poi.mThumbnailPath = "https://farm"+farm+".staticflickr.com/"+server+"/"+photoId+"_"+secret+"_B.jpg";
			poi.mUrl = "https://www.flickr.com/photos/"+nsid+"/"+photoId;
			return poi;
		}catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}

    public ArrayList<POI> getThem(String fullUrl,String extras){
        Log.d(BonusPackHelper.LOG_TAG, "FlickrPOIProvider:get:"+fullUrl);
        String jString = BonusPackHelper.requestStringFromUrl(fullUrl);
        if (jString == null) {
            Log.e(BonusPackHelper.LOG_TAG, "FlickrPOIProvider: request failed.");
            return null;
        }
        try {
            JSONObject jRoot = new JSONObject(jString);
            JSONObject jPhotos = jRoot.getJSONObject("photos");
            JSONArray jPhotoArray = jPhotos.getJSONArray("photo");
            int n = jPhotoArray.length();
            ArrayList<POI> pois = new ArrayList<POI>(n);
            for (int i=0; i<n; i++){
                JSONObject jPhoto = jPhotoArray.getJSONObject(i);
                String photoId = jPhoto.getString("id");
                POI poi = new POI(POI.POI_SERVICE_FLICKR);
                poi.mLocation = new GeoPoint(
                        jPhoto.getDouble("latitude"),
                        jPhoto.getDouble("longitude"));
                poi.mId = Long.parseLong(photoId);
                poi.mType = jPhoto.getString("title");
                poi.mThumbnailPath = jPhoto.getString(extras);
                String owner = jPhoto.getString("owner");
                poi.mUrl = "https://www.flickr.com/photos/"+owner+"/"+photoId;
                pois.add(poi);
            }
            int total = jPhotos.getInt("total");
            Log.d(BonusPackHelper.LOG_TAG, "done:"+n+" got, on a total of:"+total);
            return pois;
        }catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public ArrayList<POI> getPOIInside(BoundingBox boundingBox, int maxResults,String extras){
        String url = getUrlInside(boundingBox, maxResults,extras);
        return getThem(url,extras);
    }


}