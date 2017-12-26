package com.newapplol.entity;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class MatchEntity {

    private  boolean winner;
    private long matchId,matchCreation,matchDuration;
    private int champId,kills,deaths,assists,gold,cs,champLevel;
    private LinkedHashMap<String , Integer> stats = new LinkedHashMap<>();
    private Integer [] items = new Integer[7];
    private String sum1,sum2,champName, typeMatch;
    private List<Integer> teamWinner = new ArrayList<>();
    private List<Integer> teamLoser = new ArrayList<>();
    private int participantId;


    public MatchEntity(int participantId,boolean winner, long matchId, long matchCreation, long matchDuration, int champId, int kills, int deaths, int assists, int gold, int cs, int champLevel, Integer[] items, String sum1, String sum2, String champName, String typeMatch, List<Integer> teamWinner, List<Integer> teamLoser) {
        this.participantId = participantId;
        this.winner = winner;
        this.matchId = matchId;
        this.matchCreation = matchCreation;
        this.matchDuration = matchDuration;
        this.champId = champId;
        this.kills = kills;
        this.deaths = deaths;
        this.assists = assists;
        this.gold = gold;
        this.cs = cs;
        this.champLevel = champLevel;
        this.items = items;
        this.sum1 = sum1;
        this.sum2 = sum2;
        this.champName = champName;
        this.typeMatch = typeMatch;
        this.teamWinner = teamWinner;
        this.teamLoser = teamLoser;
    }


    public boolean isWinner() {
        return winner;
    }

    public long getMatchId() {
        return matchId;
    }

    public long getMatchCreation() {
        return matchCreation;
    }

    public long getMatchDuration() {
        return matchDuration;
    }

    public int getChampId() {
        return champId;
    }

    public int getKills() {
        return kills;
    }

    public int getDeaths() {
        return deaths;
    }

    public int getAssists() {
        return assists;
    }

    public int getGold() {
        return gold;
    }

    public int getCs() {
        return cs;
    }

    public int getChampLevel() {
        return champLevel;
    }

    public LinkedHashMap<String, Integer> getStats() {
        return stats;
    }

    public Integer[] getItems() {
        return items;
    }

    public String getSum1() {
        return sum1;
    }

    public String getSum2() {
        return sum2;
    }

    public String getChampName() {
        return champName;
    }

    public String getTypeMatch() {
        return typeMatch;
    }

    public List<Integer> getTeamWinner() {
        return teamWinner;
    }

    public List<Integer> getTeamLoser() {
        return teamLoser;
    }

    public void setWinner(boolean winner) {
        this.winner = winner;
    }

    public void setMatchId(long matchId) {
        this.matchId = matchId;
    }

    public void setMatchCreation(long matchCreation) {
        this.matchCreation = matchCreation;
    }

    public void setMatchDuration(long matchDuration) {
        this.matchDuration = matchDuration;
    }

    public void setChampId(int champId) {
        this.champId = champId;
    }

    public void setKills(int kills) {
        this.kills = kills;
    }

    public void setDeaths(int deaths) {
        this.deaths = deaths;
    }

    public void setAssists(int assists) {
        this.assists = assists;
    }

    public void setGold(int gold) {
        this.gold = gold;
    }

    public void setCs(int cs) {
        this.cs = cs;
    }

    public void setChampLevel(int champLevel) {
        this.champLevel = champLevel;
    }

    public void setStats(LinkedHashMap<String, Integer> stats) {
        this.stats = stats;
    }

    public void setItems(Integer[] items) {
        this.items = items;
    }

    public void setSum1(String sum1) {
        this.sum1 = sum1;
    }

    public void setSum2(String sum2) {
        this.sum2 = sum2;
    }

    public void setChampName(String champName) {
        this.champName = champName;
    }

    public void setTypeMatch(String typeMatch) {
        this.typeMatch = typeMatch;
    }

    public void setTeamWinner(List<Integer> teamWinner) {
        this.teamWinner = teamWinner;
    }

    public void setTeamLoser(List<Integer> teamLoser) {
        this.teamLoser = teamLoser;
    }
}