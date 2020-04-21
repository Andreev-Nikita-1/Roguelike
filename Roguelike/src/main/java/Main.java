import basicComponents.AppLogic;
import basicComponents.Controller;
import com.googlecode.lanterna.TextColor;
import menuLogic.Menu;

import java.io.IOException;

import static java.lang.Thread.sleep;


public class Main {

    static Object l = new Object();

    public static void main(String[] args) throws IOException, InterruptedException {
        Controller.initialize();
        Menu.InitializeMenus();
        Controller.update();
        Controller.drawMenu(Menu.mainMenu);
        Thread thread = new Thread(Controller::run);
        thread.start();
    }

    static class Tester {
        void test() {
            synchronized (l) {
                while (true) {
                    try {
                        System.out.println("tick" + Thread.currentThread().isInterrupted());
                        l.wait(1000);
                    } catch (InterruptedException e) {
                        System.out.println("inter " + Thread.currentThread().isInterrupted());
                    }
                }
            }
        }
    }
}