package me.evana.command.commands.command;

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
import net.rithms.riot.api.endpoints.match.dto.MatchList;
import net.rithms.riot.api.endpoints.match.dto.MatchReference;
import net.rithms.riot.constant.Platform;

import java.awt.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import static me.evana.command.commands.RiotMatch.isWin;

public class LastThree implements ICommand {

    @Override
    public void handle(CommandContext ctx) throws Exception {
        final TextChannel channel = ctx.getChannel();
        final List<String> args = ctx.getArgs();
        final RiotApi riot = RiotMain.getRiotApi();


        if(args.isEmpty() || args.size() != 1){
            channel.sendMessage("Missing Args or too many args. The correct number of args should be 2").queue();
            return;
        }

        String id = ctx.getEvent().getMessage().getMentionedMembers().get(0).getUser().getId();

        try(ResultSet rs = SQLiteDataSource
                .getConnection()
                .createStatement()
                .executeQuery("SELECT summoner_name, region, account_id, user_link FROM player_information")) {

            while(rs.next()){
                if(rs.getString("user_link").equals(id)){
                    MatchList matchList = riot.getMatchListByAccountId(Platform.getPlatformByName
                            (rs.getString("region")), rs.getString("account_id"));

                    if (matchList.getTotalGames() < 3) {
                        channel.sendMessage("Have not played 3 games on account specified").queue();
                        return;
                    }

                    List<MatchReference> gameList = matchList.getMatches();

                    EmbedBuilder message = new EmbedBuilder();
                    message.setColor(Color.blue);
                    message.setTitle("Last Three Games from " + rs.getString("summoner_name"));


                    for (int i = 0; i < 3; i++) {
                        String result = isWin(riot, matchList.getMatches().get(i).getGameId(),
                                Platform.getPlatformByName(rs.getString("region")),
                                rs.getString("summoner_name"));
                        String msg = "";
                        MatchReference currGame = gameList.get(i);
                        String gamemode = RiotMatch.queueIdFinder(currGame.getQueue());
                        String lane;
                        if(gamemode.equals("Aram")){
                            lane = "Mid";
                        }else{
                            lane = currGame.getLane().substring(0,1).toUpperCase() +
                                    currGame.getLane().substring(1).toLowerCase();
                        }

                        msg += "Gamemode: " + gamemode +
                                "\nChampion: " + DataHolder.getChampById(currGame.getChampion()).getName() +
                                "\nLane: " + lane +
                                "\nTime of Game: " + RiotMatch.dateFormatter(currGame.getTimestamp()) +
                                "\n\n";
                        message.addField("Game " + (i+1) + " (" + result + ")", msg,true);
                    }
                    channel.sendMessage(message.build()).queue();

                }

            }
        }catch (SQLException e){
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
        return "l3";
    }

    @Override
    public String getHelp() {
        return "Shows last three games played\n" +
                "Usage: `<l3 @<discord user>`";
    }
}
