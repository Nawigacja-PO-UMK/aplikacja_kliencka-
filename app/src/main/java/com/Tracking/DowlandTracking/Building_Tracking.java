package com.Tracking.DowlandTracking;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.Tracking.activity_Tracking;
import com.Tracking.trasy.trasa_building;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BaseHttpStack;
import com.android.volley.toolbox.HttpResponse;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.loader_Map_Building.Mapa_budynku;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.osmdroid.bonuspack.routing.Road;
import org.osmdroid.bonuspack.routing.RoadNode;
import org.osmdroid.util.GeoPoint;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class Building_Tracking extends Tracking {
    static boolean END;
    public JSONArray jsonArray;
    private URL url;
    String url_string= "https://34.125.80.2/server/serwe/ways_guery.php";
    RequestQueue gniazdo;
    Response.Listener response;
    //activity_Tracking activity_tracking;



    public Building_Tracking(Context context)  {
        super(context);
       // activity_tracking=activity;
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
    }

    public void Dowland_json_Tracking(GeoPoint begin, GeoPoint end,VolleyCallback callback)
    {
        response= new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                    callback.onSuccess(response);

            }
        };

        Response.ErrorListener errorListener=new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Toast.makeText(context,error.toString(),Toast.LENGTH_LONG).show();
            jsonArray=new JSONArray();
        }
    };
        StringRequest WysyłaneDane= new StringRequest(Request.Method.POST, url.toString(),
                response,errorListener){
            @Override
            protected Map<String,String> getParams()
            {
                Map<String,String> wysyłane= new HashMap<String,String>();
                wysyłane.put("S_X",String.valueOf(begin.getLongitude()));
                wysyłane.put("S_Y",String.valueOf(begin.getLatitude()));
                wysyłane.put("S_L",String.valueOf(0));

                wysyłane.put("E_X",String.valueOf(end.getLongitude()));
                wysyłane.put("E_Y",String.valueOf(end.getLatitude()));
                wysyłane.put("E_L",String.valueOf( (int)  Math.round(end.getAltitude())));
                return wysyłane;
            }
        };

        RequestQueue gniazdo= Volley.newRequestQueue(context);
        gniazdo.add(WysyłaneDane);
    }
    public interface VolleyCallback{
        void onSuccess(String result);
    }
    private void parss_JSON_Road(JSONArray jsonArray,Road trasa)
    {
        trasa.mRouteHigh=new ArrayList<>();
        for (int i=0;i< jsonArray.length();i++) {
            try {
                JSONObject way=jsonArray.getJSONObject(i);
                double level=way.getDouble("level");
                if(way.getString("level").length()>2)
                    level=0.5;
                GeoPoint point=new GeoPoint(way.getDouble("Y"),
                        way.getDouble("X"),level);
                trasa.mRouteHigh.add(point);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        trasa.mStatus=Road.STATUS_OK;
        trasa.mLength=com.Tracking.trasy.trasa.Distance(trasa)/1000;
        trasa.mDuration=trasa.mLength* 700;
    }
    @Override
    public void Dowland_Tracking(GeoPoint begin, GeoPoint end,Road road, activity_Tracking activity_tracking) {
        Dowland_json_Tracking(begin,end,new VolleyCallback() {
            @Override
            public void onSuccess(String result) {
                try {
                    jsonArray = new JSONArray(result);
                    parss_JSON_Road(jsonArray,road);
                    add_Nodess(road);
                    trasa_building trasa_building =(trasa_building)activity_tracking.trasa;
                    activity_tracking.screean_tracking.update_descryption(trasa_building);
                    activity_tracking.mapView.getOverlays().removeAll(trasa_building.markers_target);
                    activity_tracking.mapView.getOverlays().addAll(trasa_building.print_marker(Mapa_budynku.level));
                    activity_tracking.mapView.getOverlays().remove(trasa_building.polyline);
                    activity_tracking.mapView.getOverlays().removeAll(trasa_building.polylines);
                    activity_tracking.mapView.getOverlays().addAll(trasa_building.polyline(Mapa_budynku.level));
                } catch (JSONException ex) {
                    ex.printStackTrace();
                    jsonArray = new JSONArray();
                    Toast.makeText(context, ex.toString(), Toast.LENGTH_LONG).show();
                    Toast.makeText(context, result, Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

private void addinstruction(Road road,GeoPoint geoPoint,String instruction)
    {
        RoadNode roadNode=new RoadNode();
        roadNode.mInstructions=instruction;
        roadNode.mLocation=geoPoint;
        road.mNodes.add(roadNode);
    }

    private void add_Nodess(Road road) {

        road.mNodes=new ArrayList<>();
        addinstruction(road,road.mRouteHigh.get(0),"kontynłuj");

        for(int i=2;i<road.mRouteHigh.size();i++)
     {
         if(search_angle2(road.mRouteHigh,i)>0)
         {
             if(search_angle(road.mRouteHigh,i)>35 && search_angle(road.mRouteHigh,i)+Math.abs(search_angle2(road.mRouteHigh,i))>160)
                 addinstruction(road,road.mRouteHigh.get(i-1),"skręć w lewo "+
                         String.valueOf(search_angle(road.mRouteHigh,i))+" a2:"+String.valueOf(search_angle2(road.mRouteHigh,i)));

         }

         if(search_angle2(road.mRouteHigh,i)<0)
         {
             if(search_angle(road.mRouteHigh,i)>35&&search_angle(road.mRouteHigh,i)+Math.abs(search_angle2(road.mRouteHigh,i))>160)
                 addinstruction(road,road.mRouteHigh.get(i-1),"skręć w prawo "+
                         String.valueOf(search_angle(road.mRouteHigh,i))+" a2:"+String.valueOf(search_angle2(road.mRouteHigh,i)));

         }
         if(road.mRouteHigh.get(i).getAltitude()%1.0>0.3)
         {
             Toast.makeText(context, "dziala", Toast.LENGTH_SHORT).show();
             addinstruction(road,road.mRouteHigh.get(i-1),"idz po schodach");
         }

     }
        addinstruction(road,road.mRouteHigh.get(road.mRouteHigh.size()-1),"Dotarleś do celu");
    }

    double search_angle(ArrayList<GeoPoint> points,int i)
    {
        double[] V1={points.get(i).getLatitude()-points.get(i-1).getLatitude(),points.get(i).getLongitude()-points.get(i-1).getLongitude()};
        double[] V2={points.get(i-1).getLatitude()-points.get(i-2).getLatitude(),points.get(i-1).getLongitude()-points.get(i-2).getLongitude()};

        double wynik=(V1[0]*V2[0])+(V1[1]*V2[1])/(
                Math.sqrt(Math.pow(V1[0],2)+Math.pow(V1[1],2))*Math.sqrt(Math.pow(V2[0],2)+Math.pow(V2[1],2)));
        //Toast.makeText(context, String.valueOf(Math.toDegrees(Math.acos(wynik))), Toast.LENGTH_SHORT).show();
        return Math.toDegrees(Math.acos(wynik));

       //double wynik=Math.toDegrees(Math.atan2((V1[0]*V2[1])-(V1[1]*V2[0]),(V1[0]*V2[0]) - (V1[1]*V2[1])));

        //if(wynik)
        //return wynik;
    }

    double search_angle2(ArrayList<GeoPoint> points,int i)
    {
        double[] V1={points.get(i).getLatitude()-points.get(i-1).getLatitude(),points.get(i).getLongitude()-points.get(i-1).getLongitude()};
        double[] V2={points.get(i-1).getLatitude()-points.get(i-2).getLatitude(),points.get(i-1).getLongitude()-points.get(i-2).getLongitude()};

       // double wynik=(V1[0]*V2[0])+(V1[1]*V2[1])/(
         //       Math.sqrt(Math.pow(V1[0],2)+Math.pow(V1[1],2))*Math.sqrt(Math.pow(V2[0],2)+Math.pow(V2[1],2)));
        //Toast.makeText(context, String.valueOf(Math.toDegrees(Math.acos(wynik))), Toast.LENGTH_SHORT).show();
        //return Math.toDegrees(Math.acos(wynik));

        double wynik=Math.toDegrees(Math.atan2((V1[0]*V2[1])-(V1[1]*V2[0]),(V1[0]*V2[0]) - (V1[1]*V2[1])));

        //if(wynik)
        return wynik;
    }





}
