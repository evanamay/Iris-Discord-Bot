package me.evana.command;

import me.evana.command.commands.CommandContext;
import net.rithms.riot.api.RiotApiException;

import java.util.Arrays;
import java.util.List;

public interface ICommand {
    void handle(CommandContext ctx) throws Exception;

    String getName();

    String getHelp();

    default List<String> getAliases() {
        return Arrays.asList();
    }
}