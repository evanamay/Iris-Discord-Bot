package me.evana.command.commands.command;

import me.evana.command.ICommand;
import me.evana.command.commands.CommandContext;
import me.evana.command.commands.RiotMain;
import me.evana.command.commands.RiotMatch;
import me.evana.command.database.SQLiteDataSource;
import me.evana.command.leagueinfo.DataHolder;
import net.dv8tion.jda.api.entities.TextChannel;
import net.rithms.riot.api.RiotApi;
import net.rithms.riot.api.RiotApiException;
import net.rithms.riot.api.endpoints.match.dto.MatchList;
import net.rithms.riot.api.endpoints.match.dto.MatchReference;
import net.rithms.riot.api.endpoints.summoner.dto.Summoner;
import net.rithms.riot.constant.Platform;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

public class GetLast3Games implements ICommand {

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
            String accountID = getAccountID(args.get(0), args.get(1));
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
            channel.sendMessage("Last three games from " + args.get(0)).queue();
            for (int i = 0; i < 5; i++) {
                MatchReference currGame = gameList.get(i);
                channel.sendMessage("Gamemode: " + RiotMatch.queueIdFinder(currGame.getQueue()) +
                        "\nChampion: " + DataHolder.getChampById(currGame.getChampion()) +
                        "\nLane: " + RiotMatch.laneFinder(currGame.getLane(),currGame.getRole()) +
                        "\nTime of Game: " + RiotMatch.dateFormatter(currGame.getTimestamp()) +
                        "\n\n"
                ).queue();
            }
        }catch (RiotApiException e){
            e.printStackTrace();
        }



    }


    private String getAccountID(String summonerName, String region) {
        try(ResultSet rs = SQLiteDataSource
                .getConnection()
                .createStatement()
                .executeQuery("SELECT summoner_name, region, account_id FROM player_information")) {

            while(rs.next()){
                if (summonerName.equals(rs.getString("summoner_name"))
                        && region.equals(rs.getString("region"))){
                    return rs.getString("account_id");
                }
            }
        }catch (SQLException e){
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public String getName() {
        return "lastthree";
    }

    @Override
    public String getHelp() {
        return "Shows last three games played\n" +
                "Usage: `<lastthree <summoner name> <region>`";
    }
}
