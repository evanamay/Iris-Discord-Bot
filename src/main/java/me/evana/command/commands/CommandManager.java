package me.evana.command.commands;

import me.evana.command.ICommand;
import me.evana.command.commands.command.*;
//import me.evana.command.commands.command.SummonerCommand;
import me.evana.command.commands.commandQuick.QLastThree;
import me.evana.command.commands.commandQuick.QMasteryCommand;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.rithms.riot.api.RiotApiException;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;


public class CommandManager {
    private final List<ICommand> commands = new ArrayList<>();

    public CommandManager() {
        addCommand(new HelpCommand(this));
        addCommand(new SetPrefixCommand());
        addCommand(new AddPlayerInfoCommand());
        addCommand(new QLastThree());
        addCommand(new TestCommand());
        addCommand(new QMasteryCommand());
        addCommand(new MasteryCommand());
        addCommand(new LastThree());
    }

    private void addCommand(ICommand cmd) {
        boolean nameFound = this.commands.stream().anyMatch((it) -> it.getName().equalsIgnoreCase(cmd.getName()));

        if (nameFound) {
            throw new IllegalArgumentException("A command with this name is already present");
        }

        commands.add(cmd);
    }

    public List<ICommand> getCommands() {
        return commands;
    }

    @Nullable
    public ICommand getCommand(String search) {
        String searchLower = search.toLowerCase();

        for (ICommand cmd : this.commands) {
            if (cmd.getName().equals(searchLower) || cmd.getAliases().contains(searchLower)) {
                return cmd;
            }
        }

        return null;
    }

    public void handle(GuildMessageReceivedEvent event, String prefix) throws RiotApiException {
        String[] split = event.getMessage().getContentRaw()
                .replaceFirst("(?i)" + Pattern.quote(prefix), "")
                .split("\\s+");

        String invoke = split[0].toLowerCase();
        ICommand cmd = this.getCommand(invoke);

        if (cmd != null) {
            event.getChannel().sendTyping().queue();
            List<String> args = Arrays.asList(split).subList(1, split.length);

            CommandContext ctx = new CommandContext(event, args);

            try {
                cmd.handle(ctx);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}