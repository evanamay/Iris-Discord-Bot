package me.evana.command.leagueinfo;

import java.util.ArrayList;
import java.util.List;
import me.evana.command.leagueinfo.Champion;

public class DataHolder {



    private static List<Champion> champions = new ArrayList<>();
    
    public static Champion getChampById(int id){
        for(Champion champion : champions){
            if(champion.getKey() == id){
                return champion;
            }
        }
        return new Champion(-1, "N/A", "N/A", null);
    }
    public static List<Champion> getChampions() {
        return champions;
    }

    public static void setChampions(List<Champion> champions) {
        DataHolder.champions = champions;
    }
}
