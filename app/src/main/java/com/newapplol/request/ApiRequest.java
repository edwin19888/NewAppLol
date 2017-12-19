package com.newapplol.request;

import android.content.Context;
import android.util.Log;

import com.android.volley.NetworkError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

public class ApiRequest {
    private RequestQueue queue;
    private Context context;
    private static final String API_KEY= "RGAPI-494fa6ac-76f4-4ce1-b89b-2e36f33bd1ff";
    private String region = "la2";

    public ApiRequest(RequestQueue queue,Context context){
        this.queue = queue;
        this.context = context;
    }

    public void checkPlayerName(final String name, final CheckPlayerCallback callback){
        String url = "https://"+region+".api.riotgames.com/lol/summoner/v3/summoners/by-name/"+name+"?api_key="+API_KEY;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("APP", response.toString());
                try {

                    String name = response.getString("name");
                    Long id = response.getLong("id");

                    /* Api Old
                    JSONObject json = response.getJSONObject("name");
                    String summonername = json.getString("name").toLowerCase().trim();
                    Long id = json.getLong("id");
                    */
                    // String summonername = "xTH3BR4x";
                    //Long id = Long.valueOf(11110132);



                    callback.onSuccess(name,id);

                } catch (JSONException e) {
                    Log.d("APP", "EXCEPTION = " + e);
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                if(error instanceof NetworkError){
                    callback.onError("Impossible to the connect");
                }else if(error instanceof ServerError){
                    callback.onError("Server Error");
                }
                Log.d("APP","ERROR = " + error);
            }
        });

        queue.add(request);
    }

    public interface CheckPlayerCallback{
        void onSuccess(String name, long id);
        void dontExist(String message);
        void onError(String message);
    }
}