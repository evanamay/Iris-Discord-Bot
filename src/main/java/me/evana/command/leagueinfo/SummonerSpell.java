package me.evana.command.leagueinfo;

import java.util.List;

public class SummonerSpell {
    private int key;
    private String id;
    private String name;

    public SummonerSpell(int key, String id, String name){
        this.key = key;
        this.id = id;
        this.name = name;
    }


    public int getKey() {
        return key;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
