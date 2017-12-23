package com.newapplol.request;

import android.app.VoiceInteractor;
import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class ApiRequest {
    private RequestQueue queue;
    private Context context;
    private static final String API_KEY= "RGAPI-fdc4f34c-caa5-4de8-b079-0acc4a83dd3e";
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
                }else  if(error instanceof AuthFailureError){
                    callback.onError("Expired Key");
                }
                Log.d("APP","ERROR FOUND = " + error);
            }
        });

        queue.add(request);
    }

    public interface CheckPlayerCallback{
        void onSuccess(String name, long id);
        void dontExist(String message);
        void onError(String message);
    }

    public String getJsonFile(Context context, String filename){
        String json = null;

        try {
            InputStream is = context.getAssets().open(filename);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");

        }catch (IOException e){
            e.printStackTrace();
        }

        return json;
    }

    public String getChampionName(int champId) throws JSONException {
        String json = getJsonFile(context,"champion.json");

        JSONObject champ = new JSONObject(json);
        JSONObject data = champ.getJSONObject("data");
        JSONObject champInfo = data.getJSONObject(String.valueOf(champId));
        String champName = champInfo.getString("name");

        return champName;

    }

    public String getSummonerName(int spell1Id) throws JSONException{
        String json = getJsonFile(context,"summoner-spells.json");
        JSONObject spell = new JSONObject(json);
        JSONObject data = spell.getJSONObject("data");
        JSONObject spellInfo = data.getJSONObject(String.valueOf(spell1Id));
        String spellName = spellInfo.getString("name");
        return  spellName;

    }

    private ArrayList<Long> getHistoryMatchListsByAccountId(long accountId){

        String url = "https://"+region+".api.riotgames.com/lol/match/v3/matchlists/by-account/"+accountId+"/recent?api_key="+API_KEY;

        final ArrayList<Long> matches = new ArrayList<>();

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                if(response.length() > 0){
                    try {
                        JSONArray games = response.getJSONArray("matches");

                        for (int i = 0; i < games.length();i++){
                            JSONObject oneMatch = games.getJSONObject(i);
                            long matchId = oneMatch.getLong("gameId");
                            matches.add(matchId);
                        }

                    } catch (JSONException e) {
                        Log.d("APP:","EXCEPTION HISTORY = " + e);
                        e.printStackTrace();
                    }
                }else {

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        return matches;
    }

    public void getHistoryMatchListsByMatchId(long matchId){
        String url = "https://"+region+".api.riotgames.com/lol/match/v3/matches/"+matchId+"?api_key="+API_KEY;
        final JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                List<Integer> teamWinners = new ArrayList<>();
                List<Integer> teamLossers = new ArrayList<>();
                

                if (response.length() > 0){
                    try {
                        long gameId = response.getLong("gameId");
                        long gameCreation = response.getLong("gameCreation");
                        long gameDuration = response.getLong("gameDuration");
                        String gameMode = response.getString("gameMode");
                        JSONArray teams = response.getJSONArray("teams");
                        for(int j=0;j<teams.length();j++){
                            JSONObject oneteam = teams.getJSONObject(j);
                            String resultteam = oneteam.getString("win");
                            int teamId = oneteam.getInt("teamId");

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else{

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
    }

}