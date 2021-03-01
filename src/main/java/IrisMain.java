import Events.HelloEvent;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;


public class IrisMain {

    public static void main(String args[]) throws Exception {

        JDA jda = new JDABuilder("Njg5ODgwMTg3NjE3NTQyMTc2.XnJS_g.-u8nYn0OfMqwsHWXeLAW8zCj7HA").build();


        jda.addEventListener(new HelloEvent());


    }
}
