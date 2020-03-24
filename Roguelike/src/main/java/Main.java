import basicComponents.AppLogic;
import basicComponents.Controller;
import com.googlecode.lanterna.TextColor;

import java.io.IOException;


public class Main {
    public static void main(String[] args) throws IOException {
        Controller.initialize();
        new Thread(() -> Controller.run()).start();
    }
}
