package com.innovestudio.firebasevideodataloader.utils;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Map;

/**
 * Created by PC-2 on 5/9/2016.
 */
public class VolleyRequest {


    static public void sendRequest(final Context context, final String url, final Map<String, String> params, final VolleyCallback callback){
        final String[] serverResponse = new String[1];
        int count = 0;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                serverResponse[0] = response;
                callback.onSuccess(serverResponse[0]);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
               // sendRequest(context,url,params,callback);
                Toast.makeText(context, "There are some error,try again", Toast.LENGTH_SHORT).show();
                Log.d("trying",error.toString());

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                return params;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                20000,
                5,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }


    static public void sendRequestGet(final Context context, final String url, final VolleyCallback callback){
        final String[] serverResponse = new String[1];

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                serverResponse[0] = response;
                callback.onSuccess(serverResponse[0]);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                //sendRequestGet(context,url,callback);
                Toast.makeText(context, "There are some error,try again", Toast.LENGTH_SHORT).show();
            }
        } );
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                20000,
                5,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);


    }
//y45l

    /*public static void jsonObjectReq(Context context, String url, final VolleyCallBackJSONobj callback){
        final JSONObject[] serverResponse = new JSONObject[1];
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                url, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                serverResponse[0] = response;
                Log.d("result", response.toString());
                callback.onSuccess(serverResponse[0]);


                //setUpMenuLayout(response);
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(
                20000,
                5,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(jsonObjReq);

    }

    public static void jsonArrayReq(Context context, String url, final VolleyCallBackJSONArray callback){
        final JSONArray[] serverResponse = new JSONArray[1];
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET,
                url, new Response.Listener<JSONArray>() {

            @Override
            public void onResponse(JSONArray response) {
                serverResponse[0] = response;
                Log.d("result", response.toString());
                callback.onSuccess(serverResponse[0]);


                //setUpMenuLayout(response);
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        jsonArrayRequest.setRetryPolicy(new DefaultRetryPolicy(
                20000,
                5,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(jsonArrayRequest);

    }*/

    public interface VolleyCallback{
        void onSuccess(String result);
    }

    public interface VolleyCallBackJSONobj{
        void onSuccess(JSONObject result);
    }

    public interface VolleyCallBackJSONArray{
        void onSuccess(JSONArray result);
    }

}
