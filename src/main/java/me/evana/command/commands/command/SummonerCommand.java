//package me.evana.command.commands.command;
//
//import me.evana.command.ICommand;
//import me.evana.command.commands.CommandContext;
//import net.dv8tion.jda.api.entities.TextChannel;
//
//import java.util.List;
//
//public class SummonerCommand implements ICommand {
//
//
//    @Override
//    public void handle(CommandContext ctx){
//        final List<String> args = ctx.getArgs();
//        final TextChannel channel = ctx.getChannel();
//
//        if(args.size() != 2){
//            channel.sendMessage("Missing Arguments").queue();
//        }
//
//
//
//    }
//
//}
