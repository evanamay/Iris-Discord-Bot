package me.evana.command.commands.command;

import me.evana.command.ICommand;
import me.evana.command.VeryBadDesign;
import me.evana.command.commands.CommandContext;
import me.evana.command.database.SQLiteDataSource;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class SetPrefixCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) {
        final TextChannel channel = ctx.getChannel();
        final List<String> args = ctx.getArgs();
        final Member member = ctx.getMember();

        if (!member.hasPermission(Permission.MANAGE_SERVER)) {
            channel.sendMessage("You must have the Manage Server permission to use his command").queue();
            return;
        }

        if (args.isEmpty()) {
            channel.sendMessage("Missing args").queue();
            return;
        }

        final String newPrefix = String.join("", args);
        updatePrefix(ctx.getGuild().getIdLong(), newPrefix);

        channel.sendMessageFormat("New prefix has been set to `%s`", newPrefix).queue();
    }

    @Override
    public String getName() {
        return "setprefix";
    }

    @Override
    public String getHelp() {
        return "Sets the prefix for this server\n" +
                "Usage: `!!setprefix <prefix>`";
    }

    private void updatePrefix(long guildId, String newPrefix) {
        VeryBadDesign.PREFIXES.put(guildId, newPrefix);

        try (final PreparedStatement preparedStatement = SQLiteDataSource
                .getConnection()
                // language=SQLite
                .prepareStatement("UPDATE guild_settings SET prefix = ? WHERE guild_id = ?")) {

            preparedStatement.setString(1, newPrefix);
            preparedStatement.setString(2, String.valueOf(guildId));

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}