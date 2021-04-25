package me.evana.command.emotes;

import me.evana.command.IrisMain;
import net.dv8tion.jda.api.entities.Emote;

import java.io.File;

public class ChampionEmotes {
    private String name;
    private File file;
    private long emoteId;

    public ChampionEmotes(String name, File file) {
        this.name = name;
        this.file = file;
    }

    public String getUsableEmote() {
        final Emote emote = IrisMain.getJda().getEmoteById(emoteId);

        if(emote != null) {
            return emote.getAsMention();
        }

        return name;
    }

    public String getName() {
        return name.replaceAll(" ", "");
    }

    public void setName(String name) {
        this.name = name;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public Emote getEmote() {
        return IrisMain.getJda().getEmoteById(emoteId);
    }

    public void setEmote(Emote emote) {
        this.emoteId = emote.getIdLong();
    }

}
