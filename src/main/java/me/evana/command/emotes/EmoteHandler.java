package me.evana.command.emotes;

import me.evana.command.IrisMain;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Emote;
import net.dv8tion.jda.api.entities.Guild;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class EmoteHandler {

    private static List<ChampionEmotes> customEmotes = new ArrayList<>();


    public static void loadEmotes() throws IOException {
        List<Emote> uploadedEmotes = getAllGuildCustomEmotes();
        List<ChampionEmotes> downloadedImages = loadEmoteImages();

        assigneAlreadyUploadedEmoteToPicturesInFile(uploadedEmotes, downloadedImages);


        getChampionEmotes().addAll(downloadedImages);
        assigneCustomEmotesToData();

    }
    public static void assigneCustomEmotesToData() {
        for(ChampionEmotes emote : getChampionEmotes()) {
            EmoteUtils.addToChampionIfIsSame(emote);
        }
    }


    public static List<ChampionEmotes> loadEmoteImages() {
        List<ChampionEmotes> emotes = new ArrayList<>();

        File folder = new File("src/main/resources/images");
        File[] listOfFiles = folder.listFiles();

        for(int i = 0; i < listOfFiles.length; i++) {
            String name = listOfFiles[i].getName();
            if(name.endsWith(".png") || name.endsWith(".jpg")) {
                name = name.substring(0, name.length() - 4);
                emotes.add(new ChampionEmotes(name, listOfFiles[i]) {
                });
            }
        }
        return emotes;
    }

    private static void assigneAlreadyUploadedEmoteToPicturesInFile(List<Emote> uploadedEmotes, List<ChampionEmotes> picturesInFile) {
        for(ChampionEmotes customeEmote : picturesInFile) {
            for(Emote emote : uploadedEmotes) {
                if(emote.getName().equalsIgnoreCase(customeEmote.getName())) {
                    customeEmote.setEmote(emote);
                }
            }
        }
    }



    private static List<Emote> getAllGuildCustomEmotes() throws IOException {
        JDA jda = IrisMain.getJda();
        List<Emote> uploadedEmotes = jda.getEmotes();



//        try(final BufferedReader reader = new BufferedReader(new FileReader(Ressources.GUILD_EMOTES_FILE));) {
//            String line;
//            while((line = reader.readLine()) != null) {
//                listGuild.add(IrisMain.getJda().getGuildById(line));
//            }
//        }


//        for(Guild guild : listGuild) {
//            uploadedEmotes.addAll(guild.retrieveEmotes().complete());
//        }
        return uploadedEmotes;
    }


    public static List<ChampionEmotes> getChampionEmotes(){
        return customEmotes;
    }

}
