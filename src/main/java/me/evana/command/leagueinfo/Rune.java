package me.evana.command.leagueinfo;

public class Rune {
    private boolean isKeyStone;
    private int id;
    private String name;
    private String key;
    private String owner;


    public Rune(int id, String name, String key, boolean isKeyStone, String owner){
        this.id = id;
        this.name = name;
        this.key = key;
        this.isKeyStone = isKeyStone;
        this.owner = owner;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public String getKey() {
        return key;
    }

    public boolean isKeyStone() {
        return isKeyStone;
    }

    public String getOwner() {
        return owner;
    }
}
