package me.evana.command.leagueinfo;

import net.dv8tion.jda.api.entities.Emote;

import java.io.File;

public class Champion {
    private int key;
    private String id;
    private String name;
    private File champIcon;
    private Emote emote;

    public Champion(final int key, final String id, final String name, File champIcon){
        this.key = key;
        this.id = id;
        this.name = name;
        this.champIcon = champIcon;
    }

    public String getDisplayName() {
        if(emote != null) {
            return emote.getAsMention();
        }
        return name;
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

    public File getChampIcon() {
        return this.champIcon;
    }

    public Emote getEmote() {
        return this.emote;
    }

    public void setEmote(Emote emote) {
        this.emote = emote;
    }
}
