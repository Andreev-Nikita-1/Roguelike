import basicComponents.AppLogic;
import basicComponents.Controller;
import com.googlecode.lanterna.TextColor;

import java.io.IOException;


public class Main {
    public static void main(String[] args) throws IOException {


        AppLogic.initialize();
        Controller.initialize();
        new Thread(() -> Controller.run()).start();
    }
}
