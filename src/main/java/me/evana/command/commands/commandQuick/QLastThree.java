package me.evana.command.commands.commandQuick;

import me.evana.command.ICommand;
import me.evana.command.commands.CommandContext;
import me.evana.command.commands.RiotMain;
import me.evana.command.commands.RiotMatch;
import me.evana.command.database.SQLiteDataSource;
import me.evana.command.leagueinfo.DataHolder;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;
import net.rithms.riot.api.RiotApi;
import net.rithms.riot.api.RiotApiException;
import net.rithms.riot.api.endpoints.match.dto.*;
import net.rithms.riot.api.endpoints.summoner.dto.Summoner;
import net.rithms.riot.constant.Platform;

import java.awt.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import static me.evana.command.commands.RiotMatch.isWin;

public class QLastThree implements ICommand {

    @Override
    public void handle(CommandContext ctx) throws Exception {
        final TextChannel channel = ctx.getChannel();
        final List<String> args = ctx.getArgs();
        final RiotApi riot = RiotMain.getRiotApi();


        if(args.isEmpty() || args.size() != 2){
            channel.sendMessage("Missing Args or too many args. The correct number of args should be 2").queue();
            return;
        }
        try {
            String accountID = riot.getSummonerByName((Platform.getPlatformByName(args.get(1))), args.get(0)).getAccountId();
            if (accountID == null) {
                channel.sendMessage("This summoner does not exist within our database.").queue();
                return;
            }

            MatchList matchList = riot.getMatchListByAccountId(Platform.getPlatformByName(args.get(1)), accountID);

            if (matchList.getTotalGames() < 3) {
                channel.sendMessage("Have not played 3 games on account specified").queue();
                return;
            }

            List<MatchReference> gameList = matchList.getMatches();
            EmbedBuilder message = new EmbedBuilder();

            message.setColor(Color.blue);

            message.setTitle("Last Three Games from " + args.get(0));

            for (int i = 0; i < 3; i++) {
                String result = isWin(riot, matchList.getMatches().get(i).getGameId(),
                        Platform.getPlatformByName(args.get(1)), args.get(0));
                String msg = "";
                MatchReference currGame = gameList.get(i);
                String lane = currGame.getLane().substring(0,1).toUpperCase() + currGame.getLane().substring(1).toLowerCase();
                msg += "Gamemode: " + RiotMatch.queueIdFinder(currGame.getQueue()) +
                        "\nChampion: " + DataHolder.getChampById(currGame.getChampion()).getName() +
                        "\nLane: " + lane +
                        "\nTime of Game: " + RiotMatch.dateFormatter(currGame.getTimestamp()) +
                        "\n\n";
                message.addField("Game " + (i+1) + " (" + result + ")", msg,true);
            }
            channel.sendMessage(message.build()).queue();
        }catch (RiotApiException e){
            e.printStackTrace();
        }



    }

    @Override
    public String getName() {
        return "ql3";
    }

    @Override
    public String getHelp() {
        return "Shows last three games played\n" +
                "Usage: `<ql3 <summoner name> <region>`";
    }
}
