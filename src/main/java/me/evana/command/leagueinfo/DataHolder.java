package me.evana.command.leagueinfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import me.evana.command.leagueinfo.Champion;

public class DataHolder {


    private static List<SummonerSpell> summonerSpells = new ArrayList<>();
    private static List<Champion> champions = new ArrayList<>();
    private static List<Rune> runes = new ArrayList<>();
    private static HashMap<Long, String> overallRune = new HashMap<>();

    public static Champion getChampById(int id){
        for(Champion champion : champions){
            if(champion.getKey() == id){
                return champion;
            }
        }
        return new Champion(-1, "N/A", "N/A", null);
    }

    public static SummonerSpell getSumById(int id){
        for(SummonerSpell summonerSpell : summonerSpells){
            if(summonerSpell.getKey() == id){
                return summonerSpell;
            }
        }
        return new SummonerSpell(-1,"N/A","N/A");
    }

    public static Rune getRuneById(long id){
        for(Rune rune : runes){
            if(rune.getId() == id){
                return rune;
            }
        }
        return new Rune(-1,"N/A","N/A", false, "N/A");
    }

    public static String getOverallRuneById(long id){
        return overallRune.get(id);
    }

    public static List<Champion> getChampions() {
        return champions;
    }

    public static List<SummonerSpell> getSummonerSpells() {
        return summonerSpells;
    }

    public static List<Rune> getRunes(){
        return runes;
    }


    public static void setChampions(List<Champion> champions) {
        DataHolder.champions = champions;
    }

    public static void setSummonerSpells(List<SummonerSpell> summonerSpells){
        DataHolder.summonerSpells = summonerSpells;
    }

    public static void setRunes(List<Rune> runes){
        DataHolder.runes = runes;
    }

    public static void setOverallRunes(String[] names, long[] ids){
        for (int i = 0; i < names.length; i++){
            overallRune.put(ids[i], names[i]);
        }
    }
}
