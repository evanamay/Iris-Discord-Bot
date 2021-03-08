package me.evana.command.commands;

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

    public static String dateFormatter (long timestamp){
        timestamp = timestamp/1000L;
        String date = new java.text.SimpleDateFormat("MM/dd/yyyy 'at' HH:mm:ss")
                .format(new java.util.Date (timestamp*1000L));

        return date;
    }

    public static String queueIdFinder(long queueId) throws Exception {
        Object obj = new JSONParser().parse(new FileReader("src/main/resources/queues.json"));
        JSONArray jo = (JSONArray) obj;

        for(int i = 0; i < jo.size(); i++){
            Map iter = (Map) jo.get(i);
            if((long) iter.get("queueId") == queueId){
                return (String) iter.get("description");
            }
        }

        return "Error: Gamemode not found";
    }

    public static String champIdFinder(int champKey) throws Exception {
        Object obj = new JSONParser().parse(new FileReader("src/main/resources/champion.json"));
        JSONObject jo = (JSONObject) obj;
        Map champs = (Map) jo.get("data");

        Iterator<Map.Entry> itr1 = champs.entrySet().iterator();
        while(itr1.hasNext()){
            Map.Entry pair = itr1.next();
            Map specificChamp = (Map) pair.getValue();
            if(champKey == Integer.parseInt((String) specificChamp.get("key"))){
                return (String) pair.getKey();
            }
        }
        return "Champion not found";
    }

}
