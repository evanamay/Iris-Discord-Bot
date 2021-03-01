package Events;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class HelloEvent extends ListenerAdapter {

    // Listens to messages coming in
    public void onGuildMessageReceived(GuildMessageReceivedEvent event){
        String msgSent = event.getMessage().getContentRaw();

        if(msgSent.equalsIgnoreCase("hello")){
            event.getChannel().sendMessage("Hi").queue();
        }
    }

}
