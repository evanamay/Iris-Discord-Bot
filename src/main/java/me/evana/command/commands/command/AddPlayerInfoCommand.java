package me.evana.command.commands.command;

import me.evana.command.ICommand;
import me.evana.command.commands.CommandContext;
import me.evana.command.commands.RiotMain;
import me.evana.command.database.SQLiteDataSource;
import net.dv8tion.jda.api.entities.TextChannel;
import net.rithms.riot.api.RiotApi;
import net.rithms.riot.api.RiotApiException;
import net.rithms.riot.api.endpoints.summoner.dto.Summoner;
import net.rithms.riot.constant.Platform;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import static net.rithms.riot.api.endpoints.static_data.constant.ItemTags.FROM;

public class AddPlayerInfoCommand implements ICommand {

    @Override
    public void handle(CommandContext ctx) {
        final TextChannel channel = ctx.getChannel();
        final List<String> args = ctx.getArgs();
        final RiotApi riot = RiotMain.getRiotApi();

        if (args.isEmpty() || args.size() != 2) {
            channel.sendMessage("Missing args").queue();
            return;
        }


        try {
            Summoner summoner = riot.getSummonerByName(Platform.getPlatformByName(args.get(1)), args.get(0));
            if (alreadyExists(summoner, args.get(1))) {
                channel.sendMessage("Summoner name already exists");
            }
            addPlayerInfo(summoner, args.get(1));

            channel.sendMessageFormat("Player " + summoner.getName() + " has been added to database!").queue();
        }catch (RiotApiException e){
            e.printStackTrace();
        }
    }

    private boolean alreadyExists(Summoner summoner, String region) {
        try(ResultSet rs = SQLiteDataSource
            .getConnection()
            .createStatement()
            .executeQuery("SELECT summoner_name, region FROM player_information")) {

            while(rs.next()){
                if (summoner.getName().equals(rs.getString("summoner_name"))
                && region.equals(rs.getString("region"))){
                    return true;
                }
            }
        }catch (SQLException e){
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public String getName() {
        return "addinfo";
    }

    @Override
    public String getHelp() {
        return "Adds Summoner to database\n" +
                "Usage: `!!addInfo <summoner name> <region>`";
    }


    private void addPlayerInfo(Summoner summoner, String region){

        try (final PreparedStatement preparedStatement = SQLiteDataSource
                .getConnection()
                // language=SQLite
                .prepareStatement("INSERT INTO player_information(summoner_name,region,summoner_id,account_id,puuid) values(?,?,?,?,?)")){
            preparedStatement.setString(1,summoner.getName());
            preparedStatement.setString(2,region);
            preparedStatement.setString(3,summoner.getId());
            preparedStatement.setString(4,summoner.getAccountId());
            preparedStatement.setString(5,summoner.getPuuid());

            preparedStatement.executeUpdate();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

}
