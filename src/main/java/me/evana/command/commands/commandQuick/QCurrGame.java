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
import net.rithms.riot.api.endpoints.spectator.dto.BannedChampion;
import net.rithms.riot.api.endpoints.spectator.dto.CurrentGameInfo;
import net.rithms.riot.api.endpoints.spectator.dto.CurrentGameParticipant;
import net.rithms.riot.api.endpoints.spectator.dto.Participant;
import net.rithms.riot.constant.Platform;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static me.evana.command.commands.RiotMatch.getSummonerName;
import static me.evana.command.leagueinfo.DataHolder.getChampById;

public class QCurrGame implements ICommand {
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
                int ssOne = player.getSpell1Id();
                int ssTwo = player.getSpell2Id();

                if(player.getTeamId() == 100){
                    teamOne += "**"+summonerN+"** : " +  champion.getDisplayName() + "\n" + ssOne + ", " + ssTwo + "\n\n";
                }else{
                    teamTwo += "**"+summonerN+"**: " + champion.getDisplayName() + "\n" + ssOne + ", " + ssTwo + "\n\n";

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
}
