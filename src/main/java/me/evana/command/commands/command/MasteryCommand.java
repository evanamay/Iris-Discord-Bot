package me.evana.command.commands.command;

import me.evana.command.ICommand;
import me.evana.command.commands.CommandContext;
import me.evana.command.commands.RiotMain;
import me.evana.command.leagueinfo.DataHolder;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;
import net.rithms.riot.api.RiotApi;
import net.rithms.riot.api.endpoints.champion_mastery.dto.ChampionMastery;
import net.rithms.riot.constant.Platform;

import java.awt.*;
import java.text.NumberFormat;
import java.util.List;

import static me.evana.command.commands.RiotMatch.getSummonerName;

public class MasteryCommand implements ICommand {

    @Override
    public void handle(CommandContext ctx) throws Exception {
        final TextChannel channel = ctx.getChannel();
        final List<String> args = ctx.getArgs();
        String summonerName = getSummonerName(args,2);
        String plat = args.get(args.size()-2);

        if(args.size() != 3 || Integer.parseInt(args.get(args.size()-1)) > 50 || Integer.parseInt(args.get(args.size()-1)) < 1){
            channel.sendMessage("Error: Wrong number of arguments").queue();
            return;
        }

        NumberFormat myFormat = NumberFormat.getInstance();
        myFormat.setGroupingUsed(true);
        final RiotApi riot = RiotMain.getRiotApi();

        EmbedBuilder message = new EmbedBuilder();

        message.setColor(Color.blue);

        message.setTitle(summonerName);
        List<ChampionMastery> championMasteries = riot.getChampionMasteriesBySummoner
                (Platform.getPlatformByName("NA"),riot.getSummonerByName(Platform.getPlatformByName(plat),summonerName).getId());

        String msg= "";
        for(int i = 0; i < Integer.parseInt(args.get(args.size()-1)); i++){
            msg+= i+1+". " + DataHolder.getChampById(championMasteries.get(i).getChampionId()).getName() +
                    " > Mastery " + championMasteries.get(i).getChampionLevel()
                    + " (" + myFormat.format(championMasteries.get(i).getChampionPoints()) + " pts)\n";
        }

        message.addField("Top Champion Mastery: ", msg , true);

        channel.sendMessage(message.build()).queue();

    }

    @Override
    public String getName() {
        return "mastery";
    }

    @Override
    public String getHelp() {
        return "Returns champion mastery of X number of champions\n" +
                "Usage: `<mastery <summoner name> <region> <list size>`";
    }
}
