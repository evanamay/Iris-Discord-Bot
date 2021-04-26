package me.evana.command;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import me.evana.command.commands.RiotMain;
import me.evana.command.database.SQLiteDataSource;
import me.evana.command.emotes.EmoteHandler;
import me.evana.command.leagueinfo.Champion;
import me.evana.command.leagueinfo.DataHolder;
import me.evana.command.leagueinfo.Rune;
import me.evana.command.leagueinfo.SummonerSpell;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.rithms.riot.api.ApiConfig;
import net.rithms.riot.api.RiotApi;
import org.json.JSONArray;
import sun.reflect.generics.tree.Tree;


import javax.security.auth.login.LoginException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.*;

public class IrisMain {
    private static JDA jda;

    private IrisMain() throws LoginException, SQLException, IOException {
        SQLiteDataSource.getConnection();

        jda = new JDABuilder()
                .setToken(Config.get("TOKEN"))
                .addEventListeners(new Listener())
                .setActivity(Activity.watching("GC's Twitter"))
                .build();

        initializeRiotApi(Config.get("RIOT"));

        initSetup();




    }


    public static JDA getJda() {
        return jda;
    }


    private void initSetup() {
        makeChamps();
        makeSummonerSpells();
        makeRunes();
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
                champs.add(new Champion(key, id, name, logo));

            }

            DataHolder.setChampions(champs);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void makeSummonerSpells(){
        List<SummonerSpell> sums = new ArrayList<>();
        try(FileReader reader = new FileReader("src/main/resources/summoner.json")){
            JsonObject obj = JsonParser.parseReader(reader).getAsJsonObject().get("data").getAsJsonObject();
            Set<Map.Entry<String,JsonElement>> list = obj.entrySet();
            Iterator<Map.Entry<String, JsonElement>> itr = list.iterator();
            while(itr.hasNext()) {
                JsonElement element = itr.next().getValue();
                int key = element.getAsJsonObject().get("key").getAsInt();
                String id = element.getAsJsonObject().get("id").getAsString();
                String name = element.getAsJsonObject().get("name").getAsString();

                sums.add(new SummonerSpell(key, id, name));
            }
            DataHolder.setSummonerSpells(sums);

        }catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void makeRunes() {
        List<Rune> runes = new ArrayList<>();
        String[] treeNames = new String[5];
        long[] treeIDs = new long[5];

        try (FileReader reader = new FileReader("src/main/resources/runesReforged.json")) {
            JsonArray arr = JsonParser.parseReader(reader).getAsJsonArray();
            for(int i = 0; i < 5; i++){
                String namer = arr.get(i).getAsJsonObject().get("name").getAsString();
                treeNames[i] = namer;
                treeIDs[i] = (arr.get(i).getAsJsonObject().get("id").getAsLong());
                JsonArray tree = arr.get(i).getAsJsonObject().get("slots").getAsJsonArray();

                for(int j = 0; j < 4; j++){

                    JsonArray obj = tree.get(j).getAsJsonObject().get("runes").getAsJsonArray();
                    for(int x = 0; x < obj.size(); x++){

                        boolean isKeyStone = false;
                        if(x == 0){
                            isKeyStone = true;
                        }

                        int id = obj.get(x).getAsJsonObject().get("id").getAsInt();
                        String key = obj.get(x).getAsJsonObject().get("key").getAsString();
                        String name = obj.get(x).getAsJsonObject().get("name").getAsString();
                        runes.add(new Rune(id, name, key, isKeyStone, namer));
                    }

                }

            }
            DataHolder.setOverallRunes(treeNames,treeIDs);
            DataHolder.setRunes(runes);

        }catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

        public static void main(String[] args) throws LoginException, SQLException, IOException {
        new IrisMain();
    }

    public static void initializeRiotApi(String riotKey) {
        ApiConfig riotConfig = new ApiConfig().setKey(riotKey);

        new RiotMain(new RiotApi(riotConfig));
    }


}