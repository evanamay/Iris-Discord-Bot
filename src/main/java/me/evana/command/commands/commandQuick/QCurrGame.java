package me.evana.command.commands.commandQuick;

import me.evana.command.ICommand;
import me.evana.command.IrisMain;
import me.evana.command.commands.CommandContext;
import me.evana.command.commands.RiotMain;
import me.evana.command.leagueinfo.Champion;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Emote;
import net.dv8tion.jda.api.entities.TextChannel;
import net.rithms.riot.api.RiotApi;
import net.rithms.riot.api.RiotApiException;
import net.rithms.riot.api.endpoints.spectator.dto.*;
import net.rithms.riot.constant.Platform;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static me.evana.command.commands.RiotMatch.getSummonerName;
import static me.evana.command.leagueinfo.DataHolder.*;

public class QCurrGame implements ICommand {
    private final List<Long> keyStone = new ArrayList<Long>(){{
        add(8112L);add(8124L);add(8128L);add(9923L);add(8351L);add(8360L);add(8358L);add(8005L);
        add(8008L);add(8021L);add(8010L);add(8437L);add(8439L);add(8465L);add(8214L);add(8229L);add(8230L);}};

    private final List<Long> firstTier = new ArrayList<Long>(){{
        add(8126L);add(8139L);add(8143L);add(8306L);add(8304L);add(8313L);add(9101L);add(9111L);
        add(8009L);add(8446L);add(8463L);add(8401L);add(8224L);add(8226L);add(8275L);}};

    private final List<Long> secondTier = new ArrayList<Long>(){{
        add(8136L);add(8120L);add(8138L);add(8321L);add(8316L);add(8345L);add(9104L);add(9105L);
        add(9103L);add(8429L);add(8444L);add(8473L);add(8210L);add(8234L);add(8233L);}};


    private final List<Long> thirdTier = new ArrayList<Long>(){{
        add(8135L);add(8134L);add(8105L);add(8106L);add(8347L);add(8410L);add(8352L);add(8014L);
        add(8017L);add(8299L);add(8451L);add(8453L);add(8242L);add(8237L);add(8232L);add(8236L);}};



    @Override
    public void handle(CommandContext ctx) throws Exception {
        final TextChannel channel = ctx.getChannel();
        final List<String> args = ctx.getArgs();
        final RiotApi riot = RiotMain.getRiotApi();
        String summonerName = getSummonerName(args, 1);
        Platform platform = Platform.getPlatformByName(args.get(args.size()-1));
        CurrentGameInfo a = null;

        try {
           a = riot.getActiveGameBySummoner(platform, riot.getSummonerByName(platform, summonerName).getId());

            EmbedBuilder message = new EmbedBuilder();
            message.setColor(Color.blue);
            message.setTitle(summonerName + "'s Current Game: " + a.getGameMode());



            List<BannedChampion> b = a.getBannedChampions();
            String t1bans = "";
            String t2bans = "";
            for(int i = 0; i < 9; i++){
                if(b.get(i).getTeamId() == 100){
                    t1bans += getChampById(b.get(i).getChampionId()).getDisplayName() + " ";
                } else {
                    t2bans += getChampById(b.get(i).getChampionId()).getDisplayName() + " ";
                }
            }
            message.addField("Team 1 Bans", t1bans,false);
            message.addField("Team 2 Bans", t2bans,false);

            List<CurrentGameParticipant> c = a.getParticipants();


            String teamOne = "";
            String teamTwo = "";

            for(int i = 0; i < 10; i++){

                CurrentGameParticipant player = c.get(i);
                String summonerN = player.getSummonerName();
                Champion champion = getChampById(player.getChampionId());
                String primary = getOverallRuneById(player.getPerks().getPerkStyle());
                List<Long> perkIds = player.getPerks().getPerkIds();
                String[] perks = perkSorter(perkIds,primary);


                if(player.getTeamId() == 100){
                    teamOne += "**"+summonerN+"** : " +  "\n" + champion.getDisplayName() + "\n[" +
                            getSumById(player.getSpell1Id()).getName() + ", " +
                            getSumById(player.getSpell2Id()).getName() + "]\nPrimary Rune: " +
                            getOverallRuneById(player.getPerks().getPerkStyle()) + "\n\t" + perks[0] + "\n\t" + perks[1] + "\n\t" + perks[2] + "\n\t" + perks[3] +
                            "\nSec. Rune: " +
                            getOverallRuneById(player.getPerks().getPerkSubStyle()) +
                            "\n";
                }else{
                    teamTwo += "**"+summonerN+"** : " +  "\n" + champion.getDisplayName() + "\n[" +
                            getSumById(player.getSpell1Id()).getName() + ", " +
                            getSumById(player.getSpell2Id()).getName() + "]\nPrimary Rune: " +
                            getOverallRuneById(player.getPerks().getPerkStyle()) + "\n\t" + perks[0] + "\n\t" + perks[1] + "\n\t" + perks[2] + "\n\t" + perks[3] +
                            "\nSec. Rune: " +
                            getOverallRuneById(player.getPerks().getPerkSubStyle()) +
                            "\n";

                }



            }

            message.addField("Team One",teamOne,true);
            message.addField("Team Two",teamTwo,true);
            channel.sendMessage(message.build()).queue();

        } catch (RiotApiException e){
            channel.sendMessage("This player is not currently in a game").queue();
        }

    }


    @Override
    public String getName() { return "qcg"; }

    @Override
    public String getHelp() {
        return "Shows information about the current game\n" +
                "Usage: `<qcg <summoner name> <region>`";
    }


    private String[] perkSorter(List<Long> ids, String primary){
        String[] perks = new String[4];
        for(int i = 0; i < ids.size(); i++){
            long a = ids.get(i);
            if(keyStone.contains(ids.get(i))){
                perks[0] = getRuneById(ids.get(i).longValue()).getName();
            } else if(firstTier.contains(ids.get(i))){
                if(primary.equals(getRuneById(ids.get(i)).getOwner())){
                    perks[1] = getRuneById(ids.get(i).longValue()).getName();
                }
            } else if(secondTier.contains(ids.get(i))){
                if(primary.equals(getRuneById(ids.get(i)).getOwner())){
                    perks[2] = getRuneById(ids.get(i).longValue()).getName();
                }
            } else if(thirdTier.contains(ids.get(i))){
                if(primary.equals(getRuneById(ids.get(i)).getOwner())){
                    perks[3] = getRuneById(ids.get(i).longValue()).getName();
                }
            }

        }
        return perks;
    }
}
