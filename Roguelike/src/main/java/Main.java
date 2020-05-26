import basicComponents.AppLogic;
import basicComponents.Controller;
import com.googlecode.lanterna.TextColor;
import menuLogic.Menu;

import java.awt.*;
import java.io.IOException;

import static java.lang.Thread.sleep;


public class Main {

    public static void main(String[] args) throws IOException, FontFormatException {
        Menu.InitializeMenus();
        Controller.update();
        Controller.drawMenu(Menu.mainMenu);
        Thread thread = new Thread(Controller::run);
        thread.start();
    }
}