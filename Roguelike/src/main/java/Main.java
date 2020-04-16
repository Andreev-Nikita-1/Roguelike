import basicComponents.AppLogic;
import basicComponents.Controller;
import com.googlecode.lanterna.TextColor;

import java.io.IOException;

import static java.lang.Thread.sleep;


public class Main {

    static Object l = new Object();

    public static void main(String[] args) throws IOException, InterruptedException {
        Controller.initialize();
        Thread thread = new Thread(Controller::run);
        thread.start();
//        Thread tr = new Thread(() -> {
//            new Tester().test();
//        });
//        tr.start();
//        sleep(2000);
//        tr.interrupt();


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