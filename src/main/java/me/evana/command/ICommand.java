package me.evana.command;

import me.evana.command.commands.CommandContext;

import java.util.Arrays;
import java.util.List;

public interface ICommand {
    void handle(CommandContext ctx);

    String getName();

    String getHelp();

    default List<String> getAliases() {
        return Arrays.asList();
    }
}