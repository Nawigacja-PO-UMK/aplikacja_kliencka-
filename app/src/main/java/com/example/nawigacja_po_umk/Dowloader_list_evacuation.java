package com.example.nawigacja_po_umk;

import android.content.Context;
import android.location.Address;
import android.location.Location;
import android.widget.Toast;

import com.Tracking.activity_Tracking;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.lokalizator.Akcje_na_lokacizacji;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.osmdroid.util.BoundingBox;
import org.osmdroid.util.GeoPoint;

import java.net.MalformedURLException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class Dowloader_list_evacuation implements Akcje_na_lokacizacji {

    static List<BoundingBox> list_map;
    static List<GeoPoint> points_evaculation;
    BoundingBox actualmap;
    public JSONArray jsonArray;
    private URL url;
    private long timer=10000;
    private long actual_time;
    private activity_Tracking tracking;
    RequestQueue gniazdo;
    Context context;
    Response.Listener response;
    String url_string="https://34.125.216.223/server/serwe/list_evacuation.php";

    Dowloader_list_evacuation(Context context, activity_Tracking tracking)
    {
        list_map=new ArrayList<>();
        points_evaculation=new ArrayList<>();
        actualmap=null;
       this.tracking=tracking;
        this.context=context;

        try {
            url = new URL(url_string);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        try {
            X509TrustManager trustManager = new X509TrustManager() {
                public void checkClientTrusted(X509Certificate[] xcs, String string) throws CertificateException {}
                public void checkServerTrusted(X509Certificate[] xcs, String string) throws CertificateException {}
                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
            };
            SSLContext sslContext = SSLContext.getInstance("TLSv1.2");
            sslContext.init(null, new TrustManager[] { trustManager }, null);
            HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }

        gniazdo=Volley.newRequestQueue(context);

        dowland_list();
    }
    public void dowland_list()
    {
        response= new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    jsonArray = new JSONArray(response);

                    prse_json_list_box(jsonArray);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        };
        Response.ErrorListener errorListener=new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                jsonArray=new JSONArray();
            }
        };
        StringRequest WysyłaneDane= new StringRequest(Request.Method.POST, url.toString(),
                response,errorListener){
            @Override
            protected Map<String,String> getParams()
            {
                Map<String,String> wysyłane= new HashMap<String,String>();
                return wysyłane;
            }
        };

        RequestQueue gniazdo= Volley.newRequestQueue(context);
        gniazdo.add(WysyłaneDane);
    }

    void prse_json_list_box(JSONArray jsonArray) throws JSONException {
        for (int i=0;i<jsonArray.length();i++)
        {
            JSONObject box =jsonArray.getJSONObject(i);
            BoundingBox box1 = new BoundingBox();
            box1.setLonWest(box.getDouble("w"));
            box1.setLatNorth(box.getDouble("n"));
            box1.setLatSouth(box.getDouble("s"));
            box1.setLonEast(box.getDouble("e"));
            points_evaculation.add(new GeoPoint(box.getJSONArray("point_evaculation").getDouble(0),
                    box.getJSONArray("point_evaculation").getDouble(1)));
            list_map.add(box1);
        }

    }
    static public List<BoundingBox> get_list()
    {
        return list_map;
    }
    public BoundingBox getActualmap()
    {
        return actualmap;
    }

    public void setActualmap(BoundingBox actualmap) {
        this.actualmap = actualmap;
    }


    @Override
    public boolean warunek(Location location) {


        if((new Date()).getTime()-actual_time> timer) {
            actual_time=(new Date()).getTime();
            return true;
        }
        return false;
    }

    @Override
    public void Akcja(Location location) {


            for (BoundingBox box:list_map) {

             //   if(box.getLatNorth()>location.getLatitude() && box.getLatSouth()<location.getLatitude()
               //         && box.getLonEast()>location.getLongitude() && box.getLonWest()<location.getLongitude())
                {
                    tracking.remove_tracking();
                    Locale locale=new Locale("pl");
                    com.search_location.Address address=new com.search_location.Address(locale);
                    address.setFeatureName("Miejsce Zbiórki");
                    GeoPoint point=points_evaculation.get(list_map.indexOf(box));
                    address.setLatitude(point.getLatitude());
                    address.setLongitude(point.getLongitude());
                    address.setAltitude(point.getAltitude());
                    tracking.add_tracking(address);
                    ///komunikat
                    Toast.makeText(context, "Musisz ewakułować się z budynku", Toast.LENGTH_SHORT).show();

                }
            }
        dowland_list();
    }

    @Override
    public void Akcje_is_false(Location location) {

    }
}
