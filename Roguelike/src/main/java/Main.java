import basicComponents.AppLogic;
import basicComponents.Controller;
import com.googlecode.lanterna.TextColor;

import java.io.IOException;

import static java.lang.Thread.sleep;


public class Main {

    public static String a = "1";

    public static void main(String[] args) throws IOException {
        Controller.initialize();
        Thread thread = new Thread(() -> Controller.run());
        thread.setPriority(10);
        thread.start();

    }
}
//
//class A implements Runnable {
//
//    @Override
//    public void run() {
//        synchronized (Main.a) {
//            while (true) {
//                System.out.println("aaa");
//                try {
//                    Main.a.wait(1000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//    }
//}