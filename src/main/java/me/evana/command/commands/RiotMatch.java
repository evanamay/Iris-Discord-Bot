package me.evana.command.commands;

import net.rithms.riot.api.RiotApi;
import net.rithms.riot.api.RiotApiException;
import net.rithms.riot.api.endpoints.match.dto.Match;
import net.rithms.riot.api.endpoints.match.dto.Participant;
import net.rithms.riot.api.endpoints.match.dto.TeamStats;
import net.rithms.riot.constant.Platform;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.*;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class RiotMatch {


    /*
     * Formats the date taken from a MatchReference
     */
    public static String dateFormatter (long timestamp){
        timestamp = timestamp/1000L;
        String date = new java.text.SimpleDateFormat("MM/dd/yyyy 'at' HH:mm:ss")
                .format(new java.util.Date (timestamp*1000L));

        return date;
    }

    /*
     * Locates the gamemode type from the queueId given from a MatchReference
     */
    public static String queueIdFinder(long queueId) throws Exception {
        Object obj = new JSONParser().parse(new FileReader("src/main/resources/queues.json"));
        JSONArray jo = (JSONArray) obj;

        for(int i = 0; i < jo.size(); i++){
            Map iter = (Map) jo.get(i);
            if((long) iter.get("queueId") == queueId){
                if(((String) iter.get("description")).equals("5v5 Draft Pick games")){
                    return "Draft Pick";
                }
                return (String) iter.get("description");
            }
        }

        return "Error: Gamemode not found";
    }

    /*
     * Returns String Win if the game was won otherwise returns Loss
     */
    public static String isWin(RiotApi riot, long gameID, Platform platform, String summoner) throws RiotApiException {
        Match currentMatch = riot.getMatch(Platform.NA,gameID);
        Participant summonerInGame = currentMatch.getParticipantBySummonerName(summoner);
        int teamNum = summonerInGame.getTeamId();
        TeamStats teamStat = currentMatch.getTeamByTeamId(teamNum);
        String isWin = teamStat.getWin();
        isWin = isWin.equals("Fail") ? "Loss" : "Win";
        return isWin;
    }



    /*
     * Determines lane
     */
    public static String laneFinder(String lane, String role) throws Exception {
        Object obj = new JSONParser().parse(new FileReader("src/main/resources/lane.json"));
        Map jo = (JSONObject) obj;

        String result = (String) jo.get(lane + " " + role);

        if (result == null){
            return "N/A";
        }else {
            return result;
        }

    }

    /*
     * Constructs a summoner name (used if the username has spaces)
     */
    public static String getSummonerName(List<String> argfield, int argNum){
        String summonerName = "";
        for(int i = 0; i < argfield.size()-argNum; i++){
            summonerName+=argfield.get(i);
            if(i+1 != argfield.size()-1){
                summonerName+=" ";
            }
        }

        return summonerName;
    }

}
