package com.example.nawigacja_po_umk;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.osmdroid.util.BoundingBox;

import java.net.MalformedURLException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class Dowloader_list {

    static List<BoundingBox> list_map;
    static List<String> list_name_map;
    BoundingBox actualmap;
    public JSONArray jsonArray;
    private URL url;

    RequestQueue gniazdo;
    Context context;
    Response.Listener response;

    Dowloader_list(Context context,String url_string)
    {
        list_map=new ArrayList<>();
        list_name_map=new ArrayList<>();
        actualmap=null;
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
    void dowland_list()
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
            list_name_map.add(box.getString("name"));
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

    public static List<String> getList_name_map() {
        return list_name_map;
    }
}
