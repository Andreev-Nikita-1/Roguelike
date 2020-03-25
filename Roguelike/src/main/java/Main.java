import basicComponents.AppLogic;
import basicComponents.Controller;
import com.googlecode.lanterna.TextColor;

import java.io.IOException;

import static java.lang.Thread.sleep;


public class Main {

    public static void main(String[] args) throws IOException {
        Controller.initialize();
        Thread thread = new Thread(() -> Controller.run());
//        thread.setPriority(10);
        thread.start();
    }
}