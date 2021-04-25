package me.evana.command.commands.command;

import me.evana.command.ICommand;
import me.evana.command.commands.CommandContext;
import me.evana.command.commands.RiotMain;
import me.evana.command.database.SQLiteDataSource;
import me.evana.command.leagueinfo.DataHolder;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;
import net.rithms.riot.api.RiotApi;
import net.rithms.riot.api.endpoints.champion_mastery.dto.ChampionMastery;
import net.rithms.riot.constant.Platform;

import java.awt.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.util.List;

public class MasteryCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) throws Exception {
        final TextChannel channel = ctx.getChannel();
        final List<String> args = ctx.getArgs();

        String id = ctx.getEvent().getMessage().getMentionedMembers().get(0).getUser().getId();

        if(args.size() != 2){
            channel.sendMessage("Error: Wrong number of arguments").queue();
            return;
        }

        NumberFormat myFormat = NumberFormat.getInstance();
        myFormat.setGroupingUsed(true);
        final RiotApi riot = RiotMain.getRiotApi();

        EmbedBuilder message = new EmbedBuilder();

        message.setColor(Color.blue);

        try(ResultSet rs = SQLiteDataSource
                .getConnection()
                .createStatement()
                .executeQuery("SELECT summoner_name, region, user_link FROM player_information")) {

            while(rs.next()){
                if(rs.getString("user_link").equals(id)){

                    message.setTitle(rs.getString("summoner_name"));
                    List<ChampionMastery> championMasteries = riot.getChampionMasteriesBySummoner
                            (Platform.getPlatformByName(rs.getString("region")),
                                    riot.getSummonerByName(Platform.getPlatformByName(rs.getString("region")),
                                            rs.getString("summoner_name")).getId());

                    String msg= "";
                    for(int i = 0; i < Integer.parseInt(args.get(args.size()-1)); i++){
                        msg += i+1+". " + DataHolder.getChampById(championMasteries.get(i).getChampionId()).getName() +
                                " > Mastery " + championMasteries.get(i).getChampionLevel()
                                + " (" + myFormat.format(championMasteries.get(i).getChampionPoints()) + " pts)\n";
                    }

                    message.addField("Top Champion Mastery: ", msg , true);

                    channel.sendMessage(message.build()).queue();
                }
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    @Override
    public String getName() {
        return "mastery";
    }

    @Override
    public String getHelp() {
        return "Returns champion mastery of X number of champions\n" +
                "Usage: `<mastery @<discord user> <number of champions>`";
    }
}
