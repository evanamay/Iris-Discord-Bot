package me.evana.command;

import me.evana.command.commands.RiotMain;
import me.evana.command.database.SQLiteDataSource;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.rithms.riot.api.ApiConfig;
import net.rithms.riot.api.RiotApi;


import javax.security.auth.login.LoginException;
import java.sql.SQLException;

public class IrisMain {
    private static RiotMain riot;

    private IrisMain() throws LoginException, SQLException {
        SQLiteDataSource.getConnection();

        new JDABuilder()
                .setToken(Config.get("TOKEN"))
                .addEventListeners(new Listener())
                .setActivity(Activity.watching("GC's Twitter"))
                .build();

        initializeRiotApi(Config.get("RIOT"));


    }

    public static void main(String[] args) throws LoginException, SQLException {
        new IrisMain();
    }

    public static void initializeRiotApi(String riotKey) {
        ApiConfig riotConfig = new ApiConfig().setKey(riotKey);

        riot = new RiotMain(new RiotApi(riotConfig));


    }


}