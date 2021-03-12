package me.evana.command;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import jdk.nashorn.internal.parser.JSONParser;
import me.evana.command.commands.RiotMain;
import me.evana.command.database.SQLiteDataSource;
import me.evana.command.leagueinfo.Champion;
import me.evana.command.leagueinfo.DataHolder;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.rithms.riot.api.ApiConfig;
import net.rithms.riot.api.RiotApi;
import org.json.simple.JSONObject;


import javax.security.auth.login.LoginException;
import javax.xml.crypto.Data;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.*;

public class IrisMain {


    private IrisMain() throws LoginException, SQLException {
        SQLiteDataSource.getConnection();

        new JDABuilder()
                .setToken(Config.get("TOKEN"))
                .addEventListeners(new Listener())
                .setActivity(Activity.watching("GC's Twitter"))
                .build();

        initializeRiotApi(Config.get("RIOT"));

        makeChamps();


    }

    private void makeChamps() {
        List<Champion> champs = new ArrayList<>();
        try(FileReader reader = new FileReader("src/main/resources/champion.json")){
            JsonObject obj = JsonParser.parseReader(reader).getAsJsonObject().get("data").getAsJsonObject();
            Set<Map.Entry<String, JsonElement>> list = obj.entrySet();
            Iterator<Map.Entry<String, JsonElement>> itr = list.iterator();

            while(itr.hasNext()){
                JsonElement element = itr.next().getValue();
                int key = element.getAsJsonObject().get("key").getAsInt();
                String id = element.getAsJsonObject().get("id").getAsString();
                String name = element.getAsJsonObject().get("name").getAsString();
                File logo =
                        new File("src/main/resources/images/" + element.getAsJsonObject().get("image").getAsJsonObject().get("full").getAsString());
                champs.add(new Champion(key, id, name));

            }

            DataHolder.setChampions(champs);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws LoginException, SQLException {
        new IrisMain();
    }

    public static void initializeRiotApi(String riotKey) {
        ApiConfig riotConfig = new ApiConfig().setKey(riotKey);

        new RiotMain(new RiotApi(riotConfig));
    }


}