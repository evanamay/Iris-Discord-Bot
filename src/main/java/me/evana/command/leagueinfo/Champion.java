package me.evana.command.leagueinfo;

import java.io.File;

public class Champion {
    private int key;
    private String id;
    private String name;

    public Champion(final int key, final String id, final String name){
        this.key = key;
        this.id = id;
        this.name = name;
    }

    public int getKey(){
        return this.key;
    }

    public String getId(){
        return this.id;
    }

    public String getName() {
        return this.name;
    }

}
