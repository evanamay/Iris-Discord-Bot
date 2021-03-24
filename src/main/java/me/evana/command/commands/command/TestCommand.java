package me.evana.command.commands.command;

import me.evana.command.ICommand;
import me.evana.command.commands.CommandContext;
import me.evana.command.commands.RiotMain;
import me.evana.command.commands.RiotMatch;
import me.evana.command.leagueinfo.DataHolder;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.rithms.riot.api.RiotApi;
import net.rithms.riot.api.endpoints.champion_mastery.dto.ChampionMastery;
import net.rithms.riot.constant.Platform;

import java.awt.*;
import java.text.NumberFormat;
import java.util.List;

import static me.evana.command.commands.RiotMatch.getSummonerName;

public class TestCommand implements ICommand {

    @Override
    public void handle(CommandContext ctx) throws Exception {
        final TextChannel channel = ctx.getChannel();
        final List<String> args = ctx.getArgs();
        // summonerName = getSummonerName(args,2);
        //String plat = args.get(args.size()-2);

        if(ctx.getEvent().getMessage().getMentionedMembers().size() != 1){
            channel.sendMessage("Error: Please mention one user").queue();
            return;
        }



        String id= ctx.getEvent().getMessage().getMentionedMembers().get(0).getUser().getId();
        channel.sendMessage(id).queue();
        return;

    }

    @Override
    public String getName() {
        return "test";
    }

    @Override
    public String getHelp() {
        return "no need help";
    }
}
