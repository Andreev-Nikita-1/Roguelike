import basicComponents.AppLogic;
import basicComponents.Controller;
import com.googlecode.lanterna.TextColor;
import map.roomSystem.textures.RedCarpet1;
import map.roomSystem.textures.RoomTextures;
import menuLogic.Menu;

import java.awt.*;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import static java.lang.Thread.sleep;

import org.json.*;

public class Main {

    public static void main(String[] args) throws IOException, FontFormatException {

        Menu.InitializeMenus();
        Controller.update();
        Controller.drawMenu(Menu.mainMenu);
//        Controller.drawFileDialog();
//        Controller.drawInputDialog();
        Thread thread = new Thread(Controller::run);
        thread.start();
    }
}