package me.evana.command.emotes;

import me.evana.command.leagueinfo.Champion;
import me.evana.command.leagueinfo.DataHolder;

public class EmoteUtils {


    public static void addToChampionIfIsSame(ChampionEmotes emote) {
        for(Champion champion : DataHolder.getChampions()) {
            if(champion.getId().equals(emote.getName())) {
                champion.setEmote(emote.getEmote());
            }
        }
    }


}
