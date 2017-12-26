package com.newapplol.request;

import android.app.VoiceInteractor;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.newapplol.entity.MatchEntity;

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
    private static final String API_KEY= "RGAPI-cb17e7e0-dae7-45d0-8b17-b85266467112";
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

    public void getHistoryMatchListsByMatchId(ArrayList<Long> matches, final long id, final HistoryCallback callback) {

        for (int i = 0; i < matches.size();i++){

            String url = "https://"+region+".api.riotgames.com/lol/match/v3/matches/"+matches.get(i)+"?api_key="+API_KEY;
            final JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {

                    List<Integer> teamWinners = new ArrayList<>();
                    List<Integer> teamLossers = new ArrayList<>();
                    Integer [] items = new Integer[7];
                    List<MatchEntity> historyMatches = new ArrayList<>();

                    if (response.length() > 0){
                        try {
                            long matchId = response.getLong("gameId");
                            long matchDuration = response.getLong("gameDuration");
                            long matchCreation = response.getLong("gameCreation");
                            String typeMatch = response.getString("gameMode");
                            int champId = 0;
                            int sum1 = 0;
                            int sum2 = 0;
                            int teamId = 0;
                            int participantId=0;
                            String summonerName=null;
                            int kills = 0;
                            int deaths = 0;
                            int assists = 0;
                            boolean win = false;
                            int gold=0;
                            int cs = 0;
                            int champLevel = 0;

                            JSONArray participantIdentities = response.getJSONArray("participantIdentities");

                            for(int i = 0; i < participantIdentities.length(); i++){
                                JSONObject parIdent = participantIdentities.getJSONObject(i);
                                int participantIdparIdent = parIdent.getInt("participantId");
                                JSONObject player = parIdent.getJSONObject("player");
                                int summonerId = player.getInt("summonerId");

                                if(id == summonerId){
                                    summonerName = player.getString("summonerName");
                                    participantId = parIdent.getInt("participantId");

                                }
                            }

                            JSONArray participants = response.getJSONArray("participants");

                            for (int j = 0; j < participants.length(); j++){
                                JSONObject participant = participants.getJSONObject(j);
                                int participantIdparticipants = participant.getInt("participantId");

                                if(participantId == participantIdparticipants){
                                    teamId = participant.getInt("teamId");
                                    sum1 = participant.getInt("spell1Id");
                                    sum2 = participant.getInt("spell2Id");

                                    JSONObject stats = participant.getJSONObject("stats");
                                    for(int k = 0; k < 7; k++){
                                        String item = "item" + k;
                                        items[k] = stats.getInt(String.valueOf(item));
                                    }
                                    kills = stats.getInt("kills");
                                    deaths = stats.getInt("deaths");
                                    assists = stats.getInt("assists");
                                    gold = stats.getInt("goldEarned");
                                    cs = stats.getInt("totalMinionsKilled");
                                    champLevel = stats.getInt("champLevel");

                                }
                            }

                            JSONArray teams = response.getJSONArray("teams");

                            for(int l = 0; l < teams.length(); l++){
                                JSONObject team = teams.getJSONObject(l);
                                int teamIdJsonteams = team.getInt("teamId");

                                if(teamIdJsonteams == teamId){
                                    String winStatus = team.getString("win");
                                    if(winStatus.equals("Fail")){
                                        win = false;
                                    }

                                    if(winStatus.equals("Win")){
                                        win = true;
                                    }
                                }
                            }

                            /*Para definir TeamWinner o TeamLossers*/
                            JSONArray participantsteams = response.getJSONArray("participants");

                            for (int j = 0; j < participantsteams.length(); j++){
                                JSONObject participantTeam = participantsteams.getJSONObject(j);
                                int participantIdparticipants = participantTeam.getInt("participantId");

                                int teamIdxPlayerId = participantTeam.getInt("teamId");

                                if(win){
                                    if(teamIdxPlayerId == teamId){
                                        teamWinners.add(participantTeam.getInt("championId"));
                                    }else{
                                        teamLossers.add(participantTeam.getInt("championId"));
                                    }
                                }else{
                                    if(teamIdxPlayerId != teamId){
                                        teamWinners.add(participantTeam.getInt("championId"));
                                    }else{
                                        teamLossers.add(participantTeam.getInt("championId"));
                                    }
                                }
                            }

                            if(win){
                                teamWinners.add(champId);
                            }else{
                                teamLossers.add(champId);
                            }

                            String champName = getChampionName(champId);
                            String sum1Name = getSummonerName(sum1);
                            String sum2Name = getSummonerName(sum2);

                            MatchEntity singleMatch = new MatchEntity(participantId,win,matchId,matchCreation,matchDuration,champId,kills,deaths,assists,gold,cs,champLevel,items,sum1Name,sum2Name,champName,typeMatch,teamWinners,teamLossers);

                            historyMatches.add(singleMatch);
                            items = new Integer[7];
                            teamWinners = new ArrayList<>();

                            callback.onSuccess(historyMatches);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }else{
                        callback.noMatch("No found matches");
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


        }


    }

    public interface HistoryCallback{
        void onSuccess(List<MatchEntity> matches);
        void onError(String message);
        void noMatch(String message);
    }

    public void getHistoryMatchListsByMatchIdDemo(long id){

        String json = getJsonFile(context,"match-example.json");

        try {
            List<Integer> teamWinners = new ArrayList<>();
            List<Integer> teamLossers = new ArrayList<>();
            Integer [] items = new Integer[7];
            List<MatchEntity> historyMatches = new ArrayList<>();

            JSONObject data = new JSONObject(json);
            long matchId = data.getLong("gameId");
            long matchDuration = data.getLong("gameDuration");
            long matchCreation = data.getLong("gameCreation");
            String typeMatch = data.getString("gameMode");
            int champId = 0;
            int sum1 = 0;
            int sum2 = 0;
            int teamId = 0;
            int participantId=0;
            String summonerName=null;
            int kills = 0;
            int deaths = 0;
            int assists = 0;
            boolean win = false;

            int gold=0;
            int cs = 0;
            int champLevel = 0;

            JSONArray participantIdentities = data.getJSONArray("participantIdentities");

            for(int i = 0; i < participantIdentities.length(); i++){
                JSONObject parIdent = participantIdentities.getJSONObject(i);
                int participantIdparIdent = parIdent.getInt("participantId");
                JSONObject player = parIdent.getJSONObject("player");
                int summonerId = player.getInt("summonerId");

                if(id == summonerId){
                    summonerName = player.getString("summonerName");
                    participantId = parIdent.getInt("participantId");

                }
            }

            JSONArray participants = data.getJSONArray("participants");

            for (int j = 0; j < participants.length(); j++){
                JSONObject participant = participants.getJSONObject(j);
                int participantIdparticipants = participant.getInt("participantId");

                if(participantId == participantIdparticipants){
                    teamId = participant.getInt("teamId");
                    sum1 = participant.getInt("spell1Id");
                    sum2 = participant.getInt("spell2Id");

                    JSONObject stats = participant.getJSONObject("stats");
                    for(int k = 0; k < 7; k++){
                        String item = "item" + k;
                        items[k] = stats.getInt(String.valueOf(item));
                    }
                    kills = stats.getInt("kills");
                    deaths = stats.getInt("deaths");
                    assists = stats.getInt("assists");
                    gold = stats.getInt("goldEarned");
                    cs = stats.getInt("totalMinionsKilled");
                    champLevel = stats.getInt("champLevel");
                }
            }

            JSONArray teams = data.getJSONArray("teams");

            for(int l = 0; l < teams.length(); l++){
                JSONObject team = teams.getJSONObject(l);
                int teamIdJsonteams = team.getInt("teamId");

                if(teamIdJsonteams == teamId){
                    String winStatus = team.getString("win");
                    if(winStatus.equals("Fail")){
                        win = false;
                    }

                    if(winStatus.equals("Win")){
                        win = true;
                    }
                }
            }

            /*Para definir TeamWinner o TeamLossers*/
            JSONArray participantsteams = data.getJSONArray("participants");

            for (int j = 0; j < participantsteams.length(); j++){
                JSONObject participantTeam = participantsteams.getJSONObject(j);
                int participantIdparticipants = participantTeam.getInt("participantId");

                int teamIdxPlayerId = participantTeam.getInt("teamId");

                if(win){
                    if(teamIdxPlayerId == teamId){
                        teamWinners.add(participantTeam.getInt("championId"));
                    }else{
                        teamLossers.add(participantTeam.getInt("championId"));
                    }
                }else{
                    if(teamIdxPlayerId != teamId){
                        teamWinners.add(participantTeam.getInt("championId"));
                    }else{
                        teamLossers.add(participantTeam.getInt("championId"));
                    }
                }
            }

            if(win){
                teamWinners.add(champId);
            }else{
                teamLossers.add(champId);
            }

            String champName = getChampionName(champId);
            String sum1Name = getSummonerName(sum1);
            String sum2Name = getSummonerName(sum2);

            MatchEntity singleMatch = new MatchEntity(participantId,win,matchId,matchCreation,matchDuration,champId,kills,deaths,assists,gold,cs,champLevel,items,sum1Name,sum2Name,champName,typeMatch,teamWinners,teamLossers);


            Log.d("summonerName"," : "+summonerName);
            Log.d("teamId"," : "+teamId);
            Log.d("sum1"," : "+sum1);
            Log.d("sum2"," : "+sum2);
            Log.d("items"," : "+items);
            Log.d("win"," : "+win);


            /*
            for(int c = 0; c < items.length ; c++ ){
                int itemplay = items[c];
                Log.d("item["+c+"]"," => " + itemplay);
            }
            */

        } catch (JSONException e) {

        }





    }

}