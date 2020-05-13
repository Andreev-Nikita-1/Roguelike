import basicComponents.AppLogic;
import basicComponents.Controller;
import com.google.protobuf.ByteString;
import com.googlecode.lanterna.TextColor;
import menuLogic.Menu;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static java.lang.Thread.sleep;


public class Main {

    public static void main(String[] args) throws IOException, InterruptedException {
        var server = Server.run(6969);
        var client = new Client("localhost", 6969);
        System.out.println("Do join...");
        int hero_id = client.join(Model.JoinMessage.newBuilder().build());
        System.out.println(hero_id);
        System.out.println("Do doHeroAction...");
        client.doHeroAction(
                Model.HeroAction.newBuilder()
                        .setHeroId(hero_id)
                        .setType(Model.HeroAction.Type.DIRECTED_ACTION)
                        .setAction(Model.HeroAction.Action.ATTACK)
                        .setDirection(Model.HeroAction.Direction.DOWN)
                        .setEventTime(2281488)
                        .build()
        );
        System.out.println("Do getPixels...");
        ByteString nikita = client.getPixels(
                Model.GetPixelsMessage.newBuilder()
                        .setHeroId(hero_id)
                        .build()
        );
        System.out.println("Pixels:");
        System.out.println(nikita.toString(StandardCharsets.UTF_16));
        /*
        Controller.initialize();
        Menu.InitializeMenus();
        Controller.update();
        Controller.drawMenu(Menu.mainMenu);
        Thread thread = new Thread(Controller::run);
        thread.start();
        */
    }
}